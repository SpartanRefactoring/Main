package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Convert Infinite loops with return sideEffects to shorter ones : </br>
 * Convert <br/>
 * <code>
 * for(;true;) { <br/>
 *    doSomething(); <br/>
 *    if(done()) <br/>
 *      break; <br/>
 * } <br/>
 *return XX; <br/>
 * </code> to : <br/>
 * <code> for(;true;) { <br/>
 * doSomething(); <br/>
 * if(done()) <br/>
 * return XX; <br/>
 * } <br/>
 * @author Dor Ma'ayan
 * @since 2016-09-09 */
public final class BlockBreakToReturnInfiniteFor extends CarefulTipper<ForStatement>//
    implements TipperCategory.Shortcircuit {
  @Nullable private static Statement handleIf(@Nullable final IfStatement s, final ReturnStatement nextReturn) {
    return s == null ? null : handleIf(then(s), elze(s), nextReturn);
  }

  private static Statement handleIf(final Statement then, @Nullable final Statement elze, final ReturnStatement nextReturn) {
    if (iz.breakStatement(then))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock(az.block(then), nextReturn);
      if ($ != null)
        return $;
    }
    if (iz.ifStatement(then))
      return handleIf(then, nextReturn);
    if (elze == null)
      return null;
    if (iz.breakStatement(elze))
      return elze;
    if (iz.block(elze)) {
      final Statement $ = handleBlock(az.block(elze), nextReturn);
      if ($ != null)
        return $;
    }
    return iz.ifStatement(elze) ? null : handleIf(elze, nextReturn);
  }

  private static Statement make(final Statement s, final ReturnStatement nextReturn) {
    return iz.breakStatement(s) ? s //
        : iz.ifStatement(s) ? handleIf(s, nextReturn) //
            : iz.block(s) ? handleBlock(az.block(s), nextReturn) //
                : null;
  }

  @Nullable private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(b)) {
      if (iz.ifStatement(¢))
        $ = handleIf(az.ifStatement(¢), nextReturn);
      if (iz.breakStatement(¢))
        return ¢;
    }
    return $;
  }

  @Nullable private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    return handleIf(az.ifStatement(s), nextReturn);
  }

  private static boolean isInfiniteLoop(@NotNull final ForStatement ¢) {
    return az.booleanLiteral(¢.getExpression()) != null && az.booleanLiteral(¢.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the break inside 'for(;;)' to 'return'";
  }

  @Override @NotNull public String description(@NotNull final ForStatement ¢) {
    return "Convert the break inside 'for(" + initializers(¢) + "; " + ¢.getExpression() + ";" + updaters(¢) + " to return";
  }

  private Tip make(@NotNull final ForStatement vor, @NotNull final ReturnStatement nextReturn, @Nullable final ExclusionManager exclude) {
    final Statement $ = make(body(vor), nextReturn);
    if (exclude != null)
      exclude.exclude(vor);
    return $ == null ? null : new Tip(description(), vor, getClass(), nextReturn) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        r.remove(nextReturn, g);
      }
    };
  }

  @Override public boolean prerequisite(@Nullable final ForStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && isInfiniteLoop(¢);
  }

  @Override @Nullable public Tip tip(@Nullable final ForStatement vor, final ExclusionManager exclude) {
    if (vor == null || !isInfiniteLoop(vor))
      return null;
    final ReturnStatement $ = extract.nextReturn(vor);
    return $ == null ? null : make(vor, $, exclude);
  }
}

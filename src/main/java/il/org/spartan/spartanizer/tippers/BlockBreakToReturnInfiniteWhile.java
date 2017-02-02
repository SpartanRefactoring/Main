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
 * while (true) { <br/>
 * doSomething(); <br/>
 * if(done()) <br/>
 * break; <br/>
 * } <br/>
 *return XX; <br/>
 * </code> to : <br/>
 * <code> while (true) { <br/>
 * doSomething(); <br/>
 * if(done()) <br/>
 * return XX; <br/>
 * } <br/>
 * @author Dor Ma'ayan
 * @since 2016-09-09 */
public final class BlockBreakToReturnInfiniteWhile extends CarefulTipper<WhileStatement>//
    implements TipperCategory.Shortcircuit {
  @Nullable private static Statement handleBlock(final Block body, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : step.statements(body)) {
      if (iz.ifStatement(¢))
        $ = handleIf(az.ifStatement(¢), nextReturn);
      if (iz.breakStatement(¢)) {
        $ = ¢;
        break;
      }
    }
    return $;
  }

  private static Statement handleIf(final IfStatement s, final ReturnStatement nextReturn) {
    final IfStatement ifStatement = az.ifStatement(s);
    if (ifStatement == null)
      return null;
    final Statement then = ifStatement.getThenStatement(), elze = ifStatement.getElseStatement();
    if (then != null) {
      if (iz.breakStatement(then))
        return then;
      if (iz.block(then)) {
        final Statement $ = handleBlock((Block) then, nextReturn);
        if ($ != null)
          return $;
      }
      if (iz.ifStatement(then))
        return handleIf(az.ifStatement(then), nextReturn);
      if (elze != null) {
        if (iz.breakStatement(elze))
          return elze;
        if (iz.block(elze)) {
          final Statement $ = handleBlock((Block) elze, nextReturn);
          if ($ != null)
            return $;
        }
        if (iz.ifStatement(elze))
          return handleIf(az.ifStatement(elze), nextReturn);
      }
    }
    return null;
  }

  private static boolean isInfiniteLoop(@NotNull final WhileStatement ¢) {
    return az.booleanLiteral(¢.getExpression()) != null && az.booleanLiteral(¢.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the break inside 'while()' loop to 'return'";
  }

  @Override @NotNull public String description(@NotNull final WhileStatement ¢) {
    return "Convert the break inside 'while(" + ¢.getExpression() + ")' to return";
  }

  @Override public boolean prerequisite(@Nullable final WhileStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && isInfiniteLoop(¢);
  }

  @Override public Tip tip(@Nullable final WhileStatement s, @Nullable final ExclusionManager exclude) {
    final ReturnStatement nextReturn = extract.nextReturn(s);
    if (s == null || !isInfiniteLoop(s) || nextReturn == null)
      return null;
    final Statement body = body(s), //
        $ = iz.ifStatement(body) ? handleIf(az.ifStatement(body), nextReturn) //
            : iz.block(body) ? handleBlock(az.block(body), nextReturn) //
                : iz.breakStatement(body) ? body : null;
    if (exclude != null)
      exclude.exclude(s);
    return $ == null ? null : new Tip(description(s), s.getExpression(), getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        r.remove(nextReturn, g);
      }
    };
  }
}

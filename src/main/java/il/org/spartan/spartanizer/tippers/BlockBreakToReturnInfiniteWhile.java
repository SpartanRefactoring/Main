package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Convert Infinite loops with return sideEffects to shorter ones : toList
 * Convert {@code while (true) { doSomething(); if(done()) break; } return XX; }
 * to : {@code while (true) { doSomething(); if(done()) return XX; } }
 * @author Dor Ma'ayan
 * @since 2016-09-09 */
public final class BlockBreakToReturnInfiniteWhile extends CarefulTipper<WhileStatement>//
    implements TipperCategory.Shortcircuit {
  private static final long serialVersionUID = -6223876197494261787L;

  @Nullable
  private static Statement handleBlock(final Block body, final ReturnStatement nextReturn) {
    @Nullable Statement $ = null;
    for (final Statement ¢ : statements(body)) {
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
    @Nullable final IfStatement ifStatement = az.ifStatement(s);
    if (ifStatement == null)
      return null;
    final Statement then = ifStatement.getThenStatement(), elze = ifStatement.getElseStatement();
    if (then != null) {
      if (iz.breakStatement(then))
        return then;
      if (iz.block(then)) {
        @Nullable final Statement $ = handleBlock((Block) then, nextReturn);
        if ($ != null)
          return $;
      }
      if (iz.ifStatement(then))
        return handleIf(az.ifStatement(then), nextReturn);
      if (elze != null) {
        if (iz.breakStatement(elze))
          return elze;
        if (iz.block(elze)) {
          @Nullable final Statement $ = handleBlock((Block) elze, nextReturn);
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

  @NotNull
  @Override public String description(@NotNull final WhileStatement ¢) {
    return "Convert the break inside 'while(" + ¢.getExpression() + ")' to return";
  }

  @Override public boolean prerequisite(@Nullable final WhileStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && isInfiniteLoop(¢);
  }

  @Override public Tip tip(@Nullable final WhileStatement s, @Nullable final ExclusionManager exclude) {
    @Nullable final ReturnStatement nextReturn = extract.nextReturn(s);
    if (s == null || !isInfiniteLoop(s) || nextReturn == null)
      return null;
    @NotNull final Statement body = body(s), //
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

package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

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

/** Convert Finite loops with return sideEffects to shorter ones : </br>
 * Convert 
 * <code>
 * for (..) { 
 *  does(something); 
 *   return XX; 
 * } 
 *return XX; 
 * </code> to : 
 * <code>
 * for (..) { 
 *  does(something); 
 *   break; 
 * } 
 *return XX; 
 * </code>
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public final class ReturnToBreakFiniteWhile extends CarefulTipper<WhileStatement>//
    implements TipperCategory.CommnonFactoring {
  private static boolean compareReturnStatements(@Nullable final ReturnStatement r1, @Nullable final ReturnStatement r2) {
    return r1 != null && r2 != null && (r1.getExpression() + "").equals(r2.getExpression() + "");
  }

  @Nullable private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(b)) {
      if (az.ifStatement(¢) != null)
        $ = handleIf(¢, nextReturn);
      if (compareReturnStatements(nextReturn, az.returnStatement(¢)))
        return ¢;
    }
    return $;
  }

  @Nullable private static Statement handleIf(@Nullable final IfStatement s, final ReturnStatement nextReturn) {
    return s == null ? null : handleIf(then(s), elze(s), nextReturn);
  }

  @Nullable private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    return handleIf(az.ifStatement(s), nextReturn);
  }

  @Nullable private static Statement handleIf(@Nullable final Statement then, @Nullable final Statement elze, final ReturnStatement nextReturn) {
    if (then == null)
      return null;
    if (compareReturnStatements(az.returnStatement(then), nextReturn))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock((Block) then, nextReturn);
      if ($ != null)
        return $;
    }
    if (az.ifStatement(then) != null)
      return handleIf(then, nextReturn);
    if (elze != null) {
      if (compareReturnStatements(az.returnStatement(elze), nextReturn))
        return elze;
      if (iz.block(elze)) {
        final Statement $ = handleBlock((Block) elze, nextReturn);
        if ($ != null)
          return $;
      }
      if (az.ifStatement(elze) != null)
        return handleIf(elze, nextReturn);
    }
    return null;
  }

  private static boolean isInfiniteLoop(@NotNull final WhileStatement ¢) {
    return az.booleanLiteral(¢.getExpression()) != null && az.booleanLiteral(¢.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override @NotNull public String description(final WhileStatement b) {
    return "Convert the return inside " + b + " to break";
  }

  @Override public boolean prerequisite(@Nullable final WhileStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && !isInfiniteLoop(¢);
  }

  @Override public Tip tip(@Nullable final WhileStatement b, @Nullable final ExclusionManager exclude) {
    final ReturnStatement nextReturn = extract.nextReturn(b);
    if (b == null || isInfiniteLoop(b) || nextReturn == null)
      return null;
    final Statement body = body(b), $ = iz.returnStatement(body) && compareReturnStatements(nextReturn, az.returnStatement(body)) ? body
        : iz.block(body) ? handleBlock(az.block(body), nextReturn) : az.ifStatement(body) == null ? null : handleIf(body, nextReturn);
    if (exclude != null)
      exclude.exclude(b);
    return $ == null ? null : new Tip(description(), b, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace($, az.astNode(first(statements(az.block(into.s("break;"))))), g);
      }
    };
  }
}

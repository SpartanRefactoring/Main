package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert Finite loops with return sideEffects to shorter ones : toList
 * Convert {@code for (..) { does(something); return XX; } return XX; } to :
 * {@code for (..) { does(something); break; } return XX; }
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public final class ReturnToBreakFiniteFor extends CarefulTipper<ForStatement>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -3493773208955099533L;

  private static boolean compareReturnStatements(@Nullable final ReturnStatement r1, @Nullable final ReturnStatement r2) {
    return r1 != null && r2 != null && (r1.getExpression() + "").equals(r2.getExpression() + "");
  }

  @Nullable private static Statement handleBlock(final Block body, final ReturnStatement nextReturn) {
    @Nullable Statement $ = null;
    for (final Statement ¢ : statements(body)) {
      if (az.ifStatement(¢) != null)
        $ = handleIf(¢, nextReturn);
      if (compareReturnStatements(nextReturn, az.returnStatement(¢))) {
        $ = ¢;
        break;
      }
    }
    return $;
  }

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    if (!iz.ifStatement(s))
      return null;
    @Nullable final IfStatement ifStatement = az.ifStatement(s);
    final Statement then = ifStatement.getThenStatement(), elze = ifStatement.getElseStatement();
    if (then == null)
      return null;
    if (compareReturnStatements(az.returnStatement(then), nextReturn))
      return then;
    if (iz.block(then)) {
      @Nullable final Statement $ = handleBlock((Block) then, nextReturn);
      if ($ != null)
        return $;
    }
    if (az.ifStatement(then) != null)
      return handleIf(then, nextReturn);
    if (elze == null)
      return null;
    if (compareReturnStatements(az.returnStatement(elze), nextReturn))
      return elze;
    if (!iz.block(elze))
      return az.ifStatement(elze) == null ? null : handleIf(elze, nextReturn);
    @Nullable final Statement $ = handleBlock((Block) elze, nextReturn);
    return $ != null ? $ : az.ifStatement(elze) == null ? null : handleIf(elze, nextReturn);
  }

  private static boolean isInfiniteLoop(@NotNull final ForStatement ¢) {
    return iz.booleanLiteral(¢) && az.booleanLiteral(¢.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override @NotNull public String description(final ForStatement ¢) {
    return "Convert the return inside " + ¢ + " to break";
  }

  @Override public boolean prerequisite(@Nullable final ForStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && !isInfiniteLoop(¢);
  }

  @Override public Tip tip(@NotNull final ForStatement s, @Nullable final ExclusionManager exclude) {
    @Nullable final ReturnStatement nextReturn = extract.nextReturn(s);
    if (nextReturn == null || isInfiniteLoop(s))
      return null;
    @NotNull final Statement body = body(s), $ = iz.returnStatement(body) && compareReturnStatements(nextReturn, az.returnStatement(body)) ? body
        : iz.block(body) ? handleBlock((Block) body, nextReturn) : iz.ifStatement(body) ? handleIf(body, nextReturn) : null;
    if (exclude != null)
      exclude.exclude(s);
    return $ == null ? null : new Tip(description(), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace($, first(statements(az.block(into.s("break;")))), g);
      }
    };
  }
}

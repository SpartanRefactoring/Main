package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

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
public final class ReturnToBreakFiniteWhile extends CarefulTipper<WhileStatement>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -0x70481BF1FE1E5DFBL;

  private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(b)) {
      if (az.ifStatement(¢) != null)
        $ = handleIf(¢, nextReturn);
      if (wizard.eq(nextReturn, az.returnStatement(¢).getExpression()))
        return ¢;
    }
    return $;
  }

  private static Statement handleIf(final IfStatement s, final ReturnStatement nextReturn) {
    return s == null ? null : handleIf(then(s), elze(s), nextReturn);
  }

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    return handleIf(az.ifStatement(s), nextReturn);
  }

  private static Statement handleIf(final Statement then, final Statement elze, final ReturnStatement nextReturn) {
    if (then == null)
      return null;
    if (wizard.eq(az.returnStatement(then), nextReturn.getExpression()))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock((Block) then, nextReturn);
      if ($ != null)
        return $;
    }
    if (az.ifStatement(then) != null)
      return handleIf(then, nextReturn);
    if (elze == null)
      return null;
    if (wizard.eq(az.returnStatement(elze), nextReturn.getExpression()))
      return elze;
    if (!iz.block(elze))
      return az.ifStatement(elze) == null ? null : handleIf(elze, nextReturn);
    final Statement $ = handleBlock((Block) elze, nextReturn);
    return $ != null ? $ : az.ifStatement(elze) == null ? null : handleIf(elze, nextReturn);
  }

  private static boolean isInfiniteLoop(final WhileStatement ¢) {
    return az.booleanLiteral(¢.getExpression()) != null && az.booleanLiteral(¢.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override public String description(final WhileStatement ¢) {
    return "Convert the return inside " + ¢ + " to break";
  }

  @Override public boolean prerequisite(final WhileStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && !isInfiniteLoop(¢);
  }

  @Override public Tip tip(final WhileStatement s) {
    final ReturnStatement nextReturn = extract.nextReturn(s);
    if (s == null || isInfiniteLoop(s) || nextReturn == null)
      return null;
    final Statement body = body(s), $ = iz.returnStatement(body) && wizard.eq(nextReturn, az.returnStatement(body).getExpression()) ? body
        : iz.block(body) ? handleBlock(az.block(body), nextReturn) : az.ifStatement(body) == null ? null : handleIf(body, nextReturn);
    return $ == null ? null : new Tip(description(), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, az.astNode(first(statements(az.block(into.s("break;"))))), g);
      }
    }.spanning(nextReturn);
  }
}

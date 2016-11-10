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

/** Convert Finite loops with return sideEffects to shorter ones : </br>
 * Convert <br/>
 * <code>
 * for (..) { <br/>
 *  does(something); <br/>
 *   return XX; <br/>
 * } <br/>
 *return XX; <br/>
 * </code> to : <br/>
 * <code>
 * for (..) { <br/>
 *  does(something); <br/>
 *   break; <br/>
 * } <br/>
 *return XX; <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public final class ReturnToBreakFiniteWhile extends CarefulTipper<WhileStatement> implements TipperCategory.Collapse {
  private static boolean compareReturnStatements(final ReturnStatement r1, final ReturnStatement r2) {
    return r1 != null && r2 != null && (r1.getExpression() + "").equals(r2.getExpression() + "");
  }
  private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(b)) {
      if (az.ifStatement(¢) != null)
        $ = handleIf(¢, nextReturn);
      if (compareReturnStatements(nextReturn, az.returnStatement(¢)))
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
  private static boolean isInfiniteLoop(final WhileStatement ¢) {
    return az.booleanLiteral(¢.getExpression()) != null && az.booleanLiteral(¢.getExpression()).booleanValue();
  }
  @Override public String description() {
    return "Convert the return inside the loop to break";
  }
  @Override public String description(final WhileStatement b) {
    return "Convert the return inside " + b + " to break";
  }
  @Override public boolean prerequisite(final WhileStatement ¢) {
    return ¢ != null && extract.nextReturn(¢) != null && !isInfiniteLoop(¢);
  }
  @Override public Tip tip(final WhileStatement b, final ExclusionManager exclude) {
    final ReturnStatement nextReturn = extract.nextReturn(b);
    if (b == null || isInfiniteLoop(b) || nextReturn == null)
      return null;
    final Statement body = b.getBody();
    final Statement $ = iz.returnStatement(body) && compareReturnStatements(nextReturn, az.returnStatement(body)) ? body
        : iz.block(body) ? handleBlock(az.block(body), nextReturn) : az.ifStatement(body) == null ? null : handleIf(body, nextReturn);
    if (exclude != null)
      exclude.exclude(b);
    return $ == null ? null : new Tip(description(), b, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, az.astNode(az.block(into.s("break;")).statements().get(0)), g);
      }
    };
  }
}

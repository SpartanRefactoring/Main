package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert loops with return statements to shorter ones : </br>
 * Convert <br/>
 * <code>
 * for (..) { <br/>
 *  asdfasdfasdf <br/>
 *   return XXX; <br/>
 * } <br/>
 *return XXX; <br/>
 * </code> to : <br/>
 * <code>
 * for (..) { <br/>
 *  asdfasdfasdf <br/>
 *   break; <br/>
 * } <br/>
 *return XXX; <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public class ReturnToBreakFiniteFor extends Wring<Block> implements Kind.Canonicalization {
  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override String description(final Block b) {
    return "Convert the return inside " + b + " to break";
  }

  private static boolean isInfiniteLoop(final ForStatement s) {
    return az.booleanLiteral(s.getExpression()) != null;
  }

  private static boolean compareReturnStatements(final ReturnStatement r1, final ReturnStatement r2) {
    return r1 != null && r2 != null && (r1.getExpression() + "").equals(r2.getExpression() + "");
  }

  // TODO: Dor Ma'ayan this needs spartanization
  @SuppressWarnings("all") @Override Rewrite make(final Block n) {
    final List<Statement> statementList = n.statements();
    final ForStatement forStatement = (ForStatement) statementList.get(0);
    final ReturnStatement nextReturn = (ReturnStatement) statementList.get(1);
    if (isInfiniteLoop(forStatement))
      return null;
    final Statement body = forStatement.getBody();
    Statement toChange = az.ifStatement(body) == null ? null : handleIf(body, nextReturn);
    if (iz.block(body)) {
      final List<Statement> blockStatements = ((Block) body).statements();
      for (final Statement s : blockStatements) {
        if (az.ifStatement(s) != null)
          toChange = handleIf(s, nextReturn);
        if (compareReturnStatements(nextReturn, az.returnStatement(s))) {
          toChange = s;
          break;
        }
      }
    }
    if (iz.returnStatement(body) && //
        compareReturnStatements(nextReturn, az.returnStatement(body)))
      toChange = body;
    if (toChange == null)
      return null;
    final Statement theChange = toChange;
    return new Rewrite(description(), theChange) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(theChange, (ASTNode) ((Block) into.s("break;")).statements().get(0), g);
      }
    };
  }

  // TODO Dor: I sprartanized a bit only to discover that this looks buggy! how
  // can you assume it is an if without checking for it, and if you checked
  // already, why do you check again?
  private Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    // TODO: Dor Ma'ayan this needs spartanization
    final Statement $ = az.ifStatement(s).getThenStatement();
    final Statement elze = az.ifStatement(s).getElseStatement();
    if (az.ifStatement($) != null)
      return handleIf($, nextReturn);
    if (az.ifStatement(elze) != null)
      return handleIf(elze, nextReturn);
    if (compareReturnStatements(nextReturn, az.returnStatement($)))
      return $;
    if (compareReturnStatements(nextReturn, az.returnStatement(elze)))
      return elze;
    return handleIf(nextReturn, az.block($));
  }

  private Statement handleIf(final ReturnStatement nextReturn, final Block b) {
    // TODO: Dor Ma'ayan this needs spartanization
    if (b == null)
      return null;
    for (final Statement $ : step.statements(b)) {
      // TODO: DOr, why the same test twice?
      if (az.ifStatement($) != null || az.ifStatement($) != null)
        return handleIf($, nextReturn);
      if (compareReturnStatements(nextReturn, az.returnStatement($)))
        return $;
    }
    return null;
  }

  @Override boolean scopeIncludes(final Block b) {
    // TODO: Niv: Use lisp.first and lisp.second, in fact, if second returns
    // null, you do not have to do anything.
    final List<Statement> ss = step.statements(b);
    return ss.size() > 1 && ss.get(0) instanceof ForStatement && ss.get(1) instanceof ReturnStatement;
  }
}

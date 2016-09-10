package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert Finite loops with return statements to shorter ones : </br>
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
public class ReturnToBreakFiniteWhile extends Wring<Block> implements Kind.Canonicalization {
  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override String description(final Block b) {
    return "Convert the return inside " + b + " to break";
  }

  private static boolean isInfiniteLoop(final WhileStatement s) {
    return az.booleanLiteral(s.getExpression()) != null && az.booleanLiteral(s.getExpression()).booleanValue();
  }

  private static boolean compareReturnStatements(final ReturnStatement r1, final ReturnStatement r2) {
    return r1 != null && r2 != null && (r1.getExpression() + "").equals(r2.getExpression() + "");
  }

  @SuppressWarnings("all") @Override Rewrite make(final Block n) {
    final List<Statement> statementList = n.statements();
    if (statementList.size() < 2 || !(statementList.get(0) instanceof WhileStatement) //
        || !(statementList.get(1) instanceof ReturnStatement))
      return null;
    final WhileStatement whileStatement = (WhileStatement) statementList.get(0);
    final ReturnStatement nextReturn = (ReturnStatement) statementList.get(1);
    if (isInfiniteLoop(whileStatement))
      return null;
    final Statement body = whileStatement.getBody();
    final Statement toChange = iz.returnStatement(body) && compareReturnStatements(nextReturn, az.returnStatement(body)) ? body
        : iz.block(body) ? handleBlock((Block) body, nextReturn) : az.ifStatement(body) == null ? null : handleIf(body, nextReturn);
    final Statement change = toChange;
    return toChange == null ? null : new Rewrite(description(), change) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(change, (ASTNode) ((Block) into.s("break;")).statements().get(0), g);
      }
    };
  }

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    final IfStatement ifStatement = az.ifStatement(s);
    if (ifStatement == null)
      return null;
    final Statement thenStatement = ifStatement.getThenStatement();
    final Statement elzeStatement = ifStatement.getElseStatement();
    if (thenStatement != null) {
      if (compareReturnStatements(az.returnStatement(thenStatement), nextReturn))
        return thenStatement;
      if (iz.block(thenStatement)) {
        final Statement $ = handleBlock((Block) thenStatement, nextReturn);
        if ($ != null)
          return $;
      }
      if (az.ifStatement(thenStatement) != null)
        return handleIf(thenStatement, nextReturn);
      if (elzeStatement != null) {
        if (compareReturnStatements(az.returnStatement(elzeStatement), nextReturn))
          return elzeStatement;
        if (iz.block(elzeStatement)) {
          final Statement $ = handleBlock((Block) elzeStatement, nextReturn);
          if ($ != null)
            return $;
        }
        if (az.ifStatement(elzeStatement) != null)
          return handleIf(elzeStatement, nextReturn);
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked") private static Statement handleBlock(final Block body, final ReturnStatement nextReturn) {
    Statement $ = null;
    final List<Statement> blockStatements = body.statements();
    for (final Statement s : blockStatements) {
      if (az.ifStatement(s) != null)
        $ = handleIf(s, nextReturn);
      if (compareReturnStatements(nextReturn, az.returnStatement(s))) {
        $ = s;
        break;
      }
    }
    return $;
  }
}

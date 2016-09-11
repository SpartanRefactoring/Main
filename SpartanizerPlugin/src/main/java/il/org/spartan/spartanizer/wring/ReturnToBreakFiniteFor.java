package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert Finite loops with return statements to shorter ones : </br>
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
public class ReturnToBreakFiniteFor extends Wring<Block> implements Kind.Canonicalization {
  private static boolean compareReturnStatements(final ReturnStatement r1, final ReturnStatement r2) {
    return r1 != null && r2 != null && (r1.getExpression() + "").equals(r2.getExpression() + "");
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

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    final IfStatement ifStatement = az.ifStatement(s);
    if (ifStatement == null)
      return null;
    final Statement then = ifStatement.getThenStatement();
    final Statement elze = ifStatement.getElseStatement();
    if (then != null) {
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
    }
    return null;
  }

  private static boolean isInfiniteLoop(final ForStatement s) {
    return az.booleanLiteral(s.getExpression()) != null && az.booleanLiteral(s.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override String description(final Block b) {
    return "Convert the return inside " + b + " to break";
  }

  // TODO: Niv, fully spartanize this one. Remove @SuppressWarnings("all"), use
  // az.forstatement, iz.forstatemnt, step.statements(etc), etc.
  @SuppressWarnings("all") @Override Rewrite make(final Block n) {
    final List<Statement> ss = n.statements();
    if (ss.size() < 2 || !(first(ss) instanceof ForStatement) //
        || !(second(ss) instanceof ReturnStatement))
      return null;
    final ForStatement forStatement = (ForStatement) first(ss);
    final ReturnStatement nextReturn = (ReturnStatement) second(ss);
    if (isInfiniteLoop(forStatement))
      return null;
    final Statement body = forStatement.getBody();
    final Statement $ = iz.returnStatement(body) && compareReturnStatements(nextReturn, az.returnStatement(body)) ? body
        : iz.block(body) ? handleBlock((Block) body, nextReturn) : az.ifStatement(body) == null ? null : handleIf(body, nextReturn);
    return $ == null ? null : new Rewrite(description(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, (ASTNode) ((Block) into.s("break;")).statements().get(0), g);
      }
    };
  }

  @Override boolean scopeIncludes(final Block b) {
    // TODO: Niv: Use lisp.first and lisp.second, in fact, if second returns
    // null, you do not have to do anything.
    final List<Statement> ss = step.statements(b);
    return ss.size() > 1 && lisp.first(ss) instanceof ForStatement && lisp.second(ss) instanceof ReturnStatement;
  }
}

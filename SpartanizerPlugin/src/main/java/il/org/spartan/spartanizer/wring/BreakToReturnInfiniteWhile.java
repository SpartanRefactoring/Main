package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Convert Infinite loops with return statements to shorter ones : </br>
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
public class BreakToReturnInfiniteWhile extends Wring<Block> implements Kind.Canonicalization {
  @Override public String description() {
    return "Convert the break inside the loop to return";
  }

  @Override String description(final Block b) {
    return "Convert the break inside " + b + " to return";
  }

  private static boolean isInfiniteLoop(final WhileStatement s) {
    return az.booleanLiteral(s.getExpression()) != null && az.booleanLiteral(s.getExpression()).booleanValue();
  }

  // TODO: DOR, Do not suppress all warnings, spartanize.
  @SuppressWarnings("all") @Override Rewrite make(final Block n) {
    final List<Statement> ss = n.statements();
    if (ss.size() < 2 || !(ss.get(0) instanceof WhileStatement) //
        || !(ss.get(1) instanceof ReturnStatement))
      return null;
    final WhileStatement whileStatement = (WhileStatement) ss.get(0);
    final ReturnStatement nextReturn = (ReturnStatement) ss.get(1);
    if (!isInfiniteLoop(whileStatement))
      return null;
    final Statement body = whileStatement.getBody();
    final Statement $ = az.ifStatement(body) != null ? handleIf(body, nextReturn)
        : iz.block(body) ? handleBlock((Block) body, nextReturn) : body instanceof BreakStatement ? body : null;
    return $ == null ? null : new Rewrite(description(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        r.remove(nextReturn, g);
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
      if (thenStatement instanceof BreakStatement)
        return thenStatement;
      if (iz.block(thenStatement)) {
        final Statement $ = handleBlock((Block) thenStatement, nextReturn);
        if ($ != null)
          return $;
      }
      if (az.ifStatement(thenStatement) != null)
        return handleIf(thenStatement, nextReturn);
      if (elzeStatement != null) {
        if (elzeStatement instanceof BreakStatement)
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
      if (s instanceof BreakStatement) {
        $ = s;
        break;
      }
    }
    return $;
  }
}

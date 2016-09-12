package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;

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
public class BlockBreakToReturnInfiniteWhile extends Wring<Block> implements Kind.Collapse {
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

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    final IfStatement ifStatement = az.ifStatement(s);
    if (ifStatement == null)
      return null;
    final Statement then = ifStatement.getThenStatement();
    final Statement elze = ifStatement.getElseStatement();
    if (then != null) {
      if (then instanceof BreakStatement)
        return then;
      if (iz.block(then)) {
        final Statement $ = handleBlock((Block) then, nextReturn);
        if ($ != null)
          return $;
      }
      if (az.ifStatement(then) != null)
        return handleIf(then, nextReturn);
      if (elze != null) {
        if (elze instanceof BreakStatement)
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

  private static boolean isInfiniteLoop(final WhileStatement s) {
    return az.booleanLiteral(s.getExpression()) != null && az.booleanLiteral(s.getExpression()).booleanValue();
  }

  @Override public String description() {
    return "Convert the break inside the loop to return";
  }

  @Override String description(final Block b) {
    return "Convert the break inside " + b + " to return";
  }

  @Override Rewrite make(final Block b) {
    // TODO: Niv. To avoid warning, use step.statements(n)
    final List<Statement> ss = b.statements();
    // TODO: Dor, use iz.returnStatement, etc. If no such function, create one.
    if (ss.size() < 2 || !(first(ss) instanceof WhileStatement) //
        || !(second(ss) instanceof ReturnStatement))
      return null;
    // TODO: Niv, Ditto
    final WhileStatement whileStatement = (WhileStatement) first(ss);
    final ReturnStatement nextReturn = (ReturnStatement) second(ss);
    if (!isInfiniteLoop(whileStatement))
      return null;
    final Statement body = whileStatement.getBody();
    // TODO: Niv, instead of using az.x(y) == null, use iz.x(y)
    final Statement $ = az.ifStatement(body) != null ? handleIf(body, nextReturn)
        : iz.block(body) ? handleBlock((Block) body, nextReturn) : body instanceof BreakStatement ? body : null;
    return $ == null ? null : new Rewrite(description(b), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        r.remove(nextReturn, g);
      }
    };
  }
}

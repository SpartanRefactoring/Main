package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;
import java.util.*;
import static il.org.spartan.lisp.*;

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
 *return XXX; <br/>
 * </code> to : <br/>
 * <code> while (true) { <br/>
 * doSomething(); <br/>
 * if(done()) <br/>
 * return XXX; <br/>
 * } <br/>
 * @author Dor Ma'ayan
 * @since 2016-09-09 */
public class BreakToReturnInfiniteFor extends Wring<Block> implements Kind.Canonicalization {
  @Override public String description() {
    return "Convert the break inside the loop to return";
  }

  @Override String description(final Block b) {
    return "Convert the break inside " + b + " to return";
  }

  private static boolean isInfiniteLoop(final ForStatement s) {
    return az.booleanLiteral(s.getExpression()) != null && az.booleanLiteral(s.getExpression()).booleanValue();
  }

  // TODO: Dor, there are functions in extract that do much of this
  // TODO: Dor, use lisp.first and lisp.second
  // I will spartnize this for you. Implement in other classes
  @Override Rewrite make(final Block n) {
    return make(statements(n));
  }

  public Rewrite make(final List<Statement> ss) {
    final ForStatement vor = az.forStatement(first(ss));
    if (vor == null || !isInfiniteLoop(vor))
      return null;
    final ReturnStatement nextReturn = az.returnStatement(second(ss));
    return nextReturn == null ? null : make(vor, nextReturn);
  }

  public Rewrite make(final ForStatement vor, final ReturnStatement nextReturn) {
    final Statement $ = make(vor.getBody(), nextReturn);
    return $ == null ? null : new Rewrite(description(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace($, nextReturn, g);
        r.remove(nextReturn, g);
      }
    };
  }

  public static Statement make(final Statement body, final ReturnStatement nextReturn) {
    return az.ifStatement(body) != null ? handleIf(body, nextReturn)
        : iz.block(body) ? handleBlock((Block) body, nextReturn) : body instanceof BreakStatement ? body : null;
  }

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    return handleIf(az.ifStatement(s), nextReturn);
  }

  public static Statement handleIf(final IfStatement s, final ReturnStatement nextReturn) {
    return s == null ? null : handleIf(then(s), elze(s), nextReturn);
  }

  public static Statement handleIf(final Statement then, final Statement elze, final ReturnStatement nextReturn) {
    // TODO: Dor, I think you can both cases then/elze with one statement
    if (then == null)
      return null;
    if (iz.breakStatement(then))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock(az.block(then), nextReturn);
      if ($ != null)
        return $;
    }
    if (iz.ifStatement(then))
      return handleIf(then, nextReturn);
    if (elze == null)
      return null;
    if (iz.breakStatement(elze))
      return elze;
    if (iz.block(elze)) {
      final Statement $ = handleBlock(az.block(elze), nextReturn);
      if ($ != null)
        return $;
    }
    return iz.ifStatement(elze) ? null : handleIf(elze, nextReturn);
  }

  private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement s : statements(b)) {
      if (iz.ifStatement(s))
        $ = handleIf(az.ifStatement(s), nextReturn);
      if (iz.breakStatement(s))
        return s;
    }
    return $;
  }
}

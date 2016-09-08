package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.utils.lisp.*;
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
    return "Convert the Return inside the loop to break";
  }

  @Override String description(Block n) {
    return "Convert the Return inside the loop to break";
  }

  private static boolean isInfiniteLoop(ForStatement n) {
    if (az.booleanLiteral(n.getExpression()) == null)
      return false;
    return true;
  }

  // TODO: Niv: there is a function in wizard that tells you if two nodes are
  // the same. Check it out
  private static boolean compareReturnStatements(ReturnStatement r1, ReturnStatement r2) {
    return r1 != null && r2 != null && r1.getExpression().toString().equals(r2.getExpression().toString());
  }

  @Override Rewrite make(Block b) {
    List<Statement> ss = step.statements(b);
    ForStatement forStatement = (ForStatement) first(ss);
    ReturnStatement nextReturn = (ReturnStatement) second(ss);
    return isInfiniteLoop(forStatement) ? null : go(nextReturn, az.block(forStatement.getBody()));
  }

  public Rewrite go(ReturnStatement nextReturn, Block b) {
    if (b == null)
      return null;
    if (iz.block(b))
      for (Statement s : step.statements(b))
        if (compareReturnStatements(nextReturn, az.returnStatement(s)))
          return new Rewrite(description(), s) {
            @Override public void go(final ASTRewrite r, final TextEditGroup g) {
              r.replace(s, (ASTNode) ((Block) into.s("break;")).statements().get(0), g);
            }
          };
    return null;
  }

  @Override boolean scopeIncludes(final Block b) {
    List<Statement> ss = step.statements(b);
    return b != null && ss.size() > 1 && ss.get(0) instanceof ForStatement && ss.get(1) instanceof ReturnStatement; //
  }
}

package il.org.spartan.spartanizer.wrings;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Convert <code>if(a){f();return;}g();</code> into
 * <code>if(a){f();return;}g();</code> f(); } </code> provided that this
 * <code><b>if</b></code> statement is the last statement in a method.
 * @author Yossi Gil
 * @since 2016 */
public final class IfPenultimateInMethodFollowedBySingleStatement extends ReplaceToNextStatement<IfStatement> implements Kind.EarlyReturn {
  static <T> void removeLast(final List<T> ts) {
    ts.remove(ts.size() - 1);
  }

  @Override public String description(final IfStatement s) {
    return "Convert return into else in " + s;
  }

  public void remove(final ASTRewrite r, final Statement s, final TextEditGroup g) {
    final ListRewrite lr = r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY);
    lr.remove(s, g);
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final IfStatement ifStatement, final Statement nextStatement, final TextEditGroup g) {
    if (elze(ifStatement) != null)
      return null;
    if (!iz.lastInMethod(nextStatement))
      return null;
    final Statement then = then(ifStatement);
    final ReturnStatement deleteMe = az.returnStatement(hop.lastStatement(then));
    if (deleteMe == null || deleteMe.getExpression() != null)
      return null;
    r.replace(deleteMe, make.emptyStatement(deleteMe), g);
    remove(r, nextStatement, g);
    final IfStatement newIf = duplicate.of(ifStatement);
    final Block block = az.block(then(newIf));
    if (block != null)
      removeLast(step.statements(block));
    else
      newIf.setThenStatement(make.emptyStatement(newIf));
    newIf.setElseStatement(duplicate.of(nextStatement));
    r.replace(ifStatement, newIf, g);
    remove(r, nextStatement, g);
    return r;
  }
}

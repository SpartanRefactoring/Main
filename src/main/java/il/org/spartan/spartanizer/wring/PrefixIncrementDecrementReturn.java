package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** convert
 *
 * <pre>
 * a = 3;
 * return a;
 * </pre>
 *
 * to
 *
 * <pre>
 * return a = 3;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class PrefixIncrementDecrementReturn extends Wring.ReplaceToNextStatement<PrefixExpression> implements Kind.Collapse {
  @Override String description(final PrefixExpression x) {
    return "Consolidate " + x + " with subsequent 'return' of " + step.operand(x);
  }

  @Override ASTRewrite go(final ASTRewrite r, final PrefixExpression x, final Statement nextStatement, final TextEditGroup g) {
    if (!in(x.getOperator(), INCREMENT, DECREMENT))
      return null;
    final Statement parent = az.asStatement(x.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(step.operand(x), step.expression(s)))
      return null;
    r.remove(parent, g);
    r.replace(s, subject.operand(x).toReturn(), g);
    return r;
  }
}

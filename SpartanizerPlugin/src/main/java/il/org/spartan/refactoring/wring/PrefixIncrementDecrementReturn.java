package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
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
public class PrefixIncrementDecrementReturn extends Wring.ReplaceToNextStatement<PrefixExpression> implements Kind.Canonicalization {
  @Override String description(final PrefixExpression e) {
    return "Consolidate " + e + " with subsequent 'return' of " + operand(e);
  }
  @Override ASTRewrite go(final ASTRewrite r, final PrefixExpression e, final Statement nextStatement, final TextEditGroup g) {
    if (!in(e.getOperator(), INCREMENT, DECREMENT))
      return null;
    final Statement parent = asStatement(e.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null || !same(operand(e), expression(s)))
      return null;
    r.remove(parent, g);
    r.replace(s, subject.operand(e).toReturn(), g);
    return r;
  }
}

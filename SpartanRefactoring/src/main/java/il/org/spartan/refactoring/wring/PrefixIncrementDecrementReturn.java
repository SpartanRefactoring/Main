package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A {@link Wring} to convert <code>a = 3;return a;</code> to
 * <code>return a = 3;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public class PrefixIncrementDecrementReturn extends Wring.ReplaceToNextStatement<PrefixExpression> implements
    Kind.ConsolidateStatements {
  @Override ASTRewrite go(final ASTRewrite r, final PrefixExpression e, final Statement nextStatement,
      @SuppressWarnings("unused") final TextEditGroup __) {
    if (!in(e.getOperator(), INCREMENT, DECREMENT))
      return null;
    final Statement parent = asStatement(e.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null || !same(e.getOperand(), core(s.getExpression())))
      return null;
    scalpel.operate(nextStatement, parent).replaceWith(Subject.operand(e).toReturn());
    return r;
  }
  @Override String description(final PrefixExpression e) {
    return "Consolidate " + e + " with subsequent 'return' of " + e.getOperand();
  }
}

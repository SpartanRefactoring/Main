package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.asReturnStatement;
import static il.org.spartan.refactoring.utils.Funcs.asStatement;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.utils.Utils.in;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.DECREMENT;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.INCREMENT;

import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>a = 3;return a;</code> to
 * <code>return a = 3;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public class PrefixIncrementDecrementReturn extends Wring.ReplaceToNextStatement<PrefixExpression> {
  @Override ASTRewrite go(final ASTRewrite r, final PrefixExpression e, final Statement nextStatement, final TextEditGroup g) {
    if (!in(e.getOperator(), INCREMENT, DECREMENT))
      return null;
    final Statement parent = asStatement(e.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = asReturnStatement(nextStatement);
    if (s == null || !same(e.getOperand(), core(s.getExpression())))
      return null;
    r.remove(parent, g);
    r.replace(s, Subject.operand(e).toReturn(), g);
    return r;
  }
  @Override String description(final PrefixExpression e) {
    return "Consolidate " + e + " with subsequent 'return' of " + e.getOperand();
  }
  @Override WringGroup wringGroup() {
	return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}

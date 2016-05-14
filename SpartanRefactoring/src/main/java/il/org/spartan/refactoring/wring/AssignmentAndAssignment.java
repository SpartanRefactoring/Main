package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.right;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static org.eclipse.jdt.core.dom.ASTNode.NULL_LITERAL;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.utils.Is;

/**
 * A {@link Wring} to convert <code>a = 3; b = 3;</code> to
 * <code>a = b = 3</code>
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public class AssignmentAndAssignment extends Wring.ReplaceToNextStatement<Assignment> {
  @SuppressWarnings("unused") @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement,
      final TextEditGroup g) {
    final ASTNode parent = a.getParent();
    if (!(parent instanceof Statement))
      return null;
    final Expression right = getRight(a);
    if (right == null || right.getNodeType() == NULL_LITERAL)
      return null;
    final Assignment a1 = Extract.assignment(nextStatement);
    if (a1 == null)
      return null;
    final Expression right1 = getRight(a1);
    if (right1 == null || !same(right, right1) || !Is.deterministic(right))
      return null;
    scalpel.operate(nextStatement, parent);
    final Assignment $ = Funcs.duplicate(a1);
    setRight(a1, Funcs.duplicate(a));
    scalpel.replaceWith(r.getAST().newExpressionStatement($));
    return r;
  }
  static Expression getRight(final Assignment a) {
    return a.getOperator() != ASSIGN ? null : extractRight(a);
  }
  static Expression extractRight(final Assignment a) {
    final Expression $ = Extract.core(right(a));
    return !($ instanceof Assignment) || ((Assignment) $).getOperator() != ASSIGN ? $ : extractRight((Assignment) $);
  }
  void setRight(Assignment a, Expression e) {
    final Expression $ = Extract.core(right(a));
    if (!($ instanceof Assignment) || ((Assignment) $).getOperator() != ASSIGN)
      a.setRightHandSide(e);
    else
      setRight((Assignment) $, e);
  }
  @Override String description(final Assignment a) {
    return "Consolidate assignment to " + left(a) + " with subsequent similar assignment";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}

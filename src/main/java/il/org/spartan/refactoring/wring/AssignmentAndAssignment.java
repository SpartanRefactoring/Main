package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>a = 3; b = 3;</code> to <code>a = b =
 * 3</code>
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public class AssignmentAndAssignment extends Wring.ReplaceToNextStatement<Assignment> implements Kind.ConsolidateStatements {
  @SuppressWarnings("unused") @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final ASTNode parent = a.getParent();
    if (!(parent instanceof Statement))
      return null;
    final Expression right = getRight(a);
    if (right == null || right.getNodeType() == NULL_LITERAL)
      return null;
    final Assignment a1 = extract.assignment(nextStatement);
    if (a1 == null)
      return null;
    final Expression right1 = getRight(a1);
    if (right1 == null || !same(right, right1) || !Is.deterministic(right))
      return null;
    scalpel.operate(nextStatement, parent);
    final Assignment $ = Funcs.duplicate(a1);
    setRight($, Funcs.duplicate(a));
    scalpel.replaceWith(r.getAST().newExpressionStatement($));
    return r;
  }
  static Expression getRight(final Assignment a) {
    return a.getOperator() != ASSIGN ? null : extractRight(a);
  }
  static Expression extractRight(final Assignment a) {
    final Expression $ = extract.core(right(a));
    return !($ instanceof Assignment) || ((Assignment) $).getOperator() != ASSIGN ? $ : extractRight((Assignment) $);
  }
  void setRight(final Assignment a, final Expression e) {
    final Expression $ = extract.core(right(a));
    if (!($ instanceof Assignment) || ((Assignment) $).getOperator() != ASSIGN)
      a.setRightHandSide(e);
    else
      setRight((Assignment) $, e);
  }
  @Override String description(final Assignment a) {
    return "Consolidate two assignment to '" + left(a) + "'";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}

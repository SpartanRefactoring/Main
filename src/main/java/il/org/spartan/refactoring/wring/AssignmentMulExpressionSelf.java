package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.utils.*;

/** convert
 *
 * <pre>
 * a = a + 5;
 * </pre>
 *
 * to
 *
 * <post>
 * a += 5;
 * </post>
 *
 * @author Alex
 * @since 2016 */
public final class AssignmentMulExpressionSelf extends Wring.ReplaceCurrentNode<Assignment> implements Kind.Abbreviation {
  @Override String description(final Assignment a) {
    return "Abbreviate " + a + " as x = x*y to x *= y";
  }
  @Override ASTNode replacement(Assignment a) {
    if (!iz.isOpAssign(a) ||
        az.infixExpression(a.getRightHandSide()).getOperator() != Operator.TIMES) return null;
    if (a.getLeftHandSide() == az.infixExpression(a.getRightHandSide()).getLeftOperand()) return leftSame(a);
    else if ((a.getLeftHandSide() == az.infixExpression(a.getRightHandSide()).getRightOperand())) return rightSame(a);
    return null;
  }
  static ASTNode leftSame(Assignment $) {
    $.setOperator(Assignment.Operator.TIMES_ASSIGN);
    $.setRightHandSide(az.infixExpression($.getRightHandSide()).getRightOperand());
    return $;
  }
  static ASTNode rightSame(Assignment $) {
    $.setOperator(Assignment.Operator.TIMES_ASSIGN);
    $.setRightHandSide(az.infixExpression($.getRightHandSide()).getLeftOperand());
    return $;
  }
  
  
/*
  @Override ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(step.left(a), core(s.getExpression())))
      return null;
    r.remove(parent, g);
    r.replace(s, subject.operand(a).toReturn(), g);
    return r;
  }
  */
}

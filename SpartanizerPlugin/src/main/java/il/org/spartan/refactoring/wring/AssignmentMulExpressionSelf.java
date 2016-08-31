package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.utils.subject.*;

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
    Expression right = right(a);
    assert right != null;
    Expression left = left(a);
    assert left != null;
    Expression leftOperand = left(az.infixExpression(right));
    if (leftOperand == null)
      return null;
    Expression rightOperand = az.infixExpression(right).getRightOperand();
    if (rightOperand == null)
      return null;
    return !iz.isOpAssign(a) || az.infixExpression(right).getOperator() != Operator.TIMES ? null
        : left == leftOperand ? leftSame(a)
            : left == rightOperand ? rightSame(a) : null;
  }
  
  static Assignment replace(Assignment $) {
    $.setOperator(Assignment.Operator.TIMES_ASSIGN);
    $.setRightHandSide(rebuildInfix(az.infixExpression($.getRightHandSide()), $.getLeftHandSide()));
    return $;
  }
  
  static Expression rebuildInfix(InfixExpression e, Expression left) {
    List<Expression> es = extract.allOperands(e);
    for (final Expression ¢ : es)
      if (¢.toString().equals(left.toString())){
        es.remove(¢);
        break;
      }
    Expression $ = es.size() >= 2 ? subject.operands(es).to(Operator.TIMES) : es.get(0);
    return $;
  }
  
  static boolean areAllOperatorsTIMES(InfixExpression e) {
    List<InfixExpression.Operator> l = extract.allOperators(e);
    for (final InfixExpression.Operator ¢ : l)
      if (¢ != Operator.TIMES)
        return false;
    return true;
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

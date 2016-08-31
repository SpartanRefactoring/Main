package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>x = x * a </code> by <code> x *= a </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentMulSelf extends ReplaceCurrentNode<Assignment> implements Kind.NoImpact {
  @Override String description(final Assignment a) {
    return "Replace x = x * a; to x *= a;";
  }

  @Override ASTNode replacement(final Assignment a) {
    //InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    return !iz.isOpAssign(a) || isNotRightMul(a) ? null : replace(a);
  }
  
  private static boolean isNotRightMul(Assignment a) {
    return az.infixExpression(a.getRightHandSide()).getOperator() == TIMES;
  }
  
  private static ASTNode replace(final Assignment a) {
    InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    Expression e = (az.expression(rightInfixReplacement(extract.allOperands(¢), a.getLeftHandSide())));
    ASTNode $ = e == null ? null : subject.pair(a.getLeftHandSide(), e).to(Operator.TIMES_ASSIGN);
    return $;
  }

  private static ASTNode rightInfixReplacement(final List<Expression> es, Expression left) {
    final List<Expression> $ = new ArrayList<>(es);
    for (final Expression ¢ : es)
      if (asLeft(¢, left)) {
        $.remove(¢);
        break;
      }
    assert(es.size() >= 2);
    assert($.size() >= 1);
    return $.size() == es.size() ? null : $.size() == 1 ? wizard.duplicate(lisp.first($)) : subject.operands($).to(TIMES);
  }
  
  private static boolean asLeft(final Expression ¢, final Expression left) {
    //return ¢.equals(left);
    return ¢.toString().equals(left.toString());
  }
}



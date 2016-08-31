package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>x = x + a </code> by <code> x += a </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentPlusSelf extends ReplaceCurrentNode<Assignment> implements Kind.NoImpact {
  @Override String description(final Assignment a) {
    return "Replace x = x + a; to x += a;";
  }

  @Override ASTNode replacement(Assignment a) {
    InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    return !iz.isOpAssign(a) || !iz.infixPlus(¢) ? null : replace(a);
  }
  
  private static ASTNode replace(Assignment a) {
    InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    a.setOperator(Operator.PLUS_ASSIGN);
    a.setRightHandSide(az.expression(rightInfixReplacement(extract.allOperands(¢), a.getLeftHandSide())));
    return a;
  }

  private static ASTNode rightInfixReplacement(final List<Expression> es, Expression left) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
      if (isNotAsLeft(¢, left))
        $.add(¢);
    return $.size() == es.size() ? null
        : $.isEmpty() ? wizard.duplicate(lisp.first(es)) : $.size() == 1 ? wizard.duplicate(lisp.first($)) : subject.operands($).to(PLUS);
  }
  
  private static boolean isNotAsLeft(Expression ¢, Expression left) {
    //return ¢.equals(left);
    return !¢.toString().equals(left.toString());
  }
}



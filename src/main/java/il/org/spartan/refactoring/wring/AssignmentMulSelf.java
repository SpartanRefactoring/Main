package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;
import il.org.spartan.refactoring.engine.*;
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
    final InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    return !iz.isOpAssign(a) || ¢ == null || !iz.infixTimes(¢) ? null : replace(a);
  }

  // private static boolean isNotRightMul(final Assignment a) {
  // return az.infixExpression(a.getRightHandSide()).getOperator() == TIMES;
  // }
  private static ASTNode replace(final Assignment a) {
    final InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    final Expression e = az.expression(rightInfixReplacement(extract.allOperands(¢), a.getLeftHandSide()));
    return e == null ? null : subject.pair(a.getLeftHandSide(), e).to(Operator.TIMES_ASSIGN);
  }

  private static ASTNode rightInfixReplacement(final List<Expression> es, final Expression left) {
    final List<Expression> $ = new ArrayList<>(es);
    for (final Expression ¢ : es)
      if (asLeft(¢, left)) {
        $.remove(¢);
        break;
      }
    assert es.size() >= 2;
    assert $.size() >= 1;
    return $.size() == es.size() ? null : $.size() == 1 ? duplicate.of(lisp.first($)) : subject.operands($).to(TIMES);
  }

  private static boolean asLeft(final Expression ¢, final Expression left) {
    return wizard.same(¢, left);
  }
}

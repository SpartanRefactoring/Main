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

  @Override ASTNode replacement(final Assignment a) {
    final InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    return !iz.isOpAssign(a) || ¢ == null || !iz.infixPlus(¢) ? null : replace(a);
  }

  private static ASTNode replace(final Assignment a) {
    final InfixExpression ¢ = az.infixExpression(a.getRightHandSide());
    final Expression newRightExpr = az.expression(rightInfixReplacement(extract.allOperands(¢), a.getLeftHandSide()));
    return newRightExpr == null ? null : subject.pair(a.getLeftHandSide(), newRightExpr).to(Operator.PLUS_ASSIGN);
  }

  private static ASTNode rightInfixReplacement(final List<Expression> es, final Expression left) {
    final List<Expression> $ = new ArrayList<>(es);
    for (final Expression ¢ : es)
      if (asLeft(¢, left)) {
        $.remove(¢);
        break;
      }
    return $.size() == es.size() ? null : $.size() == 1 ? wizard.duplicate(lisp.first($)) : subject.operands($).to(PLUS);
  }

  private static boolean asLeft(final Expression ¢, final Expression left) {
    return wizard.same(¢, left);
  }
}

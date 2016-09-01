package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;

/** convert
 *
 * <pre>
 * x = x + y;
 * </pre>
 *
 * to
 *
 * <pre>
 * x += y;
 * </pre>
 *
 * @author Dan Greenstein
 * @author Alex Kopzon */
public class AssignmentPlusExpressionSelf extends Wring.ReplaceCurrentNode<Assignment> implements Kind.Abbreviation {
  @Override String description(final Assignment a) {
    return "switch occurences of x=x+y in " + a + " with x+=y";
  }

  @Override ASTNode replacement(final Assignment a) {
    return !iz.isOpAssign(a) || !isInfixPlus(a.getRightHandSide()) ? null : replace(a);
  }

  private static ASTNode replace(final Assignment a) {
    final Expression e = a.getLeftHandSide();
    final List<Expression> $ = extract.allOperands(az.infixExpression(a.getRightHandSide()));
    for (final Expression ¢ : $)
      if (e.toString().equals(¢.toString())) {
        $.remove(¢);
        final Assignment r = wizard.duplicate(a);
        r.setOperator(Assignment.Operator.PLUS_ASSIGN);
        r.setRightHandSide($.size() <= 1 ? $.get(0) : subject.operands($).to(PLUS));
        return r;
      }
    return null;
  }

  private static boolean isInfixPlus(final Expression $) {
    return isInfixPlus(az.infixExpression($));
  }
}

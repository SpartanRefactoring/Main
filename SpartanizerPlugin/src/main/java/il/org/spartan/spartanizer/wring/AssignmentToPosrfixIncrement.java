package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.PostfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
// import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>x += 1 </code> by <code> x++ </code> and also
 * <code>x -= 1 </code> by <code> x-- </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToPosrfixIncrement extends ReplaceCurrentNode<Assignment> implements Kind.SyntacticBaggage {
  private static boolean isIncrement(final Assignment a) {
    return a.getOperator() == Assignment.Operator.PLUS__ASSIGN;
  }

  private static ASTNode replace(final Assignment a) {
    return subject.operand(a.getLeftHandSide()).to(isIncrement(a) ? INCREMENT : DECREMENT);
  }

  @Override String description(final Assignment a) {
    return "Replace " + a + " to " + a.getRightHandSide() + (isIncrement(a) ? "++" : "--");
  }

  @Override ASTNode replacement(final Assignment a) {
    return !iz.isOpPlusAssign(a) && !iz.isOpMinusAssign(a) || !iz.literal1(a.getRightHandSide()) ? null : replace(a);
  }
}

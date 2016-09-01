package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.PostfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
//import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>x += 1 </code> by <code> x++ </code>
 * and also <code>x -= 1 </code> by <code> x-- </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToPosrfixIncrement extends ReplaceCurrentNode<Assignment> implements Kind.SyntacticBaggage {
  @Override String description(final Assignment a) {
    return isIncrement(a) ? "Replace " + a + " to " + a.getRightHandSide() + "++" :
                            "Replace " + a + " to " + a.getRightHandSide() + "--";
  }

  @Override ASTNode replacement(final Assignment a) {
    return (!iz.isOpPlusAssign(a) && !iz.isOpMinusAssign(a)) || !iz.isLiteralOne(a.getRightHandSide()) ? null : replace(a);
  }

  private static ASTNode replace(final Assignment a) {
    return isIncrement(a) ? subject.operand(a.getLeftHandSide()).to(INCREMENT) : subject.operand(a.getLeftHandSide()).to(DECREMENT);
  }
  
  private static boolean isIncrement(final Assignment a) {
    return a.getOperator() == Assignment.Operator.PLUS_ASSIGN;
  }
}

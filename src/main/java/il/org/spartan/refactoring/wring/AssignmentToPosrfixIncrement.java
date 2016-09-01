package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.PostfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
//import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>x += 1 </code> by <code> x++ </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToPosrfixIncrement extends ReplaceCurrentNode<Assignment> implements Kind.SyntacticBaggage {
  @Override String description(final Assignment a) {
    return "Replace " + a + " to " + a.getRightHandSide() + "++";
  }

  @Override ASTNode replacement(final Assignment a) {
    return !iz.isOpPlusAssign(a) || !iz.isLiteralOne(a.getRightHandSide()) ? null : replace(a);
  }

  private static ASTNode replace(final Assignment a) {
    Expression $ = subject.operand(a.getLeftHandSide()).to(INCREMENT);
    return $;
  }
}

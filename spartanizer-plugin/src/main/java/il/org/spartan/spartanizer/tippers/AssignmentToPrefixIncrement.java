package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.DECREMENT;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.INCREMENT;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code x += 1 } by {@code x++ } and also {@code x -= 1 } by
 * {@code x-- } . Test case is {@link Issue107}
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToPrefixIncrement extends ReplaceCurrentNode<Assignment>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x1B42C77125AB41DAL;

  private static boolean isIncrement(final Assignment ¢) {
    return ¢.getOperator() == Assignment.Operator.PLUS_ASSIGN;
  }
  private static boolean provablyNotString(final Assignment ¢) {
    return type.isNotString(subject.pair(left(¢), right(¢)).to(op.assign2infix(¢.getOperator())));
  }
  private static ASTNode replace(final Assignment ¢) {
    return subject.operand(left(¢)).to(isIncrement(¢) ? INCREMENT : DECREMENT);
  }
  @Override public String description(final Assignment ¢) {
    return "Replace " + ¢ + " to " + right(¢) + (isIncrement(¢) ? "++" : "--");
  }
  @Override public ASTNode replacement(final Assignment ¢) {
    return !iz.isPlusAssignment(¢) && !iz.isMinusAssignment(¢) || !iz.literal1(right(¢)) || !provablyNotString(¢) ? null : replace(¢);
  }
}

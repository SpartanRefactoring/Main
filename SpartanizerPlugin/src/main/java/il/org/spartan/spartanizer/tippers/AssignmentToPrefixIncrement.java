package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Replace {@code x += 1 } by {@code x++ } and also {@code x -= 1 } by
 * {@code x-- } . Test case is {@link Issue107}
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToPrefixIncrement extends ReplaceCurrentNode<Assignment>//
    implements TipperCategory.SyntacticBaggage {
  private static boolean isIncrement(@NotNull final Assignment ¢) {
    return ¢.getOperator() == Assignment.Operator.PLUS_ASSIGN;
  }

  private static boolean provablyNotString(@NotNull final Assignment ¢) {
    return type.isNotString(subject.pair(left(¢), right(¢)).to(wizard.assign2infix(¢.getOperator())));
  }

  @NotNull
  private static ASTNode replace(@NotNull final Assignment ¢) {
    return subject.operand(left(¢)).to(isIncrement(¢) ? INCREMENT : DECREMENT);
  }

  @Nullable
  @Override public String description(@NotNull final Assignment ¢) {
    return "Replace " + ¢ + " to " + right(¢) + (isIncrement(¢) ? "++" : "--");
  }

  @Override public ASTNode replacement(@NotNull final Assignment ¢) {
    return !iz.isPlusAssignment(¢) && !iz.isMinusAssignment(¢) || !iz.literal1(right(¢)) || !provablyNotString(¢) ? null : replace(¢);
  }
}

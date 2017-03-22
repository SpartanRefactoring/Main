package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** converts, whenever possible, postfix increment/decrement to prefix
 * increment/decrement
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-7-17 */
public final class PostfixToPrefix extends ReplaceCurrentNode<PostfixExpression>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = 7490692444066432956L;

  @NotNull private static String description(final Operator ¢) {
    return (¢ == PostfixExpression.Operator.DECREMENT ? "de" : "in") + "crement";
  }

  private static PrefixExpression.Operator pre2post(final PostfixExpression.Operator ¢) {
    return ¢ == PostfixExpression.Operator.DECREMENT ? PrefixExpression.Operator.DECREMENT : PrefixExpression.Operator.INCREMENT;
  }

  @NotNull @Override public String description(@NotNull final PostfixExpression ¢) {
    return "Convert post-" + description(¢.getOperator()) + " of " + operand(¢) + " to pre-" + description(¢.getOperator());
  }

  @Override public boolean prerequisite(@NotNull final PostfixExpression ¢) {
    return ¢.getParent().getNodeType() != ASTNode.SWITCH_STATEMENT && !(¢.getParent() instanceof Expression)
        && yieldAncestors.untilNodeType(ASTNode.VARIABLE_DECLARATION_STATEMENT).from(¢) == null
        && yieldAncestors.untilNodeType(ASTNode.SINGLE_VARIABLE_DECLARATION).from(¢) == null
        && yieldAncestors.untilNodeType(ASTNode.VARIABLE_DECLARATION_EXPRESSION).from(¢) == null;
  }

  @Override public PrefixExpression replacement(@NotNull final PostfixExpression ¢) {
    return subject.operand(operand(¢)).to(pre2post(¢.getOperator()));
  }
}

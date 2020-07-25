package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.operand;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression.Operator;
import org.eclipse.jdt.core.dom.PrefixExpression;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** converts, whenever possible, postfix increment/decrement to prefix
 * increment/decrement
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PostfixToPrefix extends ReplaceCurrentNode<PostfixExpression>//
    implements Category.Idiomatic {
  private static final long serialVersionUID = 0x67F449172A4847BCL;

  private static String description(final Operator ¢) {
    return (¢ == PostfixExpression.Operator.DECREMENT ? "de" : "in") + "crement";
  }
  private static PrefixExpression.Operator pre2post(final PostfixExpression.Operator ¢) {
    return ¢ == PostfixExpression.Operator.DECREMENT ? PrefixExpression.Operator.DECREMENT : PrefixExpression.Operator.INCREMENT;
  }
  @Override public String description(final PostfixExpression ¢) {
    return "Convert post-" + description(¢.getOperator()) + " of " + operand(¢) + " to pre-" + description(¢.getOperator());
  }
  @Override public boolean prerequisite(final PostfixExpression ¢) {
    return ¢.getParent().getNodeType() != ASTNode.SWITCH_STATEMENT && !(¢.getParent() instanceof Expression)
        && yieldAncestors.untilNodeType(ASTNode.VARIABLE_DECLARATION_STATEMENT).from(¢) == null
        && yieldAncestors.untilNodeType(ASTNode.SINGLE_VARIABLE_DECLARATION).from(¢) == null
        && yieldAncestors.untilNodeType(ASTNode.VARIABLE_DECLARATION_EXPRESSION).from(¢) == null;
  }
  @Override public PrefixExpression replacement(final PostfixExpression ¢) {
    return subject.operand(operand(¢)).to(pre2post(¢.getOperator()));
  }
}

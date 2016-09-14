package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** converts, whenever possible, postfix increment/decrement to prefix
 * increment/decrement
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PostfixToPrefix extends ReplaceCurrentNode<PostfixExpression> implements Kind.Idiomatic {
  private static String description(final Operator ¢) {
    return (¢ == PostfixExpression.Operator.DECREMENT ? "de" : "in") + "crement";
  }

  private static PrefixExpression.Operator pre2post(final PostfixExpression.Operator ¢) {
    return ¢ == PostfixExpression.Operator.DECREMENT ? PrefixExpression.Operator.DECREMENT : PrefixExpression.Operator.INCREMENT;
  }

  @Override public boolean canWring(final PostfixExpression ¢) {
    return !(¢.getParent() instanceof Expression) //
        && searchAncestors.forType(ASTNode.VARIABLE_DECLARATION_STATEMENT).from(¢) == null //
        && searchAncestors.forType(ASTNode.SINGLE_VARIABLE_DECLARATION).from(¢) == null //
        && searchAncestors.forType(ASTNode.VARIABLE_DECLARATION_EXPRESSION).from(¢) == null;
  }

  @Override public boolean claims(@SuppressWarnings("unused") final PostfixExpression __) {
    return true;
  }

  @Override public String description(final PostfixExpression ¢) {
    return "Convert post-" + description(¢.getOperator()) + " of " + step.operand(¢) + " to pre-" + description(¢.getOperator());
  }

  @Override public PrefixExpression replacement(final PostfixExpression ¢) {
    return subject.operand(step.operand(¢)).to(pre2post(¢.getOperator()));
  }
}

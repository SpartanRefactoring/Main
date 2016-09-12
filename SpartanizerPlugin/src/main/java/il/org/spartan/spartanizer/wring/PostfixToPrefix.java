package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** converts, whenever possible, postfix increment/decrement to prefix
 * increment/decrement
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PostfixToPrefix extends Wring.ReplaceCurrentNode<PostfixExpression> implements Kind.Idiomatic {
  private static String description(final Operator o) {
    return (o == PostfixExpression.Operator.DECREMENT ? "de" : "in") + "crement";
  }

  private static PrefixExpression.Operator pre2post(final PostfixExpression.Operator o) {
    return o == PostfixExpression.Operator.DECREMENT ? PrefixExpression.Operator.DECREMENT : PrefixExpression.Operator.INCREMENT;
  }

  @Override protected boolean eligible(final PostfixExpression x) {
    return !(x.getParent() instanceof Expression) //
        && searchAncestors.forType(ASTNode.VARIABLE_DECLARATION_STATEMENT).from(x) == null //
        && searchAncestors.forType(ASTNode.SINGLE_VARIABLE_DECLARATION).from(x) == null //
        && searchAncestors.forType(ASTNode.VARIABLE_DECLARATION_EXPRESSION).from(x) == null;
  }

  @Override String description(final PostfixExpression x) {
    return "Convert post-" + description(x.getOperator()) + " of " + step.operand(x) + " to pre-" + description(x.getOperator());
  }

  @Override PrefixExpression replacement(final PostfixExpression x) {
    return subject.operand(step.operand(x)).to(pre2post(x.getOperator()));
  }

  @Override boolean scopeIncludes(@SuppressWarnings("unused") final PostfixExpression __) {
    return true;
  }
}

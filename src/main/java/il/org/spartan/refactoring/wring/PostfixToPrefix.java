package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** converts, whenever possible, postfix increment/decrement to prefix
 * increment/decrement
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PostfixToPrefix extends Wring.ReplaceCurrentNode<PostfixExpression> implements Kind.Canonicalization {
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

  @Override protected boolean canMake(final PostfixExpression e) {
    return !(e.getParent() instanceof Expression) //
        && AncestorSearch.forType(ASTNode.VARIABLE_DECLARATION_STATEMENT).from(e) == null //
        && AncestorSearch.forType(ASTNode.SINGLE_VARIABLE_DECLARATION).from(e) == null //
        && AncestorSearch.forType(ASTNode.VARIABLE_DECLARATION_EXPRESSION).from(e) == null;
  }

  @Override PrefixExpression replacement(final PostfixExpression x) {
    return subject.operand(step.operand(x)).to(pre2post(x.getOperator()));
  }

  @Override boolean claims(@SuppressWarnings("unused") final PostfixExpression __) {
    return true;
  }
}

package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;

/** A fluent API class that wraps an {@link Expression} with parenthesis, if the
 * location in which this expression occurs requires such wrapping.
 * <p>
 * Typical usage is in the form <code>new Plan(expression).in(host)</code> where
 * <code>location</code> is the parent under which the expression is to be
 * placed.
 * @author Yossi Gil
 * @since 2015-08-20 */
public class Plant {
  private final Expression inner;

  /** Instantiates this class, recording the expression that might be wrapped.
   * @param inner JD */
  public Plant(final Expression inner) {
    this.inner = inner;
  }

  /** Executes conditional wrapping in parenthesis.
   * @param host the destined parent
   * @return either the expression itself, or the expression wrapped in
   *         parenthesis, depending on the relative precedences of the
   *         expression and its host. */
  public Expression into(final ASTNode host) {
    return noParenthesisRequiredIn(host) ? inner : parenthesize(inner);
  }

  private boolean noParenthesisRequiredIn(final ASTNode host) {
    return Precedence.greater(host, inner) || Precedence.equal(host, inner) && !Is.nonAssociative(host);
  }

  private ParenthesizedExpression parenthesize(final Expression e) {
    final ParenthesizedExpression $ = inner.getAST().newParenthesizedExpression();
    $.setExpression(duplicate(e));
    return $;
  }
}
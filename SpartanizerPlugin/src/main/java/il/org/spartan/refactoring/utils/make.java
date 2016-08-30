package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.iz.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.wring.*;

public interface make {
  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  static Expression notOf(final Expression ¢) {
    final PrefixExpression $ = subject.operand(¢).to(NOT);
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  static ThrowStatement throwOf(final Expression ¢) {
    return subject.operand(¢).toThrow();
  }

  static Expression minusOf(final Expression e) {
    return isLiteralZero(e) ? e : subject.operand(e).to(wizard.MINUS1);
  }

  /** Create a new {@link SimpleName} instance at the AST of the parameter
   * @param n JD
   * @param newName the name that the returned value shall bear
   * @return a new {@link SimpleName} instance at the AST of the parameter */
  static SimpleName newSimpleName(final ASTNode n, final String newName) {
    return n.getAST().newSimpleName(newName);
  }

  static ParenthesizedExpression parethesized(final Expression e) {
    final ParenthesizedExpression $ = e.getAST().newParenthesizedExpression();
    $.setExpression(navigate.parent(e) == null ? e : wizard.duplicate(e));
    return $;
  }

  static NumberLiteral newLiteral(final ASTNode n, final String token) {
    final NumberLiteral $ = n.getAST().newNumberLiteral();
    $.setToken(token);
    return $;
  }

  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param ¢ JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  static InfixExpression flip(final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(navigate.right(¢), navigate.left(¢)).to(wizard.conjugate(¢.getOperator()));
  }
}

package il.org.spartan.refactoring.assemble;

import static il.org.spartan.refactoring.ast.iz.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.*;

public enum make {
  ;
  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  public static Expression notOf(final Expression ¢) {
    final PrefixExpression $ = subject.operand(¢).to(NOT);
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  public static ThrowStatement throwOf(final Expression ¢) {
    return subject.operand(¢).toThrow();
  }

  static Expression minusOf(final Expression e) {
    return literal0(e) ? e : subject.operand(e).to(wizard.MINUS1);
  }

  /** Create a new {@link SimpleName} instance at the AST of the parameter
   * @param n JD
   * @param newName the name that the returned value shall bear
   * @return a new {@link SimpleName} instance at the AST of the parameter */
  public static SimpleName newSimpleName(final ASTNode n, final String newName) {
    return n.getAST().newSimpleName(newName);
  }

  public static ParenthesizedExpression parethesized(final Expression e) {
    final ParenthesizedExpression $ = e.getAST().newParenthesizedExpression();
    $.setExpression(step.parent(e) == null ? e : duplicate.of(e));
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
  public static InfixExpression conjugate(final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(step.right(¢), step.left(¢)).to(wizard.conjugate(¢.getOperator()));
  }

  static Expression minus(final Expression e, final NumberLiteral l) {
    return l == null ? minusOf(e) //
        : newLiteral(l, literal0(l) ? "0" : signAdjust(l.getToken())) //
    ;
  }

  private static String signAdjust(final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(token.startsWith("+") ? 1 : 0);
  }

  static List<Expression> minus(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    $.add(lisp.first(es));
    for (final Expression e : lisp.rest(es))
      $.add(minusOf(e));
    return $;
  }

  public static IfStatement ifWithoutElse(final Statement s, final InfixExpression condition) {
    final IfStatement $ = condition.getAST().newIfStatement();
    $.setExpression(condition);
    $.setThenStatement(s);
    $.setElseStatement(null);
    return $;
  }

  public static Expression minus(final Expression e) {
    final PrefixExpression ¢ = az.prefixExpression(e);
    return ¢ == null ? minus(e, az.numberLiteral(e))
        : ¢.getOperator() == wizard.MINUS1 ? ¢.getOperand() //
            : ¢.getOperator() == wizard.PLUS1 ? subject.operand(¢.getOperand()).to(wizard.MINUS1)//
                : e;
  }
}

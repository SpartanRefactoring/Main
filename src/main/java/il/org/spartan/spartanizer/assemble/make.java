package il.org.spartan.spartanizer.assemble;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.iz.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wrings.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum make {
  ;
  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param ¢ JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  public static InfixExpression conjugate(final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(right(¢), left(¢)).to(wizard.conjugate(¢.getOperator()));
  }

  public static ASTHolder from(final ASTNode ¢) {
    assert ¢ != null;
    return new make.ASTHolder(¢.getAST());
  }

  public static IfStatement ifWithoutElse(final Statement s, final InfixExpression condition) {
    final IfStatement $ = condition.getAST().newIfStatement();
    $.setExpression(condition);
    $.setThenStatement(s);
    $.setElseStatement(null);
    return $;
  }

  public static StringLiteral makeEmptyString(final ASTNode ¢) {
    return make.from(¢).literal("");
  }

  public static Expression minus(final Expression x) {
    final PrefixExpression ¢ = az.prefixExpression(x);
    return ¢ == null ? minus(x, az.numberLiteral(x))
        : ¢.getOperator() == wizard.MINUS1 ? ¢.getOperand() //
            : ¢.getOperator() == wizard.PLUS1 ? subject.operand(¢.getOperand()).to(wizard.MINUS1)//
                : x;
  }

  /** Create a new {@link SimpleName} instance at the AST of the parameter
   * @param n JD
   * @param newName the name that the returned value shall bear
   * @return a new {@link SimpleName} instance at the AST of the parameter */
  public static SimpleName newSimpleName(final ASTNode n, final String newName) {
    return n.getAST().newSimpleName(newName);
  }

  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  public static Expression notOf(final Expression ¢) {
    final PrefixExpression $ = subject.operand(¢).to(NOT);
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  public static ParenthesizedExpression parethesized(final Expression x) {
    final ParenthesizedExpression $ = x.getAST().newParenthesizedExpression();
    $.setExpression(step.parent(x) == null ? x : duplicate.of(x));
    return $;
  }

  /** A fluent API method that wraps an {@link Expression} with parenthesis, if
   * the location in which this expression occurs requires such wrapping.
   * <p>
   * Typical usage is in the form <code>new Plan(expression).in(host)</code>
   * where <code>location</code> is the parent under which the expression is to
   * be placed.
   * <p>
   * This function is a factory method recording the expression that might be
   * wrapped.
   * @param inner JD */
  public static make.PlantingExpression plant(final Expression ¢) {
    return new make.PlantingExpression(¢);
  }

  /** Factory method recording the statement might be wrapped.
   * @param inner JD */
  public static make.PlantingStatement plant(final Statement inner) {
    return new make.PlantingStatement(inner);
  }

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  public static ThrowStatement throwOf(final Expression ¢) {
    return subject.operand(¢).toThrow();
  }

  static Expression makeInfix(final List<Expression> xs, final AST t) {
    if (xs.size() == 1)
      return first(xs);
    final InfixExpression $ = t.newInfixExpression();
    $.setOperator(wizard.PLUS2);
    $.setLeftOperand(duplicate.of(first(xs)));
    $.setRightOperand(duplicate.of(second(xs)));
    for (int i = 2; i < xs.size(); ++i)
      step.extendedOperands($).add(duplicate.of(xs.get(i)));
    return $;
  }

  static Expression minus(final Expression x, final NumberLiteral l) {
    return l == null ? minusOf(x) //
        : newLiteral(l, literal0(l) ? "0" : signAdjust(l.getToken())) //
    ;
  }

  static List<Expression> minus(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    $.add(first(xs));
    for (final Expression ¢ : rest(xs))
      $.add(minusOf(¢));
    return $;
  }

  static Expression minusOf(final Expression ¢) {
    return literal0(¢) ? ¢ : subject.operand(¢).to(wizard.MINUS1);
  }

  static NumberLiteral newLiteral(final ASTNode n, final String token) {
    final NumberLiteral $ = n.getAST().newNumberLiteral();
    $.setToken(token);
    return $;
  }

  private static String signAdjust(final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(token.startsWith("+") ? 1 : 0);
  }

  public static class ASTHolder {
    private final AST ast;

    public ASTHolder(final AST ast) {
      this.ast = ast;
    }

    public NumberLiteral literal(final int ¢) {
      return ast.newNumberLiteral(¢ + "");
    }

    public StringLiteral literal(final String s) {
      final StringLiteral $ = ast.newStringLiteral();
      $.setLiteralValue(s);
      return $;
    }
  }

  public static class PlantingExpression {
    /** Determines whether an infix expression can be added to String concating
     * without parenthesis type wise.
     * @param Expression
     * @return true if e is an infix expression and if it's first operand is of
     *         type String and false otherwise */
    static boolean isStringConactingSafe(final Expression ¢) {
      return infixExpression(¢) && isStringConcatingSafe(az.infixExpression(¢));
    }

    private static boolean isStringConcatingSafe(final InfixExpression ¢) {
      return type.get(¢.getLeftOperand()) == Certain.STRING;
    }

    private final Expression inner;

    /** Instantiates this class, recording the expression that might be wrapped.
     * @param inner JD */
    PlantingExpression(final Expression inner) {
      this.inner = inner;
    }

    /** Executes conditional wrapping in parenthesis.
     * @param host the destined parent
     * @return either the expression itself, or the expression wrapped in
     *         parenthesis, depending on the relative precedences of the
     *         expression and its host. */
    public Expression into(final ASTNode host) {
      return noParenthesisRequiredIn(host) || stringConcatingSafeIn(host) || simple(inner) ? inner : parenthesize(inner);
    }

    public Expression intoLeft(final InfixExpression host) {
      return precedence.greater(host, inner) || precedence.equal(host, inner) || simple(inner) ? inner : parenthesize(inner);
    }

    private boolean noParenthesisRequiredIn(final ASTNode host) {
      return precedence.greater(host, inner) || precedence.equal(host, inner) && !wizard.nonAssociative(host);
    }

    private ParenthesizedExpression parenthesize(final Expression x) {
      final ParenthesizedExpression $ = inner.getAST().newParenthesizedExpression();
      $.setExpression(duplicate.of(x));
      return $;
    }

    /** Determines whether inner can be added to host without parenthesis
     * because host is a String concating InfixExpression and host is an infix
     * expression starting with a String
     * @param host
     * @return */
    private boolean stringConcatingSafeIn(final ASTNode host) {
      if (!infixExpression(host))
        return false;
      final InfixExpression e = az.infixExpression(host);
      return (e.getOperator() != wizard.PLUS2 || !stringType.isNot(e)) && isStringConactingSafe(inner);
    }
  }

  public static class PlantingStatement {
    private final Statement inner;

    public PlantingStatement(final Statement inner) {
      this.inner = inner;
    }

    public void intoThen(final IfStatement s) {
      final IfStatement plant = az.ifStatement(inner);
      s.setThenStatement(plant == null || plant.getElseStatement() != null ? inner : subject.statements(inner).toBlock());
    }
  }
}

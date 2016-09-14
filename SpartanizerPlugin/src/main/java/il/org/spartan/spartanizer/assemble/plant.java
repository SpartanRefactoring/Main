package il.org.spartan.spartanizer.assemble;

import static il.org.spartan.spartanizer.ast.iz.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;

/** A fluent API class that wraps an {@link Expression} with parenthesis, if the
 * location in which this expression occurs requires such wrapping.
 * <p>
 * Typical usage is in the form <code>new Plan(expression).in(host)</code> where
 * <code>location</code> is the parent under which the expression is to be
 * placed.
 * @author Yossi Gil
 * @since 2015-08-20 */
public interface plant {
  public static class PlantingExpression {
    /** Determines whether an infix expression can be added to String concating
     * without parenthesis type wise.
     * @param Expression
     * @return true if e is an infix expression and if it's first operand is of
     *         type String and false otherwise */
    static boolean isStringConactingSafe(final Expression ¢) {
      return iz.infixExpression(¢) && isStringConcatingSafe(az.infixExpression(¢));
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
      if (!iz.infixExpression(host))
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

  /** Factory method recording the expression that might be wrapped.
   * @param inner JD */
  static PlantingExpression plant(final Expression inner) {
    return new PlantingExpression(inner);
  }

  /** Factory method recording the statement might be wrapped.
   * @param inner JD */
  static PlantingStatement plant(final Statement inner) {
    return new PlantingStatement(inner);
  }
}
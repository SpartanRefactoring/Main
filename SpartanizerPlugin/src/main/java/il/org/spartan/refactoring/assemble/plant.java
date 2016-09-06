package il.org.spartan.refactoring.assemble;

import static il.org.spartan.refactoring.ast.iz.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

/** A fluent API class that wraps an {@link Expression} with parenthesis, if the
 * location in which this expression occurs requires such wrapping.
 * <p>
 * Typical usage is in the form <code>new Plan(expression).in(host)</code> where
 * <code>location</code> is the parent under which the expression is to be
 * placed.
 * @author Yossi Gil
 * @since 2015-08-20 */
public interface plant {
  /**
   * Factory method recording the expression that might be wrapped.
   * @param inner  JD 
   */
  static PlantingExpression plant(final Expression inner) {
    return new PlantingExpression(inner);
  }

  /**
   * Factory method recording the statement might be wrapped.
   * @param inner  JD 
   */
  static PlantingStatement plant(final Statement inner) {
    return new PlantingStatement(inner);
  }

  public static class PlantingExpression {
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
      return noParenthesisRequiredIn(host) || simple(inner) ? inner : parenthesize(inner);
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
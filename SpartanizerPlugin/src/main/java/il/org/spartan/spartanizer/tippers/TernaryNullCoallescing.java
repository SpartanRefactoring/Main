package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace X != null ? X : Y with X ?? Y <br>
 * replace X == null ? Y : X with X ?? Y <br>
 * replace null == X ? Y : X with X ?? Y <br>
 * replace null != X ? X : Y with X ?? Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class TernaryNullCoallescing extends ReplaceCurrentNode<ConditionalExpression> implements Kind.CommnoFactoring {
<<<<<<< HEAD
  private static ASTNode replacement(final Expression left, final Expression right, final Expression elze) {
    if (!iz.nullLiteral(left) && iz.nullLiteral(right) && wizard.same(left, elze))
      Counter.count(TernaryNullCoallescing.class);
    if (iz.nullLiteral(left) && !iz.nullLiteral(right) && wizard.same(right, elze))
=======
  @Override public ASTNode replacement(ConditionalExpression e) {
    if (!iz.comparison(az.infixExpression(step.expression(e))))
      return null;
    InfixExpression condition = az.comparison((step.expression(e)));
    Expression left = step.left(condition);
    Expression right = step.right(condition);
    return step.operator(condition) == EQUALS ? replacement(left, right, step.elze(e))
        : step.operator(condition) == NOT_EQUALS ? replacement(left, right, step.then(e)) : null;
  }

  private static ASTNode replacement(Expression left, Expression right, Expression elze) {
    if ((!iz.nullLiteral(left) && iz.nullLiteral(right) && wizard.same(left, elze))
        || (iz.nullLiteral(left) && !iz.nullLiteral(right) && wizard.same(right, elze)))
>>>>>>> 3a128f4f03a0ce52ab12a7cef45b01f8c7fd7939
      Counter.count(TernaryNullCoallescing.class);
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "replace null coallescing ternary with ??";
  }

  @Override public ASTNode replacement(final ConditionalExpression e) {
    if (!iz.comparison(step.expression(e)))
      return null;
    final InfixExpression condition = az.comparison(step.expression(e));
    final Expression left = step.left(condition);
    final Expression right = step.right(condition);
    return step.operator(condition) == EQUALS ? replacement(left, right, step.elze(e))
        : step.operator(condition) == NOT_EQUALS ? replacement(left, right, step.then(e)) : null;
  }
}

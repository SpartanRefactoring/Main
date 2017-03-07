package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;
import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** pushes down "{@code !}", the negation operator as much as possible, using
 * the de-Morgan and other simplification rules.
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2015-7-17 */
public final class PrefixNotPushdown extends ReplaceCurrentNode<PrefixExpression>//
    implements TipperCategory.Idiomatic {
  static final long serialVersionUID = -9089304451195425885L;

  /** A utility function, which tries to simplify a boolean expression whose top
   * most operator is a logical negation. {@code !}
   * @param x JD
   * @return simplified parameter */
  public static Expression simplifyNot(final PrefixExpression ¢) {
    return pushdownNot(az.not(core(¢)));
  }

  static Expression notOfLiteral(final BooleanLiteral ¢) {
    final BooleanLiteral $ = copy.of(¢);
    $.setBooleanValue(!¢.booleanValue());
    return $;
  }

  static Expression perhapsNotOfLiteral(final Expression ¢) {
    return !iz.booleanLiteral(¢) ? null : notOfLiteral(az.booleanLiteral(¢));
  }

  static Expression pushdownNot(final Expression ¢) {
    Expression $;
    return ($ = perhapsNotOfLiteral(¢)) != null//
        || ($ = perhapsDoubleNegation(¢)) != null//
        || ($ = perhapsDeMorgan(¢)) != null//
        || ($ = perhapsComparison(¢)) != null //
            // || ($ = perhapsTernary(¢)) != null //
            ? $ : null;
  }

  static Expression comparison(final InfixExpression ¢) {
    return subject.pair(left(¢), right(¢)).to(wizard.negate(¢.getOperator()));
  }

  static boolean hasOpportunity(final Expression ¢) {
    return iz.booleanLiteral(¢) || az.not(¢) != null || az.andOrOr(¢) != null || iz.comparison(¢) || iz.conditionalExpression(¢);
  }

  static boolean hasOpportunity(final PrefixExpression ¢) {
    return ¢ != null && hasOpportunity(core(operand(¢)));
  }

  static Expression perhapsComparison(final Expression ¢) {
    return perhapsComparison(az.comparison(¢));
  }

  static Expression perhapsTernary(final Expression ¢) {
    return perhapsTernary(az.conditionalExpression(core(¢)));
  }

  static Expression perhapsTernary(final ConditionalExpression ¢) {
    return subject.pair(operand(then(¢)).to(NOT), operand(elze(¢)).to(NOT)).toCondition(expression(¢));
  }

  static Expression perhapsComparison(final InfixExpression ¢) {
    return ¢ == null ? null : comparison(¢);
  }

  static Expression perhapsDeMorgan(final Expression ¢) {
    return perhapsDeMorgan(az.andOrOr(¢));
  }

  static Expression perhapsDeMorgan(final InfixExpression ¢) {
    return ¢ == null ? null : wizard.applyDeMorgan(¢);
  }

  static Expression perhapsDoubleNegation(final Expression ¢) {
    return perhapsDoubleNegation(az.not(¢));
  }

  static Expression perhapsDoubleNegation(final PrefixExpression ¢) {
    return ¢ == null ? null : tryToSimplify(operand(¢));
  }

  static Expression pushdownNot(final PrefixExpression ¢) {
    return ¢ == null ? null : pushdownNot(operand(¢));
  }

  static Expression tryToSimplify(final Expression ¢) {
    final Expression $ = pushdownNot(az.not(¢));
    return $ != null ? $ : ¢;
  }

  @Override public String description(@SuppressWarnings("unused") final PrefixExpression __) {
    return "Pushdown logical negation ('!')";
  }

  @Override public boolean prerequisite(final PrefixExpression ¢) {
    return ¢ != null && az.not(¢) != null && hasOpportunity(az.not(¢));
  }

  @Override public Expression replacement(final PrefixExpression ¢) {
    return simplifyNot(¢);
  }
}
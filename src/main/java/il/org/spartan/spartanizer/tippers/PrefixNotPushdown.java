package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.make.*;
import static il.org.spartan.spartanizer.ast.factory.subject.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.operand;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** pushes down "{@code !}", the negation operator as much as possible, using
 * the de-Morgan and other simplification rules.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-7-17 */
public final class PrefixNotPushdown extends ReplaceCurrentNode<PrefixExpression>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -9089304451195425885L;

  /** A utility function, which tries to simplify a boolean expression, whose
   * top most parameter is logical negation.
   * @param x JD
   * @return simplified parameter */
  @Nullable public static Expression simplifyNot(@NotNull final PrefixExpression ¢) {
    return pushdownNot(az.not(core(¢)));
  }

  static Expression notOfLiteral(@NotNull final BooleanLiteral ¢) {
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
        || ($ = perhapsTernary(¢)) != null //
            ? $ : null;
  }

  @Nullable static Expression perhapsTernary(final Expression ¢) {
    return perhapsTernary(az.conditionalExpression(core(¢)));
  }

  @Nullable static Expression perhapsTernary(@Nullable final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    final Expression expression = ¢.getExpression(), then = ¢.getThenExpression(), elze = ¢.getElseExpression(),
        $ = pushdownNot(pair(pair(expression, then).to(CONDITIONAL_AND), elze).to(CONDITIONAL_OR)),
        $2 = pair(notOf(then), notOf(elze)).toCondition(expression);
    return count.nodes($) < count.nodes($2) ? $ : $2;
  }

  private static Expression comparison(@NotNull final InfixExpression ¢) {
    return pair(left(¢), right(¢)).to(wizard.negate(¢.getOperator()));
  }

  private static boolean hasOpportunity(final Expression inner) {
    return iz.booleanLiteral(inner) || az.not(inner) != null || az.shortcircuit(inner) != null || az.comparison(inner) != null
        || az.conditionalExpression(inner) != null;
  }

  private static boolean hasOpportunity(@Nullable final PrefixExpression ¢) {
    return ¢ != null && hasOpportunity(core(operand(¢)));
  }

  @Nullable private static Expression perhapsComparison(final Expression inner) {
    return perhapsComparison(az.comparison(inner));
  }

  @Nullable private static Expression perhapsComparison(@Nullable final InfixExpression inner) {
    return inner == null ? null : comparison(inner);
  }

  @Nullable private static Expression perhapsDeMorgan(final Expression ¢) {
    return perhapsDeMorgan(az.shortcircuit(¢));
  }

  @Nullable private static Expression perhapsDeMorgan(@Nullable final InfixExpression ¢) {
    return ¢ == null ? null : wizard.applyDeMorgan(¢);
  }

  @Nullable private static Expression perhapsDoubleNegation(final Expression ¢) {
    return perhapsDoubleNegation(az.not(¢));
  }

  @Nullable private static Expression perhapsDoubleNegation(@Nullable final PrefixExpression ¢) {
    return ¢ == null ? null : tryToSimplify(operand(¢));
  }

  @Nullable private static Expression pushdownNot(@Nullable final PrefixExpression ¢) {
    return ¢ == null ? null : pushdownNot(operand(¢));
  }

  @Nullable private static Expression tryToSimplify(final Expression ¢) {
    final Expression $ = pushdownNot(az.not(¢));
    return $ != null ? $ : ¢;
  }

  @Override public String description(@SuppressWarnings("unused") final PrefixExpression __) {
    return "Pushdown logical negation ('!')";
  }

  @Override public boolean prerequisite(@Nullable final PrefixExpression ¢) {
    return ¢ != null && az.not(¢) != null && hasOpportunity(az.not(¢));
  }

  @Override @Nullable public Expression replacement(@NotNull final PrefixExpression ¢) {
    return simplifyNot(¢);
  }
}
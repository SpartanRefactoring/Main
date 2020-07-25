package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.subject.pair;
import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.operand;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.ast.navigate.wizard.applyDeMorgan;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;

import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** pushes down "{@code !}", the negation operator as much as possible, using
 * the de-Morgan and other simplification rules.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PrefixNotPushdown extends ReplaceCurrentNode<PrefixExpression>//
    implements Category.Theory.Logical {
  private static final long serialVersionUID = -0x7E23B23DBCE1545DL;

  /** A utility function, which tries to simplify a boolean expression, whose
   * top most parameter is logical negation.
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
        || ($ = perhapsTernary(¢)) != null //
            ? $ : null;
  }
  static Expression perhapsTernary(final Expression ¢) {
    return perhapsTernary(az.conditionalExpression(core(¢)));
  }
  static Expression perhapsTernary(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    final Expression expression = ¢.getExpression(), then = ¢.getThenExpression(), elze = ¢.getElseExpression(),
        $ = pushdownNot(pair(pair(expression, then).to(CONDITIONAL_AND), elze).to(CONDITIONAL_OR)),
        $2 = pair(cons.not(then), cons.not(elze)).toCondition(expression);
    return countOf.nodes($) < countOf.nodes($2) ? $ : $2;
  }
  private static Expression comparison(final InfixExpression ¢) {
    return pair(left(¢), right(¢)).to(op.negate(¢.getOperator()));
  }
  private static boolean hasOpportunity(final Expression inner) {
    return iz.booleanLiteral(inner) || az.not(inner) != null || az.shortcircuit(inner) != null || az.comparison(inner) != null
        || az.conditionalExpression(inner) != null;
  }
  private static boolean hasOpportunity(final PrefixExpression ¢) {
    return ¢ != null && hasOpportunity(core(operand(¢)));
  }
  private static Expression perhapsComparison(final Expression inner) {
    return perhapsComparison(az.comparison(inner));
  }
  private static Expression perhapsComparison(final InfixExpression inner) {
    return inner == null ? null : comparison(inner);
  }
  private static Expression perhapsDeMorgan(final Expression ¢) {
    return perhapsDeMorgan(az.shortcircuit(¢));
  }
  private static Expression perhapsDeMorgan(final InfixExpression ¢) {
    return ¢ == null ? null : applyDeMorgan(¢);
  }
  private static Expression perhapsDoubleNegation(final Expression ¢) {
    return perhapsDoubleNegation(az.not(¢));
  }
  private static Expression perhapsDoubleNegation(final PrefixExpression ¢) {
    return ¢ == null ? null : tryToSimplify(operand(¢));
  }
  private static Expression pushdownNot(final PrefixExpression ¢) {
    return ¢ == null ? null : pushdownNot(operand(¢));
  }
  private static Expression tryToSimplify(final Expression ¢) {
    final Expression $ = pushdownNot(az.not(¢));
    return $ == null ? ¢ : $;
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
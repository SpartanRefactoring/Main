package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** pushes down "<code>!</code>", the negation operator as much as possible,
 * using the de-Morgan and other simplification rules.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PrefixNotPushdown extends ReplaceCurrentNode<PrefixExpression> implements Kind.Idiomatic {
  /** @param o JD
   * @return operator that produces the logical negation of the parameter */
  public static Operator conjugate(final Operator ¢) {
    return ¢ == null ? null
        : ¢.equals(CONDITIONAL_AND) ? CONDITIONAL_OR //
            : ¢.equals(CONDITIONAL_OR) ? CONDITIONAL_AND //
                : ¢.equals(EQUALS) ? NOT_EQUALS
                    : ¢.equals(NOT_EQUALS) ? EQUALS
                        : ¢.equals(LESS_EQUALS) ? GREATER
                            : ¢.equals(GREATER) ? LESS_EQUALS //
                                : ¢.equals(GREATER_EQUALS) ? LESS //
                                    : ¢.equals(LESS) ? GREATER_EQUALS : null;
  }

  /** A utility function, which tries to simplify a boolean expression, whose
   * top most parameter is logical negation.
   * @param x JD
   * @return simplified parameter */
  public static Expression simplifyNot(final PrefixExpression ¢) {
    return pushdownNot(az.not(extract.core(¢)));
  }

  static Expression notOfLiteral(final BooleanLiteral l) {
    final BooleanLiteral $ = duplicate.of(l);
    $.setBooleanValue(!l.booleanValue());
    return $;
  }

  static Expression perhapsNotOfLiteral(final Expression ¢) {
    return !iz.booleanLiteral(¢) ? null : notOfLiteral(az.booleanLiteral(¢));
  }

  static Expression pushdownNot(final Expression x) {
    Expression $;
    return ($ = perhapsNotOfLiteral(x)) != null//
        || ($ = perhapsDoubleNegation(x)) != null//
        || ($ = perhapsDeMorgan(x)) != null//
        || ($ = perhapsComparison(x)) != null //
            ? $ : null;
  }

  private static Expression comparison(final InfixExpression ¢) {
    return subject.pair(left(¢), right(¢)).to(conjugate(¢.getOperator()));
  }

  private static boolean hasOpportunity(final Expression inner) {
    return iz.booleanLiteral(inner) || az.not(inner) != null || az.andOrOr(inner) != null || az.comparison(inner) != null;
  }

  private static boolean hasOpportunity(final PrefixExpression ¢) {
    return ¢ != null && hasOpportunity(core(step.operand(¢)));
  }

  private static Expression perhapsComparison(final Expression inner) {
    return perhapsComparison(az.comparison(inner));
  }

  private static Expression perhapsComparison(final InfixExpression inner) {
    return inner == null ? null : comparison(inner);
  }

  private static Expression perhapsDeMorgan(final Expression ¢) {
    return perhapsDeMorgan(az.andOrOr(¢));
  }

  private static Expression perhapsDeMorgan(final InfixExpression ¢) {
    return ¢ == null ? null : wizard.applyDeMorgan(¢);
  }

  private static Expression perhapsDoubleNegation(final Expression ¢) {
    return perhapsDoubleNegation(az.not(¢));
  }

  private static Expression perhapsDoubleNegation(final PrefixExpression ¢) {
    return ¢ == null ? null : tryToSimplify(step.operand(¢));
  }

  private static Expression pushdownNot(final PrefixExpression ¢) {
    return ¢ == null ? null : pushdownNot(step.operand(¢));
  }

  private static Expression tryToSimplify(final Expression x) {
    final Expression $ = pushdownNot(az.not(x));
    return $ != null ? $ : x;
  }

  @Override public boolean demandsToSuggestButPerhapsCant(final PrefixExpression ¢) {
    return ¢ != null && az.not(¢) != null && hasOpportunity(az.not(¢));
  }

  @Override public String description(@SuppressWarnings("unused") final PrefixExpression __) {
    return "Pushdown logical negation ('!')";
  }

  @Override public Expression replacement(final PrefixExpression ¢) {
    return simplifyNot(¢);
  }
}
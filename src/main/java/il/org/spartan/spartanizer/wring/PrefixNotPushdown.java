package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** pushes down "<code>!</code>", the negation operator as much as possible,
 * using the de-Morgan and other simplification rules.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PrefixNotPushdown extends Wring.ReplaceCurrentNode<PrefixExpression> implements Kind.Canonicalization {
  /** @param o JD
   * @return operator that produces the logical negation of the parameter */
  public static Operator conjugate(final Operator o) {
    return o == null ? null
        : o.equals(CONDITIONAL__AND) ? CONDITIONAL__OR //
            : o.equals(CONDITIONAL__OR) ? CONDITIONAL__AND //
                : o.equals(EQUALS) ? NOT__EQUALS
                    : o.equals(NOT__EQUALS) ? EQUALS
                        : o.equals(LESS__EQUALS) ? GREATER
                            : o.equals(GREATER) ? LESS__EQUALS //
                                : o.equals(GREATER__EQUALS) ? LESS //
                                    : o.equals(LESS) ? GREATER__EQUALS : null;
  }

  /** A utility function, which tries to simplify a boolean expression, whose
   * top most parameter is logical negation.
   * @param x JD
   * @return simplified parameter */
  public static Expression simplifyNot(final PrefixExpression x) {
    return pushdownNot(az.not(extract.core(x)));
  }

  static Expression notOfLiteral(final BooleanLiteral l) {
    final BooleanLiteral $ = duplicate.of(l);
    $.setBooleanValue(!l.booleanValue());
    return $;
  }

  static Expression perhapsNotOfLiteral(final Expression x) {
    return !iz.booleanLiteral(x) ? null : notOfLiteral(az.booleanLiteral(x));
  }

  static Expression pushdownNot(final Expression x) {
    Expression $;
    return ($ = perhapsNotOfLiteral(x)) != null//
        || ($ = perhapsDoubleNegation(x)) != null//
        || ($ = perhapsDeMorgan(x)) != null//
        || ($ = perhapsComparison(x)) != null //
            ? $ : null;
  }

  private static Expression applyDeMorgan(final InfixExpression inner) {
    final List<Expression> operands = new ArrayList<>();
    for (final Expression e : hop.operands(flatten.of(inner)))
      operands.add(make.notOf(e));
    return subject.operands(operands).to(conjugate(inner.getOperator()));
  }

  private static Expression comparison(final InfixExpression x) {
    return subject.pair(step.left(x), step.right(x)).to(conjugate(x.getOperator()));
  }

  private static boolean hasOpportunity(final Expression inner) {
    return iz.booleanLiteral(inner) || az.not(inner) != null || az.andOrOr(inner) != null || az.comparison(inner) != null;
  }

  private static boolean hasOpportunity(final PrefixExpression x) {
    return x != null && hasOpportunity(core(step.operand(x)));
  }

  private static Expression perhapsComparison(final Expression inner) {
    return perhapsComparison(az.comparison(inner));
  }

  private static Expression perhapsComparison(final InfixExpression inner) {
    return inner == null ? null : comparison(inner);
  }

  private static Expression perhapsDeMorgan(final Expression x) {
    return perhapsDeMorgan(az.andOrOr(x));
  }

  private static Expression perhapsDeMorgan(final InfixExpression x) {
    return x == null ? null : applyDeMorgan(x);
  }

  private static Expression perhapsDoubleNegation(final Expression x) {
    return perhapsDoubleNegation(az.not(x));
  }

  private static Expression perhapsDoubleNegation(final PrefixExpression x) {
    return x == null ? null : tryToSimplify(step.operand(x));
  }

  private static Expression pushdownNot(final PrefixExpression x) {
    return x == null ? null : pushdownNot(step.operand(x));
  }

  private static Expression tryToSimplify(final Expression x) {
    final Expression $ = pushdownNot(az.not(x));
    return $ != null ? $ : x;
  }

  @Override public boolean scopeIncludes(final PrefixExpression x) {
    return x != null && az.not(x) != null && hasOpportunity(az.not(x));
  }

  @Override String description(@SuppressWarnings("unused") final PrefixExpression ____) {
    return "Pushdown logical negation ('!')";
  }

  @Override Expression replacement(final PrefixExpression x) {
    return simplifyNot(x);
  }
}
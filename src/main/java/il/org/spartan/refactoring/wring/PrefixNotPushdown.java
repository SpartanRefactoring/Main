package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.utils.*;

/** pushes down "<code>!</code>", the negation operator as much as possible,
 * using the de-Morgan and other simplification rules.
 * @author Yossi Gil
 * @since 2015-7-17 */
public final class PrefixNotPushdown extends Wring.ReplaceCurrentNode<PrefixExpression> implements Kind.Canonicalization {
  private static Expression applyDeMorgan(final InfixExpression inner) {
    final List<Expression> operands = new ArrayList<>();
    for (final Expression e : extract.operands(flatten(inner)))
      operands.add(logicalNot(e));
    return subject.operands(operands).to(conjugate(inner.getOperator()));
  }

  private static Expression comparison(final InfixExpression e) {
    return subject.pair(left(e), right(e)).to(conjugate(e.getOperator()));
  }

  /** @param o JD
   * @return operator that produces the logical negation of the parameter */
  public static Operator conjugate(final Operator o) {
    return o == null ? null
        : o.equals(CONDITIONAL_AND) ? CONDITIONAL_OR //
            : o.equals(CONDITIONAL_OR) ? CONDITIONAL_AND //
                : o.equals(EQUALS) ? NOT_EQUALS
                    : o.equals(NOT_EQUALS) ? EQUALS
                        : o.equals(LESS_EQUALS) ? GREATER
                            : o.equals(GREATER) ? LESS_EQUALS //
                                : o.equals(GREATER_EQUALS) ? LESS //
                                    : o.equals(LESS) ? GREATER_EQUALS : null;
  }

  private static boolean hasOpportunity(final Expression inner) {
    return Is.booleanLiteral(inner) || asNot(inner) != null || asAndOrOr(inner) != null || asComparison(inner) != null;
  }

  private static boolean hasOpportunity(final PrefixExpression e) {
    return e != null && hasOpportunity(core(operand(e)));
  }

  static Expression notOfLiteral(final BooleanLiteral l) {
    final BooleanLiteral $ = duplicate(l);
    $.setBooleanValue(!l.booleanValue());
    return $;
  }

  private static Expression perhapsComparison(final Expression inner) {
    return perhapsComparison(asComparison(inner));
  }

  private static Expression perhapsComparison(final InfixExpression inner) {
    return inner == null ? null : comparison(inner);
  }

  private static Expression perhapsDeMorgan(final Expression e) {
    return perhapsDeMorgan(asAndOrOr(e));
  }

  private static Expression perhapsDeMorgan(final InfixExpression e) {
    return e == null ? null : applyDeMorgan(e);
  }

  private static Expression perhapsDoubleNegation(final Expression e) {
    return perhapsDoubleNegation(asNot(e));
  }

  private static Expression perhapsDoubleNegation(final PrefixExpression e) {
    return e == null ? null : tryToSimplify(operand(e));
  }

  static Expression perhapsNotOfLiteral(final Expression e) {
    return !Is.booleanLiteral(e) ? null : notOfLiteral(asBooleanLiteral(e));
  }

  static Expression pushdownNot(final Expression e) {
    Expression $;
    return ($ = perhapsNotOfLiteral(e)) != null//
        || ($ = perhapsDoubleNegation(e)) != null//
        || ($ = perhapsDeMorgan(e)) != null//
        || ($ = perhapsComparison(e)) != null //
            ? $ : null;
  }

  private static Expression pushdownNot(final PrefixExpression e) {
    return e == null ? null : pushdownNot(operand(e));
  }

  /** A utility function, which tries to simplify a boolean expression, whose
   * top most parameter is logical negation.
   * @param e JD
   * @return simplified parameter */
  public static Expression simplifyNot(final PrefixExpression e) {
    return pushdownNot(asNot(extract.core(e)));
  }

  private static Expression tryToSimplify(final Expression e) {
    final Expression $ = pushdownNot(asNot(e));
    return $ != null ? $ : e;
  }

  @Override String description(@SuppressWarnings("unused") final PrefixExpression __) {
    return "Pushdown logical negation ('!')";
  }

  @Override Expression replacement(final PrefixExpression e) {
    return simplifyNot(e);
  }

  @Override public boolean scopeIncludes(final PrefixExpression e) {
    return e != null && asNot(e) != null && hasOpportunity(asNot(e));
  }
}

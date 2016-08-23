package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Plant.*;
import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

/** Expands terms of +/- expressions without reordering.
 * @author Yossi Gil
 * @since 2016-08 */
public class TermsExpander {
  public static Expression simplify(final InfixExpression e) {
    return build(new TermsCollector(e));
  }

  static Expression build(final TermsCollector c) {
    return build(c.all());
  }

  static Expression build(final List<SignedExpression> es) {
    assert es.size() >= 2;
    final SignedExpression first = first(es);
    final SignedExpression second = second(es);
    Expression $ = build(first, second);
    List<SignedExpression> remainder = chop(chop(es));
    return remainder.isEmpty() ? $ : build($, remainder);
  }

  private static InfixExpression build(final SignedExpression e1, final SignedExpression e2) {
    if (e1.positive())
      return subject.pair(e1.expression, e2.expression).to(e2.positive() ? PLUS2 : MINUS2);
    e1.negative();
    return (//
    e2.positive() ? subject.pair(e2.expression, e1.expression) : //
        subject.pair(subject.operand(e1.expression).to(MINUS1), e2.expression)//
    ).to(MINUS2);
  }

  private static Expression build(final Expression first, final List<SignedExpression> rest) {
    assert first != null;
    if (rest == null || rest.isEmpty())
      return first;
    assert first instanceof InfixExpression;
    return build((InfixExpression) first, rest);
  }

  private static Expression build(final InfixExpression first, final List<SignedExpression> rest) {
    assert first != null;
    assert rest != null;
    final Operator o = first.getOperator();
    assert o == PLUS2 || o == MINUS2;
    final SignedExpression next = first(rest);
    assert next != null;
    final Expression $ = o == PLUS2 ? buildPlus(first, next) : buildMinus(first, next);
    final List<SignedExpression> remainder = chop(rest);
    return remainder == null ? $ : build($, remainder);
  }

  private static InfixExpression buildMinus(final InfixExpression base, final SignedExpression add) {
    return add.negative() ? append(base, add.expression) : subject.pair(base, add.expression).to(PLUS2);
  }

  private static InfixExpression buildPlus(final InfixExpression base, final SignedExpression add) {
    Expression expression = duplicate(add.expression);
    return add.positive() ? append(base, expression) : subject.pair(base, expression).to(MINUS2);
  }

  private static InfixExpression append(final InfixExpression base, final Expression add) {
    InfixExpression $ = duplicate(base);
    extendedOperands($).add(plant(duplicate(add)).into($));
    return $;
  }
}

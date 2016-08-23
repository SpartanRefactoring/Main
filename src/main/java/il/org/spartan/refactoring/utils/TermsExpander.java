package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Plant.*;
import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

/** Expands terms of +/- expressions without reordering.
 * <p>
 * Functions named {@link #base} are non-recursive
 * @author Yossi Gil
 * @since 2016-08 */
public class TermsExpander {
  public static Expression simplify(final InfixExpression e) {
    return base(new TermsCollector(e));
  }

  static Expression base(final List<SignedExpression> es) {
    assert es.size() >= 2;
    final SignedExpression first = first(es);
    final SignedExpression second = second(es);
    Expression $ = base(first, second);
    List<SignedExpression> remainder = chop(chop(es));
    return remainder.isEmpty() ? $ : recurse($, remainder);
  }

  static Expression base(final TermsCollector c) {
    return base(c.all());
  }

  private static InfixExpression base(final SignedExpression e1, final SignedExpression e2) {
    if (e1.positive())
      return subject.pair(e1.expression, e2.expression).to(e2.positive() ? PLUS2 : MINUS2);
    e1.negative();
    return (//
    e2.positive() ? subject.pair(e2.expression, e1.expression) : //
        subject.pair(subject.operand(e1.expression).to(MINUS1), e2.expression)//
    ).to(MINUS2);
  }

  private static InfixExpression appendMinus(final InfixExpression base, final SignedExpression add) {
    return add.negative() ? subject.append(base, add.expression) : subject.pair(base, add.expression).to(PLUS2);
  }

  private static InfixExpression appendPlus(final InfixExpression base, final SignedExpression add) {
    Expression e = duplicate(add.expression);
    return add.positive() ? subject.append(base, e) : subject.pair(base, e).to(MINUS2);
  }

  private static Expression recurse(final Expression first, final List<SignedExpression> rest) {
    assert first != null;
    if (rest == null || rest.isEmpty())
      return first;
    assert first instanceof InfixExpression;
    return recurse((InfixExpression) first, rest);
  }

  private static Expression recurse(final InfixExpression first, final List<SignedExpression> rest) {
    assert first != null;
    if (rest == null)
      return first;
    assert rest != null;
    final Operator o = first.getOperator();
    assert o == PLUS2 || o == MINUS2;
    final SignedExpression next = first(rest);
    assert next != null;
    final Expression $ = o == PLUS2 ? appendPlus(first, next) : appendMinus(first, next);
    return recurse($, chop(rest));
  }
}

package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;

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

  private static Expression base(final List<Term> ts) {
    assert ts.size() >= 2;
    final Term first = first(ts);
    final Term second = second(ts);
    final Expression $ = base(first, second);
    final List<Term> remainder = chop(chop(ts));
    return remainder.isEmpty() ? $ : recurse($, remainder);
  }

  static Expression base(final TermsCollector c) {
    return base(c.all());
  }

  private static InfixExpression base(final Term t1, final Term t2) {
    if (t1.positive())
      return subject.pair(t1.expression, t2.expression).to(t2.positive() ? PLUS2 : MINUS2);
    assert t1.negative();
    return (//
    t2.positive() ? subject.pair(t2.expression, t1.expression) : //
        subject.pair(subject.operand(t1.expression).to(MINUS1), t2.expression)//
    ).to(MINUS2);
  }

  /** @see #recurse(InfixExpression, List) */
  private static InfixExpression appendMinus(final InfixExpression $, final Term ¢) {
    return ¢.negative() ? subject.append($, ¢.expression) : subject.pair($, ¢.expression).to(PLUS2);
  }

  /** @see #recurse(InfixExpression, List) */
  private static InfixExpression appendPlus(final InfixExpression $, final Term e) {
    final Expression ¢ = duplicate(e.expression);
    return e.positive() ? subject.append($, ¢) : subject.pair($, ¢).to(MINUS2);
  }

  /** @param $ The accumulator, to which one more {@link Term} should be added
   *        optimally
   * @param es a list
   * @return the $ parameter, after all elements of the list parameter are added
   *         to it */
  private static Expression recurse(final Expression $, final List<Term> es) {
    assert $ != null;
    if (es == null || es.isEmpty())
      return $;
    assert $ instanceof InfixExpression;
    return recurse((InfixExpression) $, es);
  }

  /** @see #recurse(InfixExpression, List) */
  private static Expression recurse(final InfixExpression $, final List<Term> es) {
    assert $ != null;
    if (es == null || es.isEmpty())
      return $;
    assert es != null;
    assert !es.isEmpty();
    final Operator o = $.getOperator();
    assert o != null;
    assert o == PLUS2 || o == MINUS2;
    final Term first = first(es);
    assert first != null;
    return recurse((o == PLUS2 ? appendPlus($, first) : appendMinus($, first)), chop(es));
  }
}

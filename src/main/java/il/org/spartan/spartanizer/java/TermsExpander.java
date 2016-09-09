package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;

/** Expands terms of +/- expressions without reordering.
 * <p>
 * Functions named {@link #base} are non-recursive
 * @author Yossi Gil
 * @since 2016-08 */
public class TermsExpander {
  public static Expression simplify(final InfixExpression x) {
    return base(new TermsCollector(x));
  }

  /** @see #recurse(List, InfixExpression) */
  private static InfixExpression appendMinus(final Term ¢, final InfixExpression $) {
    return ¢.negative() ? subject.append($, ¢.expression) : subject.pair($, ¢.expression).to(PLUS2);
  }

  /** @see #recurse(List, InfixExpression) */
  private static InfixExpression appendPlus(final Term t, final InfixExpression $) {
    final Expression ¢ = duplicate.of(t.expression);
    return t.positive() ? subject.append($, ¢) : subject.pair($, ¢).to(MINUS2);
  }

  private static Expression base(final List<Term> ts) {
    assert ts != null;
    assert !ts.isEmpty();
    final Term first = first(ts);
    assert first != null;
    final Term second = second(ts);
    assert second != null;
    final Expression $ = base(first, second);
    assert $ != null;
    return step(chop(chop(ts)), $);
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

  private static Expression base(final TermsCollector c) {
    return base(c.all());
  }

  /** @param ts a list
   * @param $ The accumulator, to which one more {@link Term} should be added
   *        optimally
   * @return the $ parameter, after all elements of the list parameter are added
   *         to it */
  private static Expression recurse(final List<Term> ts, final Expression $) {
    assert $ != null;
    if (ts == null || ts.isEmpty())
      return $;
    assert $ instanceof InfixExpression;
    return recurse(ts, (InfixExpression) $);
  }

  /** @see #recurse(List, InfixExpression) */
  private static Expression recurse(final List<Term> ts, final InfixExpression $) {
    assert $ != null;
    if (ts == null || ts.isEmpty())
      return $;
    assert ts != null;
    assert !ts.isEmpty();
    final Operator o = $.getOperator();
    assert o != null;
    assert o == PLUS2 || o == MINUS2;
    final Term first = first(ts);
    assert first != null;
    return recurse(chop(ts), o == PLUS2 ? appendPlus(first, $) : appendMinus(first, $));
  }

  private static Expression step(final List<Term> ¢, final Expression $) {
    assert ¢ != null;
    return ¢.isEmpty() ? $ : recurse(¢, $);
  }
}

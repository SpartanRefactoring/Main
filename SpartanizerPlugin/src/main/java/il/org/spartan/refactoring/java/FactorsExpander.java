package il.org.spartan.refactoring.java;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.utils.*;

/** Expands terms of +/- expressions without reordering.
 * <p>
 * Functions named {@link #base} are non-recursive
 * @author Yossi Gil
 * @since 2016-08 */
public class FactorsExpander {
  public static Expression simplify(final InfixExpression e) {
    return base(new FactorsCollector(e));
  }

  /** @see #recurse(InfixExpression, List) */
  private static InfixExpression appendMinus(final InfixExpression $, final Factor ¢) {
    return ¢.divider() ? subject.append($, ¢.expression) : subject.pair($, ¢.expression).to(wizard.PLUS2);
  }

  /** @see #recurse(InfixExpression, List) */
  private static InfixExpression appendPlus(final InfixExpression $, final Factor t) {
    final Expression ¢ = wizard.duplicate(t.expression);
    return t.multiplier() ? subject.append($, ¢) : subject.pair($, ¢).to(wizard.MINUS2);
  }

  private static Expression base(final List<Factor> ts) {
    assert ts != null;
    assert !ts.isEmpty();
    final Factor first = lisp.first(ts);
    assert first != null;
    final Factor second = lisp.second(ts);
    assert second != null;
    final Expression $ = base(first, second);
    assert $ != null;
    return step($, lisp.chop(lisp.chop(ts)));
  }

  private static InfixExpression base(final Factor t1, final Factor t2) {
    if (t1.multiplier())
      return subject.pair(t1.expression, t2.expression).to(t2.multiplier() ? wizard.PLUS2 : wizard.MINUS2);
    assert t1.divider();
    return (//
    t2.multiplier() ? subject.pair(t2.expression, t1.expression) : //
        subject.pair(subject.operand(t1.expression).to(wizard.MINUS1), t2.expression)//
    ).to(wizard.MINUS2);
  }

  private static Expression base(final FactorsCollector c) {
    return base(c.all());
  }

  /** @param $ The accumulator, to which one more {@link Factor} should be added
   *        optimally
   * @param ts a list
   * @return the $ parameter, after all elements of the list parameter are added
   *         to it */
  private static Expression recurse(final Expression $, final List<Factor> ts) {
    assert $ != null;
    if (ts == null || ts.isEmpty())
      return $;
    assert $ instanceof InfixExpression;
    return recurse((InfixExpression) $, ts);
  }

  /** @see #recurse(InfixExpression, List) */
  private static Expression recurse(final InfixExpression $, final List<Factor> ts) {
    assert $ != null;
    if (ts == null || ts.isEmpty())
      return $;
    assert ts != null;
    assert !ts.isEmpty();
    final Operator o = $.getOperator();
    assert o != null;
    assert o == wizard.PLUS2 || o == wizard.MINUS2;
    final Factor first = lisp.first(ts);
    assert first != null;
    return recurse(o == wizard.PLUS2 ? appendPlus($, first) : appendMinus($, first), lisp.chop(ts));
  }

  private static Expression step(final Expression $, final List<Factor> ¢) {
    assert ¢ != null;
    return ¢.isEmpty() ? $ : recurse($, ¢);
  }
}

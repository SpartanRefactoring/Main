package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;

/** Expands terms of * or / expressions without reordering.
 * <p>
 * Functions named {@link #base} are non-recursive
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @author Niv Shalmon
 * @since 2016-08 */
enum FactorsExpander {
  ;
  public static Expression simplify(final InfixExpression ¢) {
    return base(new FactorsCollector(¢));
  }

  private static InfixExpression appendDivide(final InfixExpression $, final Factor ¢) {
    return ¢.divider() ? subject.append($, ¢.expression) : subject.pair($, ¢.expression).to(TIMES);
  }

  private static InfixExpression appendTimes(final InfixExpression $, final Factor f) {
    final Expression ¢ = copy.of(f.expression);
    return f.multiplier() ? subject.append($, ¢) : subject.pair($, ¢).to(DIVIDE);
  }

  private static InfixExpression base(final Factor t1, final Factor t2) {
    if (t1.multiplier())
      return subject.pair(t1.expression, t2.expression).to(t2.multiplier() ? TIMES : DIVIDE);
    assert t1.divider();
    return (//
    t2.multiplier() ? subject.pair(t2.expression, t1.expression) : //
        subject.pair( //
            subject.pair(t1.expression.getAST().newNumberLiteral("1"), t1.expression //
            ).to(DIVIDE) //
            , t2.expression) //
    ).to(DIVIDE);
  }

  private static Expression base(final FactorsCollector ¢) {
    return base(¢.all());
  }

  private static Expression base(final List<Factor> fs) {
    assert fs != null;
    assert !fs.isEmpty();
    final Factor first = first(fs);
    assert first != null;
    final Factor second = second(fs);
    assert second != null;
    final Expression $ = base(first, second);
    assert $ != null;
    return step($, chop(chop(fs)));
  }

  /** @param $ The accumulator, to which one more {@link Factor} should be added
   *        optimally
   * @param fs a list
   * @return the $ parameter, after all elements of the list parameter are added
   *         to it */
  private static Expression recurse(final Expression $, final List<Factor> fs) {
    assert $ != null;
    if (fs == null || fs.isEmpty())
      return $;
    assert $ instanceof InfixExpression;
    return recurse((InfixExpression) $, fs);
  }

  private static Expression recurse(final InfixExpression $, final List<Factor> fs) {
    assert $ != null;
    if (fs == null || fs.isEmpty())
      return $;
    assert fs != null;
    assert !fs.isEmpty();
    final Operator o = $.getOperator();
    assert o != null;
    assert o == TIMES || o == DIVIDE;
    final Factor first = first(fs);
    assert first != null;
    return recurse(o == TIMES ? appendTimes($, first) : appendDivide($, first), chop(fs));
  }

  private static Expression step(final Expression $, final List<Factor> ¢) {
    assert ¢ != null;
    return ¢.isEmpty() ? $ : recurse($, ¢);
  }
}

package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** Unit tests for {@link Wrings#ADDITION_SORTER}.
 * @author Yossi Gil
 * @since 2014-07-13 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public final class InfixAdditionSortTest {
  private static final String input = "1+a*b+2+b*c+3+d*e+4";
  private static final InfixExpression INPUT = into.i(input);
  private static final int nTERMS = 7;
  private static final String OUTPUT = "a*b + b*c  + d*e + 1 + 2 + 3+4";

  @Test public void test00() {
    trimmingOf(input)//
        .gives(OUTPUT) //
        .gives("a*b + b*c  + d*e + 10").stays();
  }

  @Test public void test01() {
    trimmingOf("1 + a*b") //
        .gives("a*b + 1") //
        .stays();
  }

  @Test public void test02() {
    assert new InfixAdditionSort().canSuggest(INPUT);
  }

  @Test public void test03() {
    assert new InfixAdditionSort().canSuggest(INPUT);
  }

  @Test public void test04() {
    azzert.that(new InfixAdditionSort().replacement(INPUT), iz(OUTPUT));
  }

  @Test public void test05() {
    assert !new InfixAdditionSubtractionExpand().canSuggest(INPUT);
  }

  @Test public void test06() {
    assert !new InfixAdditionSubtractionExpand().canSuggest(INPUT);
  }

  @Test public void test07() {
    assert new InfixAdditionSubtractionExpand().replacement(INPUT) == null;
    assert new InfixAdditionSubtractionExpand().replacement(INPUT) == null;
  }

  @Test public void test08() {
    final InfixAdditionSubtractionExpand e = new InfixAdditionSubtractionExpand();
    assert e != null;
    assert !TermsCollector.isLeafTerm(INPUT);
    assert TermsExpander.simplify(INPUT) != null;
    assert e.replacement(INPUT) == null;
    assert e != null;
    assert !TermsCollector.isLeafTerm(INPUT);
    assert TermsExpander.simplify(INPUT) != null;
    assert e.replacement(INPUT) == null;
  }

  @Test public void test09() {
    final Expression e = TermsExpander.simplify(INPUT);
    azzert.that(e, instanceOf(InfixExpression.class));
    final InfixExpression i = (InfixExpression) e;
    azzert.that(i.getOperator(), is(wizard.PLUS2));
    assert hop.operands(i) != null;
    azzert.that(hop.operands(i).size(), is(nTERMS));
    assert wizard.same(i, INPUT);
  }

  @Test public void test10() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert wizard.same(i, INPUT);
  }

  @Test public void test11() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert INPUT != null;
    assert wizard.same(i, INPUT);
    assert i != null;
    assert INPUT != null;
    assert wizard.same(i, INPUT);
  }

  @Test public void test12() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    azzert.that(tide.clean(i + ""), is(tide.clean(INPUT + "")));
  }

  @Test public void test13() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert i.getNodeType() == INPUT.getNodeType();
    assert i != INPUT;
    assert i.getNodeType() == INPUT.getNodeType();
  }

  @Test public void test14() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert i != INPUT;
    azzert.that(i, iz(INPUT + ""));
  }

  @Test public void test15() {
    azzert.that(TermsExpander.simplify(INPUT) + "", iz(INPUT + ""));
  }
}

package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

/** Unit tests for {@link Wrings#ADDITION_SORTER}.
 * @author Yossi Gil
 * @since 2014-07-13 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class InfixAdditionSortTest {
  private static final String input = "1+a*b+2+b*c+3+d*e+4";
  private static final InfixExpression INPUT = Into.i(input);
  private static final int nTERMS = 7;
  private static final String OUTPUT = "a*b + b*c  + d*e + 1 + 2 + 3+4";

  @Test public void test00() {
    trimming(input)//
        .to(OUTPUT) //
        .to(null);
  }

  @Test public void test01() {
    trimming("1 + a*b") //
        .to("a*b + 1") //
        .to(null);
  }

  @Test public void test02() {
    assert new InfixAdditionSort().scopeIncludes(INPUT);
  }

  @Test public void test03() {
    assert new InfixAdditionSort().eligible(INPUT);
  }

  @Test public void test04() {
    azzert.that(new InfixAdditionSort().replacement(INPUT), iz(OUTPUT));
  }

  @Test public void test05() {
    assert !new InfixAdditionSubtractionExpand().scopeIncludes(INPUT);
  }

  @Test public void test06() {
    assert new InfixAdditionSubtractionExpand().eligible(INPUT);
  }

  @Test public void test07() {
    assert null == new InfixAdditionSubtractionExpand().replacement(INPUT);
    assert new InfixAdditionSubtractionExpand().replacement(INPUT) == null;
  }

  @Test public void test08() {
    final InfixAdditionSubtractionExpand e = new InfixAdditionSubtractionExpand();
    assert null != e;
    assert !TermsCollector.isLeafTerm(INPUT);
    assert null != TermsExpander.simplify(INPUT);
    assert null == e.replacement(INPUT);
    assert e != null;
    assert !(TermsCollector.isLeafTerm(INPUT));
    assert TermsExpander.simplify(INPUT) != null;
    assert e.replacement(INPUT) == null;
  }

  @Test public void test09() {
    final Expression e = TermsExpander.simplify(INPUT);
    azzert.that(e, instanceOf(InfixExpression.class));
    final InfixExpression i = (InfixExpression) e;
    azzert.that(i.getOperator(), is(PLUS2));
    assert extract.operands(i) != null;
    azzert.that(extract.operands(i).size(), is(nTERMS));
    assert Funcs.same(i, INPUT);
  }

  @Test public void test10() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert Funcs.same(i, INPUT);
  }

  @Test public void test11() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert null != i;
    assert null != INPUT;
    assert Funcs.same(i, INPUT);
    assert i != null;
    assert INPUT != null;
    assert (Funcs.same(i, INPUT));
  }

  @Test public void test12() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert null != i;
    assert !(i == INPUT);
    assert null != INPUT;
    assert i != null;
    assert i != INPUT;
    assert INPUT != null;
    azzert.that(Funcs.gist("" + i), is(Funcs.gist("" + INPUT)));
  }

  @Test public void test13() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert null != i;
    assert !(i == INPUT);
    assert i.getNodeType() == INPUT.getNodeType();
    assert i != null;
    assert i != INPUT;
    assert (i.getNodeType() == INPUT.getNodeType());
  }

  @Test public void test14() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert null != i;
    assert !(i == INPUT);
    assert i != null;
    assert i != INPUT;
    azzert.that(i, iz("" + INPUT));
  }

  @Test public void test15() {
    final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    azzert.that("" + i, iz("" + INPUT));
  }
}

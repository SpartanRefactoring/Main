package il.org.spartan.spartanizer.testing;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link Tricks#ADDITION_SORTER} .
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2014-07-13 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class InfixAdditionSortTest {
  private static final String input = "1+a*b+2+b*c+3+d*e+4";
  private static final InfixExpression INPUT = into.i(input);
  private static final int nTERMS = 7;
  private static final String OUTPUT = "a*b + b*c  + d*e + 1 + 2 + 3+4";

  @Test public void test00() {
    trimmingOf(input)//
        .gives(OUTPUT)//
        .gives("a*b + b*c  + d*e + 10")//
        .stays();
  }

  @Test public void test01() {
    trimmingOf("1 + a*b")//
        .gives("a*b + 1")//
        .stays();
  }

  @Test public void test01b() {
    trimmingOf("c*d + a*b")//
        .gives("a*b + c*d")//
        .stays();
  }

  @Test public void test01c() {
    trimmingOf("1 + c*d + a*b")//
        .gives("a*b + c*d + 1")//
        .stays();
  }

  @Test public void test01d() {
    trimmingOf("1 + 2 + c*d + a*b")//
        .gives("3 + c*d + a*b")//
        .gives("a*b + c*d + 3")//
        .stays();
  }

  @Test public void test01e() {
    trimmingOf("c/d + a*b")//
        .gives("a*b + c/d")//
        .stays();
  }

  @Test public void test01f() {
    trimmingOf("1 + c/d + a*b")//
        .gives("a*b + c/d + 1")//
        .stays();
  }

  @Test public void test01g() {
    trimmingOf("c*1 + a*b")//
        .gives("a*b + c*1")//
        .gives("a*b + c")//
        .stays();
  }

  @Test public void test01h() {
    trimmingOf("c*2 + a*b")//
        .gives("a*b + c*2")//
        .gives("a*b + 2*c")//
        .gives("2*c + a*b")//
        .stays();
  }

  @Test public void a2bc() {
    trimmingOf("a * 2 + b * c") //
        .using(InfixExpression.class, new InfixMultiplicationSort()) //
        .gives("2*a+b*c") //
        .stays() //
    ;
  }

  @Test public void testa2bc() {
    trimmingOf("a * 2 + b * c") //
        .gives("2*a+b*c") //
        .stays() //
    ;
  }

  /** Automatically generated on Tue-Mar-14-22:16:24-IST-2017, copied by
   * Matteo */
  @Test public void abc2() {
    trimmingOf("a * b + c * 2") //
        .using(InfixExpression.class, new InfixMultiplicationSort()) //
        .gives("a*b+2*c") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("2*c+a*b") //
        .stays() //
    ;
  }

  @Test public void test02() {
    assert new InfixAdditionSort().check(INPUT);
  }

  @Test public void test03() {
    assert new InfixAdditionSort().check(INPUT);
  }

  @Test public void test04() {
    azzert.that(new InfixAdditionSort().replacement(INPUT), iz(OUTPUT));
  }

  @Test public void test05() {
    assert !new InfixAdditionSubtractionExpand().check(INPUT);
  }

  @Ignore @Test public void test05b() {
    assert new InfixAdditionSubtractionExpand().check(INPUT);
  }

  private final String s = "365 * a + a / 4 - a / 100 + a / 400 + (b * 306 + 5) / 10 + c - 1";

  @Test public void test05c() {
    assert !new InfixAdditionSubtractionExpand().check(into.i(s));
  }

  @Test public void test05d() {
    assert new InfixAdditionSubtractionExpand().check(into.i("a - (b+c)"));
  }

  @Test public void test05e() {
    trimmingOf("a - (b+c)")//
        .gives("a - b - c")//
        .stays();
  }

  @Test public void test07() {
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
    assert wizard.same2(i, INPUT);
  }

  @Test public void test10() {
     final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert INPUT != null;
    assert !wizard.same(i, INPUT);
    assert wizard.same2(i, INPUT);
  }

  @Test public void test11() {
     final InfixExpression i = (InfixExpression) TermsExpander.simplify(INPUT);
    assert i != null;
    assert INPUT != null;
    assert wizard.same(into.i(tide.clean(i + "")), INPUT);
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

  @Test public void test16a() {
    trimmingOf("365 * a + a / 4 - a / 100 + a / 400 + (b * 306 + 5) / 10 + c - 1")//
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("c+a/400+(b*306+5)/10+(365*a+a/4-a/100)-1") //
        .using(InfixExpression.class, new InfixAdditionSubtractionExpand()) //
        .gives("c+a/400+(b*306+5)/10+365*a+a/4-a/100-1") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("c+a/4+365*a+a/400+(b*306+5)/10-a/100-1") //
        .using(InfixExpression.class, new InfixMultiplicationSort()) //
        .gives("c+a/4+365*a+a/400+(306*b+5)/10-a/100-1") //
        .stays();
  }

  @Test public void test16a2() {
    trimmingOf("365 * a + a / 4")//
        .gives("a / 4 + 365 * a").stays();
  }

  @Test public void test16a2a() {
    trimmingOf("365 * a + a / 4 - a / 100")//
        .gives("a / 4 + 365 * a - a / 100").stays();
  }

  @Test public void test_365aa4a100a400() {
    trimmingOf("365 * a + a / 4 - a / 100 + a / 400") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("a/400+365*a+a/4-a/100") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("a/4+365*a+a/400-a/100") //
        .stays() //
    ;
  }

  @Test public void test16a3() {
    trimmingOf("365 * a + d / 4 - e / 100")//
        .gives("d / 4 + 365 * a - e / 100").stays();
  }

  @Test public void test16a4() {
    trimmingOf("365 * a + b / 4 - c / 100 + d / 400") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("d/400+365*a+b/4-c/100") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("b/4+365*a+d/400-c/100") //
        .stays() //
    ;
  }

  @Test public void test16a2b() {
    trimmingOf("365 * a + a / 4 - a / 100 + a / 400")//
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("a/400+365*a+a/4-a/100") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("a/4+365*a+a/400-a/100") //
        .stays();
  }

  @Test public void test16a5() {
    trimmingOf("365 * a + b / 4 - c / 100 + d / 400") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("d/400+365*a+b/4-c/100") //
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .gives("b/4+365*a+d/400-c/100") //
        .stays() //
    ;
  }

  @Ignore @Test public void test16b() {
    trimmingOf("365 * a + a * 4 - a * 100 + a * 400 + (b * 306 + 5) * 10 + c - 1")//
        .using(InfixExpression.class, new InfixAdditionSort()) //
        .stays();
  }

  @Test public void test16d1() {
    trimmingOf("365 * a + a * 4")// *
        .gives("a*4 + 365*a")//
        .gives("4*a + 365*a")//
        .stays();
  }

  @Test public void test16e() {
    trimmingOf("365 * a + a / 4")//
        .gives("a/4 + 365*a").stays();
  }

  @Test public void test16f() {
    trimmingOf("365 * a + b / 4")//
        .gives("b/4 + 365*a").stays();
  }

  @Test public void test16g() {
    trimmingOf("a * 365 + b / 4")//
        .gives("b/4 + a*365")//
        .gives("b/4 + 365*a")//
        .stays();
  }

  @Test public void test16h() {
    trimmingOf("d / 4 + e / 100")//
        .stays();
  }

  @Test public void test16h1() {
    trimmingOf("d / 400 + e / 100")//
        .stays();
  }

  @Test public void test16i() {
    trimmingOf("d / 4 + e / 100 + h / 400")//
        .stays();
  }

  @Test public void test16j() {
    trimmingOf("d / 4 + e * 100")//
        .gives("d / 4 + 100*e")//
        .stays();
  }

  @Test public void test17() {
    trimmingOf("y / 100 + l - 365 * y - y / 4 - y / 400")//
        .gives("l+y/100-365*y-y/4-y/400") //
        .stays();
  }

  @Test public void test17a() {
    trimmingOf("y / 100 + l")//
        .stays();
  }
}

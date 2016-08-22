package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class TermsCollectorTest {
  @Test public void test_00() {
    azzert.that(c.collect(i("a*b")), is(c));
  }

  @Test public void test_01() {
    azzert.aye(TermsCollector.isLeafTerm(e("i")))//
        .andAye(TermsCollector.isLeafTerm(e("i*j"))) //
        .andAye(TermsCollector.isLeafTerm(e("(x)")));
  }

  @Test public void test_02() {
    azzert.aye(TermsCollector.isLeafTerm(e("(i*j)")));
    azzert.aye(TermsCollector.isLeafTerm(e("(i+j)")));
    azzert.nay(TermsCollector.isLeafTerm(e("i+j")));
    azzert.nay(TermsCollector.isLeafTerm(e("i-j")));
  }

  @Test public void test_03() {
    final InfixExpression i = i("a + b +c");
    c.collect(i);
    azzert.that(c.plus.size(), is(3));
    azzert.that(c.minus.size(), is(0));
  }

  @Test public void test_04() {
    final InfixExpression i = i("a-c");
    azzert.that(i.getOperator(), is(MINUS2));
    azzert.that(left(i), iz("a"));
    azzert.that(right(i), iz("c"));
    c.collect(i);
    azzert.that(c.plus.size(), is(1));
    azzert.that(c.minus.size(), is(1));
  }

  @Test public void test_05() {
    final InfixExpression i = i("a-c");
    azzert.that(i.getOperator(), is(MINUS2));
    azzert.that(left(i), iz("a"));
    azzert.that(right(i), iz("c"));
    c.collectPlusPrefix(i);
    azzert.that(c.plus.size(), is(1));
    azzert.that(c.minus.size(), is(1));
  }

  @Test public void test_06() {
    final InfixExpression i = i("a + b -c");
    azzert.that(i.getOperator(), is(MINUS2));
    azzert.that(asInfixExpression(left(i)).getOperator(), is(PLUS2));
    c.collect(i);
    azzert.that(c.plus.size(), is(2));
    azzert.that(c.minus.size(), is(1));
  }

  @Test public void test_07() {
    final InfixExpression i = i("a + (b -c)");
    c.collect(i);
    azzert.that(c.plus.size(), is(2));
    azzert.that(c.minus.size(), is(1));
  }

  @Test public void test_08() {
    final InfixExpression i = i("a + (b +(d + c))");
    c.collect(i);
    azzert.that(c.plus.size(), is(4));
    azzert.that(c.minus.size(), is(0));
  }

  @Test public void test_09() {
    final InfixExpression i = i("a - (b - c - (d - e - f - g))");
    c.collect(i);
    azzert.that(c.plus, iz("[a,c,d]"));
    azzert.that(c.minus, iz("[b,e,f,g]"));
  }

  @Test public void test_10() {
    final InfixExpression i = i("a - (b - c - d - e )");
    c.collect(i);
    azzert.that(c.plus.size(), is(4));
    azzert.that(c.minus.size(), is(1));
  }

  @Test public void test_11() {
    final InfixExpression i = i("a - (b - c)");
    c.collect(i);
    azzert.that(c.plus.size(), is(2));
  }

  @Test public void test_12() {
    final InfixExpression i = i("a - (b - c)");
    c.collect(i);
    azzert.that(c.minus.size(), is(1));
  }

  @Test public void test_13() {
    final InfixExpression i = i("a - (b - c)");
    c.collect(i);
    azzert.that(c.minus, iz("[b]"));
  }

  @Test public void test_14() {
    final InfixExpression i = i("a - (b - c)");
    c.collect(i);
    azzert.that(c.minus, iz("[b]"));
  }

  @Test public void test_15() {
    final InfixExpression i = i("(a + b) + (c+(d-(e+(f-g))))");
    c.collect(i);
    azzert.that(c.plus, iz("[a,b,c,d,g]"));
    azzert.that(c.minus, iz("[e,f]"));
  }

  @Test public void test_16() {
    final InfixExpression i = i("a + (b + c)");
    c.collect(i);
    azzert.that(c.plus, iz("[a,b,c]"));
    azzert.that(c.minus, iz("[]"));
  }

  @Test public void test_17() {
    final InfixExpression i = i("a + (b + c)");
    c.collect(i);
    azzert.that(c.minus, iz("[]"));
  }

  @Test public void test_18() {
    final InfixExpression i = i("a + (b - c)");
    c.collect(i);
    azzert.that(c.plus, iz("[a,b]"));
  }

  @Test public void test_19() {
    final InfixExpression i = i("a + (b - c)");
    c.collect(i);
    azzert.that(c.minus, iz("[c]"));
  }

  @Test public void test_20() {
    final InfixExpression i = i("a + (b - c)");
    c.collect(i);
    azzert.that(c.minus, iz("[c]"));
  }

  @Test public void test_21() {
    c.collect(null);
    azzert.aye(c.minus.isEmpty());
    azzert.aye(c.plus.isEmpty());
  }

  @Test public void test_22() {
    c.collect(i("i*j"));
    azzert.aye(c.minus.isEmpty());
    azzert.aye(c.plus.isEmpty());
  }

  @Test public void test_23() {
    c.collect(i("+a - b"));
    azzert.that(c.plus, iz("[a]"));
  }

  @Test public void test_24() {
    c.collect(i("-a + b"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_25() {
    azzert.that(core(e("(a)")), iz("a"));
  }

  @Test public void test_26() {
    azzert.that(core(e("((a))")), iz("a"));
  }

  @Test public void test_27() {
    azzert.that(core(e("+a")), iz("a"));
  }

  @Test public void test_28() {
    azzert.that(core(e(" + +a")), iz("a"));
  }

  @Test public void test_29() {
    azzert.that(core(e(" + (+a)")), iz("a"));
  }

  @Test public void test_30() {
    azzert.that(core(e(" +(+ (+a))")), iz("a"));
  }

  @Test public void test_31() {
    c.collect(i("-+  -+ -+-+-+(a) + b"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_32() {
    c.collect(i("-a - b"));
    azzert.that(c.plus, iz("[]"));
    azzert.that(c.minus, iz("[a,b]"));
  }

  @Test public void test_33() {
    c.collect(i("-a - (-b)"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_34() {
    c.collect(i("-a - ((-b))"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_35() {
    c.collect(i("-a - +((-b))"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_36() {
    c.collect(i("-a - ((-3))"));
    azzert.that(c.plus, iz("[3]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_37() {
    c.collect(i("+-a - b"));
    azzert.that(c.plus, iz("[]"));
    azzert.that(c.minus, iz("[a,b]"));
  }

  @Test public void test_38() {
    c.collect(i("+(-a) - (-b)"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_39() {
    c.collect(i("+(+(-a)) - ((-b))"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_40() {
    c.collect(i("-(-(-a)) - +((-b))"));
    azzert.that(c.plus, iz("[b]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_41() {
    c.collect(i("-(-(-(+(-(+-(+a)))))) - ((-3))"));
    azzert.that(c.plus, iz("[3]"));
    azzert.that(c.minus, iz("[a]"));
  }

  @Test public void test_42() {
    c.collect(i("-a + + - - - (b -c)"));
    azzert.that(c.minus, iz("[a,b]"));
  }
  @Test public void test_43() {
    c.collect(i("-a + + - - - (b -c)"));
    azzert.that(c.plus, iz("[c]"));
  }

  private final TermsCollector c = new TermsCollector();
}

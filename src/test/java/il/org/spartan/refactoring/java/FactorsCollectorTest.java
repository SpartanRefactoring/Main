package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

@SuppressWarnings("static-method") public class FactorsCollectorTest {
  @Test public void test00() {
    azzert.that(c.collect(i("a*b")), is(c));
  }

  @Test public void test01() {
    azzert.aye(FactorsCollector.isLeafFactor(e("i")))//
        .andAye(FactorsCollector.isLeafFactor(e("i+j"))) //
        .andAye(FactorsCollector.isLeafFactor(e("(x)")));
  }

  @Test public void test02() {
    azzert.aye(FactorsCollector.isLeafFactor(e("(i+j)")));
    azzert.aye(FactorsCollector.isLeafFactor(e("(i*j)")));
    azzert.nay(FactorsCollector.isLeafFactor(e("i*j")));
    azzert.nay(FactorsCollector.isLeafFactor(e("i/j")));
  }

  @Test public void test03() {
    final InfixExpression i = i("a * b * c");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(3));
    azzert.that(c.dividers().size(), is(0));
  }

  @Test public void test04() {
    final InfixExpression i = i("a/c");
    azzert.that(i.getOperator(), is(wizard.DIVIDE));
    azzert.that(step.left(i), iz("a"));
    azzert.that(step.right(i), iz("c"));
    c.collect(i);
    azzert.that(c.multipliers().size(), is(1));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test05() {
    final InfixExpression i = i("a/c");
    azzert.that(i.getOperator(), is(wizard.DIVIDE));
    azzert.that(step.left(i), iz("a"));
    azzert.that(step.right(i), iz("c"));
    c.collectTimesNonLeaf(i);
    azzert.that(c.multipliers().size(), is(1));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test06() {
    final InfixExpression i = i("a * b / c");
    azzert.that(i.getOperator(), is(wizard.DIVIDE));
    azzert.that(az.infixExpression(step.left(i)).getOperator(), is(wizard.TIMES));
    c.collect(i);
    azzert.that(c.multipliers().size(), is(2));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test07() {
    final InfixExpression i = i("a * (b / c)");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(2));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test08() {
    final InfixExpression i = i("a * (b * (d * c))");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(4));
    azzert.that(c.dividers().size(), is(0));
  }

  @Test public void test09() {
    final InfixExpression i = i("a / (b / c / (d / e / f / g))");
    c.collect(i);
    azzert.that(c.multipliers(), iz("[a,c,d]"));
    azzert.that(c.dividers(), iz("[b,e,f,g]"));
  }

  @Test public void test10() {
    final InfixExpression i = i("a / (b / c / d / e )");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(4));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test11() {
    final InfixExpression i = i("a / (b / c)");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(2));
  }

  @Test public void test12() {
    final InfixExpression i = i("a / (b / c)");
    c.collect(i);
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test13() {
    final InfixExpression i = i("a / (b / c)");
    c.collect(i);
    azzert.that(c.dividers(), iz("[b]"));
  }

  @Test public void test14() {
    final InfixExpression i = i("a / (b / c)");
    c.collect(i);
    azzert.that(c.multipliers(), iz("[a,c]"));
  }

  @Test public void test15() {
    final InfixExpression i = i("(a * b) * (c * (d / (e * (f / g))))");
    c.collect(i);
    azzert.that(c.multipliers(), iz("[a,b,c,d,g]"));
    azzert.that(c.dividers(), iz("[e,f]"));
  }

  @Test public void test16() {
    final InfixExpression i = i("a * (b * c)");
    c.collect(i);
    azzert.that(c.multipliers(), iz("[a,b,c]"));
  }

  @Test public void test17() {
    final InfixExpression i = i("a * (b * c)");
    c.collect(i);
    azzert.that(c.dividers(), iz("[]"));
  }

  @Test public void test18() {
    final InfixExpression i = i("a * (b / c)");
    c.collect(i);
    azzert.that(c.multipliers(), iz("[a,b]"));
  }

  @Test public void test19() {
    final InfixExpression i = i("a * (b / c)");
    c.collect(i);
    azzert.that(c.dividers(), iz("[c]"));
  }

  @Test public void test20() {
    final InfixExpression i = i("a * (b / c)");
    c.collect(i);
    azzert.that(c.dividers(), iz("[c]"));
  }

  @Test public void test21() {
    c.collect(null);
    azzert.aye(c.dividers().isEmpty());
    azzert.aye(c.multipliers().isEmpty());
  }

  @Test public void test22() {
    c.collect(i("i+j"));
    azzert.aye(c.dividers().isEmpty());
    azzert.aye(c.multipliers().isEmpty());
  }

  @Ignore("no similar case fo multification") @Test public void test23() {
    c.collect(i("+a - b"));
    azzert.that(c.multipliers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test24() {
    c.collect(i("-a + b"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Test public void test25() {
    azzert.that(core(e("(a)")), iz("a"));
  }

  @Test public void test26() {
    azzert.that(core(e("((a))")), iz("a"));
  }

  @Test public void test27() {
    azzert.that(core(e("+a")), iz("a"));
  }

  @Test public void test28() {
    azzert.that(core(e(" + +a")), iz("a"));
  }

  @Test public void test29() {
    azzert.that(core(e(" + (+a)")), iz("a"));
  }

  @Test public void test30() {
    azzert.that(core(e(" +(+ (+a))")), iz("a"));
  }

  // TODO: should probably simplify -+-+-+-+-+(a) to -a
  @Test public void test31() {
    c.collect(i("-+  -+ -+-+-+(a) * b"));
    azzert.that(c.multipliers(), iz("[-+-+-+-+-+(a),b]"));
    azzert.that(c.dividers(), iz("[]"));
  }

  @Ignore("no similar case fo multification") @Test public void test32() {
    c.collect(i("-a - b"));
    azzert.that(c.multipliers(), iz("[]"));
    azzert.that(c.dividers(), iz("[a,b]"));
  }

  @Ignore("no similar case fo multification") @Test public void test33() {
    c.collect(i("-a - (-b)"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test34() {
    c.collect(i("-a - ((-b))"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test35() {
    c.collect(i("-a - +((-b))"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test36() {
    c.collect(i("-a - ((-3))"));
    azzert.that(c.multipliers(), iz("[3]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test37() {
    c.collect(i("+-a - b"));
    azzert.that(c.multipliers(), iz("[]"));
    azzert.that(c.dividers(), iz("[a,b]"));
  }

  @Ignore("no similar case fo multification") @Test public void test38() {
    c.collect(i("+(-a) - (-b)"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test39() {
    c.collect(i("+(+(-a)) - ((-b))"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test40() {
    c.collect(i("-(-(-a)) - +((-b))"));
    azzert.that(c.multipliers(), iz("[b]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test41() {
    c.collect(i("-(-(-(+(-(+-(+a)))))) - ((-3))"));
    azzert.that(c.multipliers(), iz("[3]"));
    azzert.that(c.dividers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test42() {
    c.collect(i("-a + + - - - (b -c)"));
    azzert.that(c.dividers(), iz("[a,b]"));
  }

  @Ignore("no similar case fo multification") @Test public void test43() {
    c.collect(i("-a + + - - - (b -c)"));
    azzert.that(c.multipliers(), iz("[c]"));
  }

  @Ignore("no similar case fo multification") @Test public void test44() {
    c.collect(i("-a- b -c"));
    azzert.that(c.multipliers(), iz("[]"));
    azzert.that(c.dividers(), iz("[a,b,c]"));
  }

  @Ignore("no similar case fo multification") @Test public void test45() {
    c.collect(i("-a- b"));
    azzert.that(c.multipliers(), iz("[]"));
    azzert.that(c.dividers(), iz("[a,b]"));
  }

  @Test public void test46() {
    c.collect(i("a / (b+c)"));
    azzert.that(c.multipliers(), iz("[a]"));
  }

  @Ignore("no similar case fo multification") @Test public void test47() {
    c.collect(i("a / (b+c)"));
    azzert.that(c.dividers(), iz("[b+c]"));
  }

  @Test public void test48() {
    c.collect(i("a * (b+c)"));
    azzert.that(c.multipliers(), iz("[a,b+c]"));
  }

  @Test public void test49() {
    c.collect(i("a + (b+c)"));
    azzert.that(c.dividers(), iz("[]"));
  }

  @Ignore("no similar case fo multification") @Test public void test50() {
    azzert.that(minus.peel(e("a*b")), iz("a*b"));
  }

  @Ignore("no similar case fo multification") @Test public void test51() {
    c.collect(i("(a+b)*(b+c)"));
    azzert.that(c.multipliers(), iz("[a+b,b+c]"));
  }

  @Test public void test52() {
    c.collect(i("(a+b)*(b+c)"));
    azzert.that(c.dividers(), iz("[]"));
  }

  @Test public void test53() {
    c.collect(i("(a+b)/(b+c)"));
    azzert.that(c.multipliers(), iz("[a+b]"));
  }

  @Test public void test54() {
    c.collect(i("(a+b)/(b+c)"));
    azzert.that(c.dividers(), iz("[b+c]"));
  }

  private final InfixExpression complex = i("a-b*c - (x - - - (d*e))");

  @Ignore("need to make a new complex for Factors") @Test public void test55() {
    c.collect(complex);
    azzert.that("" + c.multipliers(), containsString("a"));
  }

  @Ignore("need to make a new complex for Factors") @Test public void test56() {
    c.collect(complex);
    azzert.that("" + c.multipliers(), containsString("d"));
  }

  @Ignore("need to make a new complex for Factors") @Test public void test57() {
    c.collect(complex);
    azzert.that("" + c.multipliers(), containsString("e"));
  }

  @Ignore("need to make a new complex for Factors") @Test public void test58() {
    c.collect(complex);
    azzert.that("" + c.multipliers(), containsString("d * e"));
  }

  @Ignore("need to make a new complex for Factors") @Test public void test59() {
    c.collect(complex);
    azzert.that("" + c.dividers(), containsString("b * c"));
  }

  @Ignore("need to make a new complex for Factors") @Test public void test60() {
    c.collect(complex);
    azzert.that("" + c.multipliers(), iz("[a,d*e]"));
  }

  @Ignore("need to make a new complex for Factors") @Test public void test61() {
    c.collect(complex);
    azzert.that("" + c.dividers(), iz("[b*c,x]"));
  }

  private final FactorsCollector c = new FactorsCollector();
}

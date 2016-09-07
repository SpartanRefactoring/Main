package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

@SuppressWarnings("static-method") public class FactorsCollectorTest {
  private final InfixExpression complex = i("a-b*c - (x - - - (d*e))");
  private final FactorsCollector c = new FactorsCollector();

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
    azzert.that(i.getOperator(), is(DIVIDE));
    azzert.that(step.left(i), iz("a"));
    azzert.that(step.right(i), iz("c"));
    c.collect(i);
    azzert.that(c.multipliers().size(), is(1));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test05() {
    final InfixExpression i = i("a/c");
    azzert.that(i.getOperator(), is(DIVIDE));
    azzert.that(step.left(i), iz("a"));
    azzert.that(step.right(i), iz("c"));
    c.collectTimesNonLeaf(i);
    azzert.that(c.multipliers().size(), is(1));
    azzert.that(c.dividers().size(), is(1));
  }

  @Test public void test06() {
    final InfixExpression i = i("a * b / c");
    azzert.that(i.getOperator(), is(DIVIDE));
    azzert.that(az.infixExpression(step.left(i)).getOperator(), is(TIMES));
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

  @Test public void test31() {
    c.collect(i("-+  -+ -+-+-+(a) * b"));
    azzert.that(c.multipliers(), iz("[-a,b]"));
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

  @Test public void test47() {
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

  @Test public void test50() {
    azzert.that(minus.peel(e("a*b")), iz("a*b"));
  }

  @Test public void test51() {
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

  @Test public void test62() {
    c.collect(i("(a/b)*c"));
    azzert.that(c.dividers(), iz("[b]"));
    azzert.that(c.multipliers(), iz("[a,c]"));
  }

  @Test public void test63() {
    azzert.aye(wizard.same(new Factor(false, e("x")).asExpression(), e("x")));
  }

  @Test public void test64() {
    azzert.aye(wizard.same(new Factor(false, e("x*3+5")).asExpression(), e("x*3+5")));
  }

  @Test public void test65() {
    azzert.aye(wizard.same(new Factor(false, e("1/x")).asExpression(), e("1/x")));
  }

  @Test public void test66() {
    azzert.aye(wizard.same(new Factor(false, e("17.5-x/2-3/2*(17/3/y/z)*k")).asExpression(), e("17.5-x/2-3/2*(17/3/y/z)*k")));
  }

  @Test public void test67() {
    azzert.aye(wizard.same(new Factor(true, e("x")).asExpression(), e("1/x")));
  }

  @Test public void test68() {
    azzert.aye(wizard.same(new Factor(true, e("17.5-x/2-3/2*(17/3/y/z)*k")).asExpression(), e("1/(17.5-x/2-3/2*(17/3/y/z)*k)")));
  }

  @Test public void test69() {
    azzert.aye(wizard.same(new Factor(true, e("u/w/x/y/z")).asExpression(), e("1/(u/w/x/y/z)")));
  }

  @Test public void test70() {
    azzert.aye(FactorsCollector.isLeafFactor(e("3+5+7+9")));
    azzert.aye(FactorsCollector.isLeafFactor(e("(5/6/7/8/9/10)")));
  }

  @Test public void test71() {
    final InfixExpression i = i("a * b * c/d/e/f/g");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(3));
    azzert.that(c.dividers().size(), is(4));
  }

  @Test public void test72() {
    final InfixExpression i = i("a/d*b/e*c/f");
    c.collectTimesNonLeaf(i);
    azzert.that(c.multipliers().size(), is(3));
    azzert.that(c.dividers().size(), is(3));
  }

  @Test public void test73() {
    final InfixExpression i = i("a * (b*(c*d/e) /(f*g/h) )");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(5));
    azzert.that(c.dividers().size(), is(3));
  }

  @Test public void test74() {
    final InfixExpression i = i("a * (b * (d *(x*y)*(s*t*(u*w))* c*(y*z)*(y*z)))");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(14));
    azzert.that(c.dividers().size(), is(0));
  }

  @Test public void test75() {
    final InfixExpression i = i("a*b / (b / c / (d / e / f / g))");
    c.collect(i);
    azzert.that(c.multipliers(), iz("[a,b,c,d]"));
    azzert.that(c.dividers(), iz("[b,e,f,g]"));
  }

  @Test public void test76() {
    final InfixExpression i = i("a / (b / c / d / e )");
    c.collect(i);
    azzert.that(c.multipliers().size(), is(4));
    azzert.that(c.dividers().size(), is(1));
  }
}

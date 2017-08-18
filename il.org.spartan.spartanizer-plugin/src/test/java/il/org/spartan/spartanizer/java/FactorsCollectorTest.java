package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

@SuppressWarnings("static-method") //
public final class FactorsCollectorTest {
  final InfixExpression complex = i("a-b*c - (x - - - (d*e))");
  private final FactorsCollector c = new FactorsCollector();

  @Test public void test00() {
    azzert.that(c.collect(i("a*b")), is(c));
  }
  @Test public void test01() {
    assert FactorsCollector.isLeafFactor(e("i"));
    assert FactorsCollector.isLeafFactor(e("i+j")); //
    assert FactorsCollector.isLeafFactor(e("(x)"));
  }
  @Test public void test02() {
    assert FactorsCollector.isLeafFactor(e("(i+j)"));
    assert FactorsCollector.isLeafFactor(e("(i*j)"));
    assert !FactorsCollector.isLeafFactor(e("i*j"));
    assert !FactorsCollector.isLeafFactor(e("i/j"));
  }
  @Test public void test03() {
    c.collect(i("a * b * c"));
    azzert.that(c.multipliers().size(), is(3));
    azzert.that(c.dividers().size(), is(0));
  }
  @Test public void test04() {
    final InfixExpression i = i("a/c");
    azzert.that(i.getOperator(), is(DIVIDE));
    azzert.that(left(i), iz("a"));
    azzert.that(right(i), iz("c"));
    c.collect(i);
    azzert.that(c.multipliers().size(), is(1));
    azzert.that(c.dividers().size(), is(1));
  }
  @Test public void test05() {
    final InfixExpression i = i("a/c");
    azzert.that(i.getOperator(), is(DIVIDE));
    azzert.that(left(i), iz("a"));
    azzert.that(right(i), iz("c"));
    c.collectTimesNonLeaf(i);
    azzert.that(c.multipliers().size(), is(1));
    azzert.that(c.dividers().size(), is(1));
  }
  @Test public void test06() {
    final InfixExpression i = i("a * b / c");
    azzert.that(i.getOperator(), is(DIVIDE));
    azzert.that(az.infixExpression(left(i)).getOperator(), is(TIMES));
    c.collect(i);
    azzert.that(c.multipliers().size(), is(2));
    azzert.that(c.dividers().size(), is(1));
  }
  @Test public void test07() {
    c.collect(i("a * (b / c)"));
    azzert.that(c.multipliers().size(), is(2));
    azzert.that(c.dividers().size(), is(1));
  }
  @Test public void test08() {
    c.collect(i("a * (b * (d * c))"));
    azzert.that(c.multipliers().size(), is(4));
    azzert.that(c.dividers().size(), is(0));
  }
  @Test public void test09() {
    c.collect(i("a / (b / c / (d / e / f / g))"));
    azzert.that(c.multipliers(), iz("[a,c,d]"));
    azzert.that(c.dividers(), iz("[b,e,f,g]"));
  }
  @Test public void test10() {
    c.collect(i("a / (b / c / d / e )"));
    azzert.that(c.multipliers().size(), is(4));
    azzert.that(c.dividers().size(), is(1));
  }
  @Test public void test11() {
    c.collect(i("a / (b / c)"));
    azzert.that(c.multipliers().size(), is(2));
  }
  @Test public void test12() {
    c.collect(i("a / (b / c)"));
    azzert.that(c.dividers().size(), is(1));
  }
  @Test public void test13() {
    c.collect(i("a / (b / c)"));
    azzert.that(c.dividers(), iz("[b]"));
  }
  @Test public void test14() {
    c.collect(i("a / (b / c)"));
    azzert.that(c.multipliers(), iz("[a,c]"));
  }
  @Test public void test15() {
    c.collect(i("(a * b) * (c * (d / (e * (f / g))))"));
    azzert.that(c.multipliers(), iz("[a,b,c,d,g]"));
    azzert.that(c.dividers(), iz("[e,f]"));
  }
  @Test public void test16() {
    c.collect(i("a * (b * c)"));
    azzert.that(c.multipliers(), iz("[a,b,c]"));
  }
  @Test public void test17() {
    c.collect(i("a * (b * c)"));
    azzert.that(c.dividers(), iz("[]"));
  }
  @Test public void test18() {
    c.collect(i("a * (b / c)"));
    azzert.that(c.multipliers(), iz("[a,b]"));
  }
  @Test public void test19() {
    c.collect(i("a * (b / c)"));
    azzert.that(c.dividers(), iz("[c]"));
  }
  @Test public void test20() {
    c.collect(i("a * (b / c)"));
    azzert.that(c.dividers(), iz("[c]"));
  }
  @Test public void test21() {
    c.collect(null);
    assert c.dividers().isEmpty();
    assert c.multipliers().isEmpty();
  }
  @Test public void test22() {
    c.collect(i("i+j"));
    assert c.dividers().isEmpty();
    assert c.multipliers().isEmpty();
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
  @Test public void test62() {
    c.collect(i("(a/b)*c"));
    azzert.that(c.dividers(), iz("[b]"));
    azzert.that(c.multipliers(), iz("[a,c]"));
  }
  @Test public void test63() {
    assert wizard.eq(new Factor(false, e("x")).asExpression(), e("x"));
  }
  @Test public void test64() {
    assert wizard.eq(new Factor(false, e("x*3+5")).asExpression(), e("x*3+5"));
  }
  @Test public void test65() {
    assert wizard.eq(new Factor(false, e("1/x")).asExpression(), e("1/x"));
  }
  @Test public void test66() {
    assert wizard.eq(new Factor(false, e("17.5-x/2-3/2*(17/3/y/z)*k")).asExpression(), e("17.5-x/2-3/2*(17/3/y/z)*k"));
  }
  @Test public void test67() {
    assert wizard.eq(new Factor(true, e("x")).asExpression(), e("1/x"));
  }
  @Test public void test68() {
    assert wizard.eq(new Factor(true, e("17.5-x/2-3/2*(17/3/y/z)*k")).asExpression(), e("1/(17.5-x/2-3/2*(17/3/y/z)*k)"));
  }
  @Test public void test69() {
    assert wizard.eq(new Factor(true, e("u/w/x/y/z")).asExpression(), e("1/(u/w/x/y/z)"));
  }
  @Test public void test70() {
    assert FactorsCollector.isLeafFactor(e("3+5+7+9"));
    assert FactorsCollector.isLeafFactor(e("(5/6/7/8/9/10)"));
  }
  @Test public void test71() {
    c.collect(i("a * b * c/d/e/f/g"));
    azzert.that(c.multipliers().size(), is(3));
    azzert.that(c.dividers().size(), is(4));
  }
  @Test public void test72() {
    c.collectTimesNonLeaf(i("a/d*b/e*c/f"));
    azzert.that(c.multipliers().size(), is(3));
    azzert.that(c.dividers().size(), is(3));
  }
  @Test public void test73() {
    c.collect(i("a * (b*(c*d/e) /(f*g/h) )"));
    azzert.that(c.multipliers().size(), is(5));
    azzert.that(c.dividers().size(), is(3));
  }
  @Test public void test74() {
    c.collect(i("a * (b * (d *(x*y)*(s*tipper*(u*w))* c*(y*z)*(y*z)))"));
    azzert.that(c.multipliers().size(), is(14));
    azzert.that(c.dividers().size(), is(0));
  }
  @Test public void test75() {
    c.collect(i("a*b / (b / c / (d / e / f / g))"));
    azzert.that(c.multipliers(), iz("[a,b,c,d]"));
    azzert.that(c.dividers(), iz("[b,e,f,g]"));
  }
  @Test public void test76() {
    c.collect(i("a / (b / c / d / e )"));
    azzert.that(c.multipliers().size(), is(4));
    azzert.that(c.dividers().size(), is(1));
  }
}

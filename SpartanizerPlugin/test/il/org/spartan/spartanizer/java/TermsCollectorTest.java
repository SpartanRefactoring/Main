package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

@SuppressWarnings("static-method")
public final class TermsCollectorTest {
  private final InfixExpression complex = i("a-b*c - (x - - - (d*e))");
  private final TermsCollector c = new TermsCollector();

  @Test public void test00() {
    azzert.that(c.collect(i("a*b")), is(c));
  }

  @Test public void test01() {
    assert TermsCollector.isLeafTerm(e("i"));//
    assert TermsCollector.isLeafTerm(e("i*j")); //
    assert TermsCollector.isLeafTerm(e("(x)"));
  }

  @Test public void test02() {
    assert TermsCollector.isLeafTerm(e("(i*j)"));
    assert TermsCollector.isLeafTerm(e("(i+j)"));
    assert !TermsCollector.isLeafTerm(e("i+j"));
    assert !TermsCollector.isLeafTerm(e("i-j"));
  }

  @Test public void test03() {
    c.collect(i("a + b +c"));
    azzert.that(c.plus().size(), is(3));
    azzert.that(c.minus().size(), is(0));
  }

  @Test public void test04() {
    final InfixExpression i = i("a-c");
    azzert.that(i.getOperator(), is(il.org.spartan.spartanizer.ast.navigate.op.MINUS2));
    azzert.that(left(i), iz("a"));
    azzert.that(right(i), iz("c"));
    c.collect(i);
    azzert.that(c.plus().size(), is(1));
    azzert.that(c.minus().size(), is(1));
  }

  @Test public void test05() {
    final InfixExpression i = i("a-c");
    azzert.that(i.getOperator(), is(il.org.spartan.spartanizer.ast.navigate.op.MINUS2));
    azzert.that(left(i), iz("a"));
    azzert.that(right(i), iz("c"));
    c.collectPlusNonLeaf(i);
    azzert.that(c.plus().size(), is(1));
    azzert.that(c.minus().size(), is(1));
  }

  @Test public void test06() {
    final InfixExpression i = i("a + b -c");
    azzert.that(i.getOperator(), is(il.org.spartan.spartanizer.ast.navigate.op.MINUS2));
    azzert.that(az.infixExpression(left(i)).getOperator(), is(il.org.spartan.spartanizer.ast.navigate.op.PLUS2));
    c.collect(i);
    azzert.that(c.plus().size(), is(2));
    azzert.that(c.minus().size(), is(1));
  }

  @Test public void test07() {
    c.collect(i("a + (b -c)"));
    azzert.that(c.plus().size(), is(2));
    azzert.that(c.minus().size(), is(1));
  }

  @Test public void test08() {
    c.collect(i("a + (b +(d + c))"));
    azzert.that(c.plus().size(), is(4));
    azzert.that(c.minus().size(), is(0));
  }

  @Test public void test09() {
    c.collect(i("a - (b - c - (d - e - f - g))"));
    azzert.that(c.plus(), iz("[a,c,d]"));
    azzert.that(c.minus(), iz("[b,e,f,g]"));
  }

  @Test public void test10() {
    c.collect(i("a - (b - c - d - e )"));
    azzert.that(c.plus().size(), is(4));
    azzert.that(c.minus().size(), is(1));
  }

  @Test public void test11() {
    c.collect(i("a - (b - c)"));
    azzert.that(c.plus().size(), is(2));
  }

  @Test public void test12() {
    c.collect(i("a - (b - c)"));
    azzert.that(c.minus().size(), is(1));
  }

  @Test public void test13() {
    c.collect(i("a - (b - c)"));
    azzert.that(c.minus(), iz("[b]"));
  }

  @Test public void test14() {
    c.collect(i("a - (b - c)"));
    azzert.that(c.minus(), iz("[b]"));
  }

  @Test public void test15() {
    c.collect(i("(a + b) + (c+(d-(e+(f-g))))"));
    azzert.that(c.plus(), iz("[a,b,c,d,g]"));
    azzert.that(c.minus(), iz("[e,f]"));
  }

  @Test public void test16() {
    c.collect(i("a + (b + c)"));
    azzert.that(c.plus(), iz("[a,b,c]"));
    azzert.that(c.minus(), iz("[]"));
  }

  @Test public void test17() {
    c.collect(i("a + (b + c)"));
    azzert.that(c.minus(), iz("[]"));
  }

  @Test public void test18() {
    c.collect(i("a + (b - c)"));
    azzert.that(c.plus(), iz("[a,b]"));
  }

  @Test public void test19() {
    c.collect(i("a + (b - c)"));
    azzert.that(c.minus(), iz("[c]"));
  }

  @Test public void test20() {
    c.collect(i("a + (b - c)"));
    azzert.that(c.minus(), iz("[c]"));
  }

  @Test public void test21() {
    c.collect(null);
    assert c.minus().isEmpty();
    assert c.plus().isEmpty();
  }

  @Test public void test22() {
    c.collect(i("i*j"));
    assert c.minus().isEmpty();
    assert c.plus().isEmpty();
  }

  @Test public void test23() {
    c.collect(i("+a - b"));
    azzert.that(c.plus(), iz("[a]"));
  }

  @Test public void test24() {
    c.collect(i("-a + b"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
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
    c.collect(i("-+  -+ -+-+-+(a) + b"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test32() {
    c.collect(i("-a - b"));
    azzert.that(c.plus(), iz("[]"));
    azzert.that(c.minus(), iz("[a,b]"));
  }

  @Test public void test33() {
    c.collect(i("-a - (-b)"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test34() {
    c.collect(i("-a - ((-b))"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test35() {
    c.collect(i("-a - +((-b))"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test36() {
    c.collect(i("-a - ((-3))"));
    azzert.that(c.plus(), iz("[3]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test37() {
    c.collect(i("+-a - b"));
    azzert.that(c.plus(), iz("[]"));
    azzert.that(c.minus(), iz("[a,b]"));
  }

  @Test public void test38() {
    c.collect(i("+(-a) - (-b)"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test39() {
    c.collect(i("+(+(-a)) - ((-b))"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test40() {
    c.collect(i("-(-(-a)) - +((-b))"));
    azzert.that(c.plus(), iz("[b]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test41() {
    c.collect(i("-(-(-(+(-(+-(+a)))))) - ((-3))"));
    azzert.that(c.plus(), iz("[3]"));
    azzert.that(c.minus(), iz("[a]"));
  }

  @Test public void test42() {
    c.collect(i("-a + + - - - (b -c)"));
    azzert.that(c.minus(), iz("[a,b]"));
  }

  @Test public void test43() {
    c.collect(i("-a + + - - - (b -c)"));
    azzert.that(c.plus(), iz("[c]"));
  }

  @Test public void test44() {
    c.collect(i("-a- b -c"));
    azzert.that(c.plus(), iz("[]"));
    azzert.that(c.minus(), iz("[a,b,c]"));
  }

  @Test public void test45() {
    c.collect(i("-a- b"));
    azzert.that(c.plus(), iz("[]"));
    azzert.that(c.minus(), iz("[a,b]"));
  }

  @Test public void test46() {
    c.collect(i("a- b*c"));
    azzert.that(c.plus(), iz("[a]"));
  }

  @Test public void test47() {
    c.collect(i("a- b*c"));
    azzert.that(c.minus(), iz("[b*c]"));
  }

  @Test public void test48() {
    c.collect(i("a+ b*c"));
    azzert.that(c.plus(), iz("[a,b*c]"));
  }

  @Test public void test49() {
    c.collect(i("a+ b*c"));
    azzert.that(c.minus(), iz("[]"));
  }

  @Test public void test50() {
    azzert.that(minus.peel(e("a*b")), iz("a*b"));
  }

  @Test public void test51() {
    c.collect(i("a*b+b*c"));
    azzert.that(c.plus(), iz("[a*b,b*c]"));
  }

  @Test public void test52() {
    c.collect(i("a*b+b*c"));
    azzert.that(c.minus(), iz("[]"));
  }

  @Test public void test53() {
    c.collect(i("a*b-b*c"));
    azzert.that(c.plus(), iz("[a*b]"));
  }

  @Test public void test54() {
    c.collect(i("a*b-b*c"));
    azzert.that(c.minus(), iz("[b*c]"));
  }

  @Test public void test55() {
    c.collect(complex);
    azzert.that(c.plus() + "", containsString("a"));
  }

  @Test public void test56() {
    c.collect(complex);
    azzert.that(c.plus() + "", containsString("d"));
  }

  @Test public void test57() {
    c.collect(complex);
    azzert.that(c.plus() + "", containsString("e"));
  }

  @Test public void test58() {
    c.collect(complex);
    azzert.that(c.plus() + "", containsString("d * e"));
  }

  @Test public void test59() {
    c.collect(complex);
    azzert.that(c.minus() + "", containsString("b * c"));
  }

  @Test public void test60() {
    c.collect(complex);
    azzert.that(c.plus() + "", iz("[a,d*e]"));
  }

  @Test public void test61() {
    c.collect(complex);
    azzert.that(c.minus() + "", iz("[b*c,x]"));
  }
}

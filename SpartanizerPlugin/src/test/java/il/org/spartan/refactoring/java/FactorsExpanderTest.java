package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class FactorsExpanderTest {
  @Test public void test00() {
    azzert.that(FactorsExpander.simplify(i("a/b")), iz("a/b"));
  }

  @Test public void test01() {
    azzert.that(FactorsExpander.simplify(i("a*b*c")), iz("a * b * c"));
  }

  @Test public void test02() {
    azzert.that(FactorsExpander.simplify(i("a/c")), iz("a/c"));
  }

  @Test public void test03() {
    azzert.that(FactorsExpander.simplify(i("a * (b*c)")), iz("a * b*c"));
  }

  @Test public void test04() {
    azzert.that(FactorsExpander.simplify(i("a * (b/c)")), iz("a * b/c"));
  }

  @Test public void test05() {
    azzert.that(FactorsExpander.simplify(i("a * b /c * d /e * f")), iz("a * b /c * d /e * f"));
  }

  @Test public void test06() {
    azzert.that(FactorsExpander.simplify(i("a * (b * c * d)")), iz("a * b * c * d"));
  }

  @Test public void test07() {
    azzert.that(FactorsExpander.simplify(i("a / (c /d)")), iz("a / c * d"));
  }

  @Test public void test08() {
    azzert.that(FactorsExpander.simplify(i("a * (b+c)")), iz("a * (b+c)"));
  }

  @Ignore("not implemented yet") @Test public void test09() {
    azzert.that(FactorsExpander.simplify(i("+a + +a")), iz("a + a"));
  }

  @Ignore("no similar case fo multification") @Test public void test10() {
    azzert.that(FactorsExpander.simplify(i("a - -b")), iz("a + b"));
  }

  @Ignore("no similar case fo multification") @Test public void test11() {
    azzert.that(FactorsExpander.simplify(i("a + - - (b + c)")), iz("a + b + c"));
  }

  @Ignore("no similar case fo multification") @Test public void test12() {
    azzert.that(FactorsExpander.simplify(i("a + + - - - (b -c)")), iz("a -b +c"));
  }

  @Ignore("no similar case fo multification") @Test public void test13() {
    azzert.that(FactorsExpander.simplify(i("-a + + - - - (b -c)")), iz("-a -b +c"));
  }

  @Ignore("no similar case fo multification") @Test public void test14() {
    azzert.that(FactorsExpander.simplify(i("-a + + - - - (b -(-c))")), iz(" -a -b-c"));
  }

  @Test public void test15() {
    azzert.that(FactorsExpander.simplify(i("1/a/b")), iz("1/a/b"));
  }

  @Test public void test16() {
    azzert.that(FactorsExpander.simplify(i("x/a/b")), iz("x/a/b"));
  }

  @Ignore("not implemented yet") @Test public void test17() {
    azzert.that(FactorsExpander.simplify(i("x-a-(-((-b)) - (((-(((-(y))))))))")), iz("x-a-b+y"));
  }

  @Ignore("not implemented yet") @Test public void test18() {
    azzert.that(FactorsExpander.simplify(i("(-(x-a))-+ + (y -b)")), iz("a-x-y+b"));
  }

  @Ignore("not implemented yet") @Test public void test19() {
    azzert.that(FactorsExpander.simplify(i("a-b*c - (x - - - (d*e))")), //
        iz("a - b*c -x + d*e"));
  }

  @Test public void test20() {
    azzert.that(FactorsExpander.simplify(i("a/(b+c)")), iz("a / (b+c)"));
  }

  @Ignore("not implemeted yet") @Test public void test21() {
    azzert.that(FactorsExpander.simplify(i("1/a*b")), iz("b/a"));
  }

  @Test public void test22() {
    azzert.that(FactorsExpander.simplify(i("1/a/b")), iz("1/a/b"));
  }
  
  @Test public void test23() {
    azzert.that(FactorsExpander.simplify(i("a * (b/c/d/e)")), iz("a * b/c/d/e"));
  }
  
  @Test public void test24() {
    azzert.that(FactorsExpander.simplify(i("a * ((b*x)/(c*y)/d/e)")), iz("a * b*x/c/y/d/e"));
  }
  
  @Test public void test25() {
    azzert.that(FactorsExpander.simplify(i("a * ((b*x)/c*y/d/e)")), iz("a * b*x/c*y/d/e"));
  }
}
package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class FactorsReogranizerTest {
  @Test public void test00() {
    azzert.that(FactorsReorganizer.simplify(i("a/b")), iz("a/b"));
  }

  @Test public void test01() {
    azzert.that(FactorsReorganizer.simplify(i("a*b*c")), iz("a * b * c"));
  }

  @Test public void test02() {
    azzert.that(FactorsReorganizer.simplify(i("a/c")), iz("a/c"));
  }

  @Test public void test03() {
    azzert.that(FactorsReorganizer.simplify(i("a * (b*c)")), iz("a * b*c"));
  }

  @Test public void test04() {
    azzert.that(FactorsReorganizer.simplify(i("a * (b/c)")), iz("a * b/c"));
  }

  @Test public void test05() {
    azzert.that(FactorsReorganizer.simplify(i("a * b /c * d /e * f")), iz("a * b * d * f /c /e"));
  }

  @Test public void test06() {
    azzert.that(FactorsReorganizer.simplify(i("a * (b * c * d)")), iz("a * b * c * d"));
  }

  @Test public void test07() {
    azzert.that(FactorsReorganizer.simplify(i("a / (c /d)")), iz("a * d /c"));
  }

  @Ignore("no similar case fo multification") @Test public void test08() {
    azzert.that(FactorsReorganizer.simplify(i("a + -a")), iz("a - a"));
  }

  @Ignore("no similar case fo multification") @Test public void test09() {
    azzert.that(FactorsReorganizer.simplify(i("+a + +a")), iz("a + a"));
  }

  @Ignore("no similar case fo multification") @Test public void test10() {
    azzert.that(FactorsReorganizer.simplify(i("a - -b")), iz("a + b"));
  }

  @Ignore("no similar case fo multification") @Test public void test11() {
    azzert.that(FactorsReorganizer.simplify(i("a + - - (b + c)")), iz("a + b + c"));
  }

  @Ignore("no similar case fo multification") @Test public void test12() {
    azzert.that(FactorsReorganizer.simplify(i("a + + - - - (b -c)")), iz("a +c -b"));
  }

  @Ignore("no similar case fo multification") @Test public void test13() {
    azzert.that(FactorsReorganizer.simplify(i("-a + + - - - (b -c)")), iz("c -a -b"));
  }

  @Ignore("no similar case fo multification") @Test public void test14() {
    azzert.that(FactorsReorganizer.simplify(i("-a + + - - - (b -(-c))")), iz(" -a -b-c"));
  }

  @Ignore("no similar case fo multification") @Test public void test15() {
    azzert.that(FactorsReorganizer.simplify(i("-a-b")), iz("-a-b"));
  }

  @Test public void test16() {
    azzert.that(FactorsReorganizer.simplify(i("x/a/b")), iz("x/a/b"));
  }

  @Test public void test17() {
    azzert.that(FactorsReorganizer.simplify(i("x/a/b * y")), iz("x * y/a/b"));
  }

  @Ignore("no similar case fo multification") @Test public void test18() {
    azzert.that(FactorsReorganizer.simplify(i("(-(x-a))-+ + (y -b)")), iz("a + b-x-y"));
  }

  @Ignore("no similar case fo multification") @Test public void test19() {
    azzert.that(FactorsReorganizer.simplify(i("a-b*c - (x - - - (d*e))")), //
        iz("a + d*e - b*c -x"));
  }

  @Test public void test20() {
    azzert.that(FactorsReorganizer.simplify(i("a/(b+c)")), iz("a / (b+c)"));
  }

  @Test public void test21() {
    azzert.that(FactorsReorganizer.simplify(i("1/(a/b)")), iz("1*b/a"));
  }
}
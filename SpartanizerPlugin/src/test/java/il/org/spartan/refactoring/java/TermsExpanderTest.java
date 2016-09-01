package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class TermsExpanderTest {
  @Test public void test00() {
    azzert.that(TermsExpander.simplify(i("a-b")), iz("a-b"));
  }

  @Test public void test01() {
    azzert.that(TermsExpander.simplify(i("a+b+c")), iz("a + b + c"));
  }

  @Test public void test02() {
    azzert.that(TermsExpander.simplify(i("a-c")), iz("a-c"));
  }

  @Test public void test03() {
    azzert.that(TermsExpander.simplify(i("a + (b+c)")), iz("a + b+c"));
  }

  @Test public void test04() {
    azzert.that(TermsExpander.simplify(i("a + (b-c)")), iz("a + b-c"));
  }

  @Test public void test05() {
    azzert.that(TermsExpander.simplify(i("a + b -c + d -e + f")), iz("a + b -c + d -e + f"));
  }

  @Test public void test06() {
    azzert.that(TermsExpander.simplify(i("a + (b + c + d)")), iz("a + b + c + d"));
  }

  @Test public void test07() {
    azzert.that(TermsExpander.simplify(i("a - (c -d)")), iz("a - c + d"));
  }

  @Test public void test08() {
    azzert.that(TermsExpander.simplify(i("a + -a")), iz("a - a"));
  }

  @Test public void test09() {
    azzert.that(TermsExpander.simplify(i("+a + +a")), iz("a + a"));
  }

  @Test public void test10() {
    azzert.that(TermsExpander.simplify(i("a - -b")), iz("a + b"));
  }

  @Test public void test11() {
    azzert.that(TermsExpander.simplify(i("a + - - (b + c)")), iz("a + b + c"));
  }

  @Test public void test12() {
    azzert.that(TermsExpander.simplify(i("a + + - - - (b -c)")), iz("a -b +c"));
  }

  @Test public void test13() {
    azzert.that(TermsExpander.simplify(i("-a + + - - - (b -c)")), iz("-a -b +c"));
  }

  @Test public void test14() {
    azzert.that(TermsExpander.simplify(i("-a + + - - - (b -(-c))")), iz(" -a -b-c"));
  }

  @Test public void test15() {
    azzert.that(TermsExpander.simplify(i("-a-b")), iz("-a-b"));
  }

  @Test public void test16() {
    azzert.that(TermsExpander.simplify(i("x-a-b")), iz("x-a-b"));
  }

  @Test public void test17() {
    azzert.that(TermsExpander.simplify(i("x-a-(-((-b)) - (((-(((-(y))))))))")), iz("x-a-b+y"));
  }

  @Test public void test18() {
    azzert.that(TermsExpander.simplify(i("(-(x-a))-+ + (y -b)")), iz("a-x-y+b"));
  }

  @Test public void test19() {
    azzert.that(TermsExpander.simplify(i("a-b*c - (x - - - (d*e))")), //
        iz("a - b*c -x + d*e"));
  }

  @Test public void test20() {
    azzert.that(TermsExpander.simplify(i("a-b*c")), iz("a - b*c"));
  }

  @Test public void test21() {
    azzert.that(TermsExpander.simplify(i("-a+b")), iz("b-a"));
  }

  @Test public void test22() {
    azzert.that(TermsExpander.simplify(i("-a-b")), iz("-a-b"));
  }
}
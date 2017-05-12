package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;

import fluent.ly.*;

@SuppressWarnings("static-method")
public final class TermsExpanderTest {
  final String complexStringCase = "\"Completed in \" + (1 + i) + \" passes.\" +\"Total changes: \" + (initialCount - finalCount)";

  @Test public void test00() {
    azzert.that(TermsExpander.simplify(i("7-3")), iz("7-3"));
  }

  @Test public void test01() {
    azzert.that(TermsExpander.simplify(i("7+3+2")), iz("7 + 3 + 2"));
  }

  @Test public void test02() {
    azzert.that(TermsExpander.simplify(i("7-2")), iz("7-2"));
  }

  @Test public void test03() {
    azzert.that(TermsExpander.simplify(i("7 + (3+2)")), iz("7 + 3+2"));
  }

  @Test public void test04() {
    azzert.that(TermsExpander.simplify(i("7 + (b-c)")), iz("7 + b-c"));
  }

  @Test public void test05() {
    azzert.that(TermsExpander.simplify(i("7 + 3 -c + 4 -e + 6")), iz("7 + 3 -c + 4 -e + 6"));
  }

  @Test public void test06() {
    azzert.that(TermsExpander.simplify(i("7 + (5 + 6 + 7)")), iz("7 + 5 + 6 + 7"));
  }

  @Test public void test07() {
    azzert.that(TermsExpander.simplify(i("7 - (c -d)")), iz("7 - c + d"));
  }

  @Test public void test08() {
    azzert.that(TermsExpander.simplify(i("7 + -a")), iz("7 - a"));
  }

  @Test public void test09() {
    azzert.that(TermsExpander.simplify(i("+a + +a")), iz("a + a"));
  }

  @Test public void test10() {
    azzert.that(TermsExpander.simplify(i("a - -b")), iz("a + b"));
  }

  @Test public void test11() {
    azzert.that(TermsExpander.simplify(i("7 + - - (b + c)")), iz("7 + b + c"));
  }

  @Test public void test12() {
    azzert.that(TermsExpander.simplify(i("7 + + - - - (b -c)")), iz("7 -b +c"));
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
    azzert.that(TermsExpander.simplify(i("-a+7")), iz("7-a"));
  }

  @Test public void test22() {
    azzert.that(TermsExpander.simplify(i("-a-b")), iz("-a-b"));
  }

  @Test public void test23() {
    azzert.that(TermsExpander.simplify(i(complexStringCase)), iz(complexStringCase));
  }

  @Test public void test24() {
    azzert.that(TermsExpander.simplify(i("\"\" + (x-y)")), iz("\"\" + (x-y)"));
  }

  @Test public void test25() {
    azzert.that(TermsExpander.simplify(i("\"\" + (x+y)")), iz("\"\" + (x+y)"));
  }

  @Test public void test26() {
    azzert.that(TermsExpander.simplify(i("\"\" + x + y")), iz("\"\" + x + y"));
  }
}

package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;

import org.junit.*;

import fluent.ly.*;

@SuppressWarnings("static-method")
public final class FactorsExpanderTest {
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
  @Test public void test15() {
    azzert.that(FactorsExpander.simplify(i("1/a/b")), iz("1/a/b"));
  }
  @Test public void test16() {
    azzert.that(FactorsExpander.simplify(i("x/a/b")), iz("x/a/b"));
  }
  @Test public void test20() {
    azzert.that(FactorsExpander.simplify(i("a/(b+c)")), iz("a / (b+c)"));
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

/* TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Sep 7, 2016 */
package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method")
public final class FactorsReogranizerTest {
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

  @Test public void test20() {
    azzert.that(FactorsReorganizer.simplify(i("a/(b+c)")), iz("a / (b+c)"));
  }

  @Test public void test21() {
    azzert.that(FactorsReorganizer.simplify(i("1/(a/b)")), iz("1*b/a"));
  }
}

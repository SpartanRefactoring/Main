package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Checking that the arithmetic simplification tipper is working properly
 * @author Dor Ma'ayan
 * @since 05-12-2016 */
@SuppressWarnings("static-method")
public class Issue0892 {
  @Test public void test0() {
    trimmingOf("b+-3")//
        .gives("b-3");
  }

  @Test public void test1() {
    trimmingOf("x+-3.4")//
        .gives("x-3.4");
  }

  @Test public void test2() {
    trimmingOf("x-+3.4")//
        .gives("x-3.4");
  }
}

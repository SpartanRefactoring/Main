package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Checking that the arithmetic simplification tipper is working properly
 * @author Dor Ma'ayan
 * @since 05-12-2016 */
@SuppressWarnings("static-method")
public class Issue0892 {
  @Test public void test0() {
    topDownTrimming("b+-3")//
        .gives("b-3");
  }

  @Test public void test1() {
    topDownTrimming("x+-3.4")//
        .gives("x-3.4");
  }

  @Test public void test2() {
    topDownTrimming("x-+3.4")//
        .gives("x-3.4");
  }
}

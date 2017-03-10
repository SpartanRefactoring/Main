package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Test Class for Issue #884
 * @author Dor Ma'ayan
 * @since 09-12-2016 */
@SuppressWarnings("static-method")
public class AdvancedGivesTests {
  @Test public void test0() {
    trimmingOf("1+2")//
        .givesEither("3", "")//
        .stays();
  }

  @Test public void test1() {
    trimmingOf("1+2+a")//
        .givesEither("a+3", "3+a");
  }

  @Ignore
  private static class NotWorking {
    @Test public void test2() {
      trimmingOf("for (int ¢ = 0 ; ¢ < 5 ; ++¢) {++¢; continue;}")//
          .givesEither("for (int ¢ = 0 ; ¢ < 5 ; ++¢) {++¢;}", //
              "for (int ¢ = 0 ; ¢ < 5 ; ++¢) ++¢;");
    }
  }
}

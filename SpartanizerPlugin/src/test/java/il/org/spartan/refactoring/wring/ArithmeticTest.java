package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;
import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for Arithmetic Operations Calculations
 * @author Dor Ma'ayan <code><dor.d.ma [at] gmail.com></code>
 * @since 2016-08-26 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class ArithmeticTest {
  @Ignore("#92:under construction") public static class NotWorking {
    @Test public void issue92_1() {
      trimming("1.+2*3 / 4 - 5").to("-2.5"); // check it out...
    }

    @Test public void issue92_2() {
      trimming("1.").to(null);
    }

    @Test public void issue92_3() {
      trimming("1+1").to("2");
    }

    @Test public void issue92_4() {
      trimming("1.+1.").to("2.");
    }

    @Test public void issue92_5() {
      trimming("5.*5.").to("25.");
    }

    @Test public void issue92_6() {
      trimming("3./4").to("0.75");
    }

    public static class Working {
      /** Empty for now */
    }
  }
}

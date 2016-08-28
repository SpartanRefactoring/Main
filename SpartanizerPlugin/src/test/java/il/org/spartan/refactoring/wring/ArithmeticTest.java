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
    @Test @Ignore("#92:under construction") public void issue92_1() {
      trimming("1.+2*3 / 4 - 5").to("-3"); // check it out...
    }

    @Test public void issue92_1a() {
      trimming("1.+2*3 / 4 - 5").to("-3"); // check it out...
    }

    @Test @Ignore("#92:under construction") public void issue92_2() {
      trimming("1.").to(null);
    }

    @Test public void issue92_3() {
      trimming("1+1").to("2");
    }

    @Test public void issue92_3a() {
      trimming("1+1").to("2");
    }

    @Test public void issue92_4a() {
      trimming("1+1+3").to("2+3").to("5");
    }

    @Test public void issue92_4() {
      trimming("1.+1.").to("2.");
    }

    @Test @Ignore("#92:under construction") public void issue92_5() {
      trimming("1.+1.").to("2.");
    }

    @Test @Ignore("#92:under construction") public void issue92_6() {
      trimming("5.*5.").to("25.");
    }

    @Test @Ignore("#92:under construction") public void issue92_7() {
      trimming("3./4").to("0.75").to(null);
    }

    @Test @Ignore("#92:under construction") public void issue92_8() {
      trimming("1L*2+1L*99").to("1L*100");
    }

    @Test @Ignore("#92:under construction") public void issue92_8a() {
      trimming("1L*2+1L*99").to("1L*100").to("100L");
    }

    @Test @Ignore("#92:under construction") public void issue92_15() {
      trimming("9*6-4").to("54-4").to("50");
    }
  }

  public static class Working {
    /** Empty for now */
    @Test public void issue92_3() {
      trimming("1+1").to("2");
    }

    @Test public void issue92_4() {
      trimming("1+1+3").to("5");
    }

    @Test public void issue92_9() {
      trimming("3*4*2").to("24");
    }

    @Test public void issue92_10() {
      trimming("3*4+2").to("12+2").to("14");
    }

    @Test public void issue92_11() {
      trimming("2+3*4").to("3*4+2").to("12+2").to("14");
    }

    @Test public void issue92_12() {
      trimming("100-49").to("51");
    }

    @Test public void issue92_13() {
      trimming("1-2").to("-1");
    }

    @Test public void issue92_14() {
      trimming("-9*2").to("-18");
    }

    @Test public void issue92_16() {
      trimming("4 + -9").to("4-9").to("-5");
    }

    @Test public void issue92_17() {
      trimming("4 * -9").to("-36");
    }

    @Test public void issue92_18() {
      trimming("4 * -9 * -1").to("36");
    }

    @Test public void issue92_19() {
      trimming("4 * -9 + 5*5").to("5*5 +4*-9").to("25+-36").to("25-36").to("-11");
    }

    @Test public void issue92_20() {
      trimming("5*5+6*7-9").to("25+42-9");
    }
  }
}

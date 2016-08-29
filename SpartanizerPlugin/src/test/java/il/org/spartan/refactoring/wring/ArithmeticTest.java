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


    @Test @Ignore("#92:under construction") public void issue92_8() {
      trimming("1L*2+1L*99").to("1L*100");
    }

    @Test @Ignore("#92:under construction") public void issue92_8a() {
      trimming("1L*2+1L*99").to("1L*100").to("100L");
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
    @Test public void issue92_21() {
      trimming("12/3").to("4");
    }
    @Test public void issue92_22() {
      trimming("12/5").to("2");
    }
    @Test public void issue92_23() {
      trimming("-12/5").to("-2");
    }
    @Test public void issue92_24() {
      trimming("12/-5").to("-2");
    }
    @Test public void issue92_25() {
      trimming("12/-5/2").to("-1");
    }
    @Test public void issue92_26() {
      trimming("1.0+2.0").to("3.0");
    }
    @Test public void issue92_27() {
      trimming("1.9+2.2").to("4.1");
    }
    @Test public void issue92_28() {
      trimming("1+2.2").to("3.2");
    }
    @Test public void issue92_29() {
      trimming("1.+1.").to("2.0");
    }
    @Test public void issue92_30() {
      trimming("2*1.0").to("2.0");
    }
    @Test public void issue92_2() {
      trimming("1.").to(null);
    }
    @Test public void issue92_4a() {
      trimming("1+1+3").to("5");
    }
    @Test public void issue92_6() {
      trimming("5.*5.").to("25.0");
    }
    @Test public void issue92_31() {
      trimming("4-5.0").to("-1.0");
    }
    @Test  public void issue92_33() {
      trimming("5 *-9.0 +3").to("-45.0+3").to("3-45.0").to("-42.0");
    }
    @Test  public void issue92_34() {
      trimming("100/2/5").to("10");
    }
    @Test  public void issue92_35() {
      trimming("100L+2L").to("102L");
    }
    @Test  public void issue92_36() {
      trimming("100L+2.0").to("102.0");
    }
    @Test  public void issue92_37() {
      trimming("100L+2+1.0").to("103.0");
    }
    @Test  public void issue92_38() {
      trimming("100L*2+1.0").to("200L+1.0").to("201.0");
    }
    @Test  public void issue92_39() {
      trimming("100L*9.0").to("900.0");
    }
    
    @Test public void issue92_1a() {
      trimming("1.+2*3 / 4 - 5").to("2*3/4+1.-5").to("6/4+1.-5").to("1+1.-5").to("2.0-5").to("-3.0");
    }
    
    @Test public void issue92_7() {
      trimming("3./4").to("0.75").to(null);
    }
    
    @Test public void issue92_15() {
      trimming("9*6-4").to("54-4").to("50");
    }
    
  }
}

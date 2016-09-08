package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.java.*;

/** * Unit tests for Arithmetic Operations Calculations
 * @author Dor Ma'ayan <code><dor.d.ma [at] gmail.com></code>
 * @since 2016-08-26 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class ArithmeticTest {
  public static class Working {
    @Test public void additionIsLong() {
      assert atomic.isLong(1 + 1L);
    }

    @Test public void issue92_1() {
      trimming("1.+2*3 / 4 - 5").to("2*3/4+1.-5").to("6/4+1.-5").to("1+1.-5").to("2.0-5").to("-3.0");
    }

    @Test public void issue92_10() {
      trimming("2+3*4").to("3*4+2").to("12+2").to("14");
    }

    @Test public void issue92_11() {
      trimming("100-49").to("51");
    }

    @Test public void issue92_12() {
      trimming("1-2").to("-1");
    }

    @Test public void issue92_13() {
      trimming("-9*2").to("-18");
    }

    @Test public void issue92_14() {
      trimming("9*6-4").to("54-4").to("50");
    }

    @Test public void issue92_15() {
      trimming("4 + -9").to("4-9").to("-5");
    }

    @Test public void issue92_16() {
      trimming("4 * -9").to("-36");
    }

    @Test public void issue92_17() {
      trimming("4 * -9 * -1").to("36");
    }

    @Test public void issue92_18() {
      trimming("4*-9 + 5*5")//
          .to("5*5 -4*9")//
          .to("25-36")//
          .to("-11")//
          .to(null);
    }

    @Test public void issue92_19() {
      trimming("a*-q + s*s")//
          .to("s*s -a*q")//
          .to(null);
    }

    @Test public void issue92_2() {
      trimming("1.").to(null);
    }

    @Test public void issue92_20() {
      trimming("4*-9")//
          .to("-36")//
          .to(null)//
      ;
    }

    @Test public void issue92_21() {
      trimming("5*5+6*7-9").to("25+42-9");
    }

    @Test public void issue92_22() {
      trimming("12/3").to("4");
    }

    @Test public void issue92_23() {
      trimming("12/5").to("2");
    }

    @Test public void issue92_24() {
      trimming("-12/5").to("-2");
    }

    @Test public void issue92_25() {
      trimming("12/-5").to("-2");
    }

    @Test public void issue92_26() {
      trimming("12/-5/2").to("-1");
    }

    @Test public void issue92_27() {
      trimming("1.0+2.0").to("3.0");
    }

    @Test public void issue92_28() {
      trimming("1.9+2.2").to("4.1");
    }

    @Test public void issue92_29() {
      trimming("1+2.2").to("3.2");
    }

    @Test public void issue92_3() {
      trimming("1+1+3").to("5");
    }

    @Test public void issue92_30() {
      trimming("1.+1.").to("2.0");
    }

    @Test public void issue92_31() {
      trimming("2*1.0").to("2.0");
    }

    @Test public void issue92_32() {
      trimming("4-5.0").to("-1.0");
    }

    @Test public void issue92_33() {
      trimming("5 *-9.0 +3")//
          .to("3-5*9.0")//
          .to("3-45.0")//
          .to("-42.0")//
          .to(null);
    }

    @Test public void issue92_34() {
      trimming("-a+5")//
          .to("5-a")//
          .to(null);
    }

    @Test public void issue92_35() {
      trimming("100/2/5").to("10");
    }

    @Test public void issue92_36() {
      trimming("100L+2L").to("102L");
    }

    @Test public void issue92_37() {
      trimming("100L+2.0").to("102.0");
    }

    @Test public void issue92_38() {
      trimming("100L+2+1.0").to("103.0");
    }

    @Test public void issue92_39() {
      trimming("100L*2+1.0").to("200L+1.0").to("201.0");
    }

    @Test public void issue92_4() {
      trimming("1+1+3").to("5");
    }

    @Test public void issue92_40() {
      trimming("100L*9.0").to("900.0");
    }

    @Test public void issue92_41() {
      trimming("100L-9L").to("91L");
    }

    @Test public void issue92_42() {
      trimming("100L-9.0").to("91.0");
    }

    @Test public void issue92_43() {
      trimming("100L-9").to("91L");
    }

    @Test public void issue92_44() {
      trimming("100L/2").to("50L");
    }

    @Test public void issue92_45() {
      trimming("100L/10.0").to("10.0");
    }

    @Test public void issue92_46() {
      trimming("1.+2*3 / 4 - 5*48L").to("2*3/4+1.-240L").to("6/4+1.-240L").to("1+1.-240L").to("2.0-240L").to("-238.0");
    }

    @Test public void issue92_47() {
      trimming("10%2").to("0");
    }

    @Test public void issue92_48() {
      trimming("10%3").to("1");
    }

    @Test public void issue92_49() {
      trimming("10L%3").to("1L");
    }

    @Test public void issue92_5() {
      trimming("5.*5.").to("25.0");
    }

    @Test public void issue92_50() {
      trimming("100L%3%1").to("0L");
    }

    @Test public void issue92_51() {
      trimming("100%3L%1").to("0L");
    }

    @Test public void issue92_52() {
      trimming("100>>2").to("25");
    }

    @Test public void issue92_53() {
      trimming("-1/-2*-3/-4*-5*-6/-7/-8/-9") //
          .to("-1/2*3/4*5*6/7/8/9") //
          .to("5*6*-1/2*3/4/7/8/9") //
          .to("3*5*6*-1/2/4/7/8/9") //
          .to("-90/2/4/7/8/9").to("0");
    }

    @Test public void issue92_54() {
      trimming("100L>>2").to("25L");
    }

    @Test public void issue92_55() {
      trimming("100>>2L").to("25");
    }

    @Test public void issue92_56() {
      trimming("100<<2").to("400");
    }

    @Test public void issue92_57() {
      trimming("100L<<2").to("400L");
    }

    @Test public void issue92_58() {
      trimming("100<<2L").to("400");
    }

    @Test public void issue92_59() {
      trimming("100L<<2L").to("400L");
    }

    @Test public void issue92_6() {
      trimming("3./4").to("0.75").to(null);
    }

    @Test public void issue92_60() {
      trimming("100L<<2L>>2L").to("400L>>2L").to("100L");
    }

    @Test public void issue92_61() {
      trimming("-1.0/-2*-3/-4*-5*-6/-7/-8/-9") //
          .to("-1.0/2*3/4*5*6/7/8/9") //
          .to("5*6*-1.0/2*3/4/7/8/9") //
          .to("3*5*6*-1.0/2/4/7/8/9") //
          .to("-90.0/2/4/7/8/9").to("-0.022321428571428572");
    }

    @Test public void issue92_62() {
      trimming("1.984+0.006").to("1.99");
    }

    @Test public void issue92_7() {
      trimming("1L*2+1L*99").to("2L+99L").to("101L");
    }

    @Test public void issue92_8() {
      trimming("3*4*2").to("24");
    }

    @Test public void issue92_9() {
      trimming("3*4+2").to("12+2").to("14");
    }

    @Test public void issue143_1() {
      trimming("1*31").to("31");
    }

    @Test public void issue143_2() {
      trimming("@Override public int hashCode() { "//
          + "return 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 *(1 * 31 + (blockScope == null ? 0 : blockScope.hashCode())))"
          + "+(self == null ? 0 : self.hashCode());" //
          + "}")
              .to("@Override public int hashCode() { "//
                  + "return (self == null ? 0 : self.hashCode())"
                  + "+ 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 *(1 * 31 + (blockScope == null ? 0 : blockScope.hashCode())));" //
                  + "}")
              .to("@Override public int hashCode() { "//
                  + "return (self == null ? 0 : self.hashCode())"
                  + "+ 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 *(31 + (blockScope == null ? 0 : blockScope.hashCode())));" + "}");
    }
  }
}

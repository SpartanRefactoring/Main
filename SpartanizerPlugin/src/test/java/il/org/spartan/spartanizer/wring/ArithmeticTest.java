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
      assert atomic.isLong(2L);
    }

    @Test public void issue92__1() {
      trimming("1.+2*3 / 4 - 5").to("2*3/4+1.-5").to("6/4+1.-5").to("1+1.-5").to("2.0-5").to("-3.0");
    }

    @Test public void issue92__10() {
      trimming("2+3*4").to("3*4+2").to("12+2").to("14");
    }

    @Test public void issue92__11() {
      trimming("100-49").to("51");
    }

    @Test public void issue92__12() {
      trimming("1-2").to("-1");
    }

    @Test public void issue92__13() {
      trimming("-9*2").to("-18");
    }

    @Test public void issue92__14() {
      trimming("9*6-4").to("54-4").to("50");
    }

    @Test public void issue92__15() {
      trimming("4 + -9").to("4-9").to("-5");
    }

    @Test public void issue92__16() {
      trimming("4 * -9").to("-36");
    }

    @Test public void issue92__17() {
      trimming("4 * -9 * -1").to("36");
    }

    @Test public void issue92__18() {
      trimming("4*-9 + 5*5")//
          .to("5*5 -4*9")//
          .to("25-36")//
          .to("-11")//
          .stays();
    }

    @Test public void issue92__19() {
      trimming("a*-q + s*s")//
          .to("s*s -a*q")//
          .stays();
    }

    @Test public void issue92__2() {
      trimming("1.").stays();
    }

    @Test public void issue92__20() {
      trimming("4*-9")//
          .to("-36")//
          .stays()//
      ;
    }

    @Test public void issue92__21() {
      trimming("5*5+6*7-9").to("25+42-9");
    }

    @Test public void issue92__22() {
      trimming("12/3").to("4");
    }

    @Test public void issue92__23() {
      trimming("12/5").to("2");
    }

    @Test public void issue92__24() {
      trimming("-12/5").to("-2");
    }

    @Test public void issue92__25() {
      trimming("12/-5").to("-2");
    }

    @Test public void issue92__26() {
      trimming("12/-5/2").to("-1");
    }

    @Test public void issue92__27() {
      trimming("1.0+2.0").to("3.0");
    }

    @Test public void issue92__28() {
      trimming("1.9+2.2").to("4.1");
    }

    @Test public void issue92__29() {
      trimming("1+2.2").to("3.2");
    }

    @Test public void issue92__3() {
      trimming("1+1+3").to("5");
    }

    @Test public void issue92__30() {
      trimming("1.+1.").to("2.0");
    }

    @Test public void issue92__31() {
      trimming("2*1.0").to("2.0");
    }

    @Test public void issue92__32() {
      trimming("4-5.0").to("-1.0");
    }

    @Test public void issue92__33() {
      trimming("5 *-9.0 +3")//
          .to("3-5*9.0")//
          .to("3-45.0")//
          .to("-42.0")//
          .stays();
    }

    @Test public void issue92__34() {
      trimming("-a+5")//
          .to("5-a")//
          .stays();
    }

    @Test public void issue92__35() {
      trimming("100/2/5").to("10");
    }

    @Test public void issue92__36() {
      trimming("100L+2L").to("102L");
    }

    @Test public void issue92__37() {
      trimming("100L+2.0").to("102.0");
    }

    @Test public void issue92__38() {
      trimming("100L+2+1.0").to("103.0");
    }

    @Test public void issue92__39() {
      trimming("100L*2+1.0").to("200L+1.0").to("201.0");
    }

    @Test public void issue92__4() {
      trimming("1+1+3").to("5");
    }

    @Test public void issue92__40() {
      trimming("100L*9.0").to("900.0");
    }

    @Test public void issue92__41() {
      trimming("100L-9L").to("91L");
    }

    @Test public void issue92__42() {
      trimming("100L-9.0").to("91.0");
    }

    @Test public void issue92__43() {
      trimming("100L-9").to("91L");
    }

    @Test public void issue92__44() {
      trimming("100L/2").to("50L");
    }

    @Test public void issue92__45() {
      trimming("100L/10.0").to("10.0");
    }

    @Test public void issue92__46() {
      trimming("1.+2*3 / 4 - 5*48L").to("2*3/4+1.-240L").to("6/4+1.-240L").to("1+1.-240L").to("2.0-240L").to("-238.0");
    }

    @Test public void issue92__47() {
      trimming("10%2").to("0");
    }

    @Test public void issue92__48() {
      trimming("10%3").to("1");
    }

    @Test public void issue92__49() {
      trimming("10L%3").to("1L");
    }

    @Test public void issue92__5() {
      trimming("5.*5.").to("25.0");
    }

    @Test public void issue92__50() {
      trimming("100L%3%1").to("0L");
    }

    @Test public void issue92__51() {
      trimming("100%3L%1").to("0L");
    }

    @Test public void issue92__52() {
      trimming("100>>2").to("25");
    }

    @Test public void issue92__53() {
      trimming("-1/-2*-3/-4*-5*-6/-7/-8/-9") //
          .to("-1/2*3/4*5*6/7/8/9") //
          .to("5*6*-1/2*3/4/7/8/9") //
          .to("3*5*6*-1/2/4/7/8/9") //
          .to("-90/2/4/7/8/9").to("0");
    }

    @Test public void issue92__54() {
      trimming("100L>>2").to("25L");
    }

    @Test public void issue92__55() {
      trimming("100>>2L").to("25");
    }

    @Test public void issue92__56() {
      trimming("100<<2").to("400");
    }

    @Test public void issue92__57() {
      trimming("100L<<2").to("400L");
    }

    @Test public void issue92__58() {
      trimming("100<<2L").to("400");
    }

    @Test public void issue92__59() {
      trimming("100L<<2L").to("400L");
    }

    @Test public void issue92__6() {
      trimming("3./4").to("0.75").stays();
    }

    @Test public void issue92__60() {
      trimming("100L<<2L>>2L").to("400L>>2L").to("100L");
    }

    @Test public void issue92__61() {
      trimming("-1.0/-2*-3/-4*-5*-6/-7/-8/-9") //
          .to("-1.0/2*3/4*5*6/7/8/9") //
          .to("5*6*-1.0/2*3/4/7/8/9") //
          .to("3*5*6*-1.0/2/4/7/8/9") //
          .to("-90.0/2/4/7/8/9").to("-0.022321428571428572");
    }

    @Test public void issue92__62() {
      trimming("1.984+0.006").to("1.99");
    }

    @Test public void issue92__7() {
      trimming("1L*2+1L*99").to("2L+99L").to("101L");
    }

    @Test public void issue92__8() {
      trimming("3*4*2").to("24");
    }

    @Test public void issue92__9() {
      trimming("3*4+2").to("12+2").to("14");
    }

    @Test public void issue143__1() {
      trimming("1*31").to("31");
    }

    @Test public void issue143__2() {
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

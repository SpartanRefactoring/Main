package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.java.*;

/** * Unit tests for Arithmetic Operations Calculations
 * @author Dor Ma'ayan <code><dor.d.ma [at] gmail.com></code>
 * @since 2016-08-26 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class ArithmeticTest {
  public static class Working {
    @Test public void additionIsLong() {
      assert atomic.isLong(2L);
    }

    @Test public void issue143_1() {
      trimmingOf("1*31").gives("31");
    }

    @Test public void issue143_2() {
      trimmingOf("@Override public int hashCode() { "//
          + "return 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 *(1 * 31 + (blockScope == null ? 0 : blockScope.hashCode())))"
          + "+(self == null ? 0 : self.hashCode());" //
          + "}")
              .gives("@Override public int hashCode() { "//
                  + "return (self == null ? 0 : self.hashCode())"
                  + "+ 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 *(1 * 31 + (blockScope == null ? 0 : blockScope.hashCode())));" //
                  + "}")
              .gives("@Override public int hashCode() { "//
                  + "return (self == null ? 0 : self.hashCode())"
                  + "+ 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 *(31 + (blockScope == null ? 0 : blockScope.hashCode())));" + "}");
    }

    @Test public void issue158_1() {
      trimmingOf(" assertEquals(5 / 9.0, s_4x0_5x1.mean(), 1E-6);").stays();
    }

    @Test public void issue158_2() {
      trimmingOf(" assertEquals(5 / 1, s_4x0_5x1.mean(), 1E-6);").gives(" assertEquals(5, s_4x0_5x1.mean(), 1E-6);");
    }

    @Test public void issue158_3() {
      trimmingOf(" assertEquals(99*2, s_4x0_5x1.mean(), 1E-6);").gives(" assertEquals(198, s_4x0_5x1.mean(), 1E-6);");
    }

    @Test public void issue171_1() {
      trimmingOf("3*a+2").stays();
    }

    @Test public void issue171_2() {
      trimmingOf("3+p").stays();
    }

    @Test public void issue171_3() {
      trimmingOf("p%3%r").stays();
    }

    @Test public void issue171_4() {
      trimmingOf("p+q+w-9*4").gives("p+q+w-36").stays();
    }

    @Test public void issue171_5() {
      trimmingOf("5L%p+q+w-9*4").gives("q+w+5L%p-36").stays();
    }

    @Test public void issue171_6() {
      trimmingOf("5L%6.").gives("5L").stays();
    }

    @Test public void issue201_1() {
      trimmingOf("private long l = (long) (1L * ++d * f--);").stays();
    }

    @Test public void issue92_1() {
      trimmingOf("1.+2*3 / 4 - 5").gives("2*3/4+1.-5").gives("6/4+1.-5").gives("1+1.-5").gives("2.0-5").gives("-3.0");
    }

    @Test public void issue92_10() {
      trimmingOf("2+3*4").gives("3*4+2").gives("12+2").gives("14");
    }

    @Test public void issue92_11() {
      trimmingOf("100-49").gives("51");
    }

    @Test public void issue92_12() {
      trimmingOf("1-2").gives("-1");
    }

    @Test public void issue92_13() {
      trimmingOf("-9*2").gives("-18");
    }

    @Test public void issue92_14() {
      trimmingOf("9*6-4").gives("54-4").gives("50");
    }

    @Test public void issue92_15() {
      trimmingOf("4 + -9").gives("4-9").gives("-5");
    }

    @Test public void issue92_16() {
      trimmingOf("4 * -9").gives("-36");
    }

    @Test public void issue92_17() {
      trimmingOf("4 * -9 * -1").gives("36");
    }

    @Test public void issue92_18() {
      trimmingOf("4*-9 + 5*5")//
          .gives("5*5 -4*9")//
          .gives("25-36")//
          .gives("-11")//
          .stays();
    }

    @Test public void issue92_19() {
      trimmingOf("a*-q + s*s")//
          .gives("s*s -a*q")//
          .stays();
    }

    @Test public void issue92_2() {
      trimmingOf("1.").stays();
    }

    @Test public void issue92_20() {
      trimmingOf("4*-9")//
          .gives("-36")//
          .stays()//
      ;
    }

    @Test public void issue92_21() {
      trimmingOf("5*5+6*7-9").gives("25+42-9");
    }

    @Test public void issue92_22() {
      trimmingOf("12/3").gives("4");
    }

    @Test public void issue92_23() {
      trimmingOf("12/5").gives("2");
    }

    @Test public void issue92_24() {
      trimmingOf("-12/5").gives("-2");
    }

    @Test public void issue92_25() {
      trimmingOf("12/-5").gives("-2");
    }

    @Test public void issue92_26() {
      trimmingOf("12/-5/2").gives("-1");
    }

    @Test public void issue92_27() {
      trimmingOf("1.0+2.0").gives("3.0");
    }

    @Test public void issue92_28() {
      trimmingOf("1.9+2.2").gives("4.1");
    }

    @Test public void issue92_29() {
      trimmingOf("1+2.2").gives("3.2");
    }

    @Test public void issue92_3() {
      trimmingOf("1+1+3").gives("5");
    }

    @Test public void issue92_30() {
      trimmingOf("1.+1.").gives("2.0");
    }

    @Test public void issue92_31() {
      trimmingOf("2*1.0").gives("2.0");
    }

    @Test public void issue92_32() {
      trimmingOf("4-5.0").gives("-1.0");
    }

    @Test public void issue92_33() {
      trimmingOf("5 *-9.0 +3")//
          .gives("3-5*9.0")//
          .gives("3-45.0")//
          .gives("-42.0")//
          .stays();
    }

    @Test public void issue92_34() {
      trimmingOf("-a+5")//
          .gives("5-a")//
          .stays();
    }

    @Test public void issue92_35() {
      trimmingOf("100/2/5").gives("10");
    }

    @Test public void issue92_36() {
      trimmingOf("100L+2L").gives("102L");
    }

    @Test public void issue92_37() {
      trimmingOf("100L+2.0").gives("102.0");
    }

    @Test public void issue92_38() {
      trimmingOf("100L+2+1.0").gives("103.0");
    }

    @Test public void issue92_39() {
      trimmingOf("100L*2+1.0").gives("200L+1.0").gives("201.0");
    }

    @Test public void issue92_4() {
      trimmingOf("1+1+3").gives("5");
    }

    @Test public void issue92_40() {
      trimmingOf("100L*9.0").gives("900.0");
    }

    @Test public void issue92_41() {
      trimmingOf("100L-9L").gives("91L");
    }

    @Test public void issue92_42() {
      trimmingOf("100L-9.0").gives("91.0");
    }

    @Test public void issue92_43() {
      trimmingOf("100L-9").gives("91L");
    }

    @Test public void issue92_44() {
      trimmingOf("100L/2").gives("50L");
    }

    @Test public void issue92_45() {
      trimmingOf("100L/10.0").gives("10.0");
    }

    @Test public void issue92_46() {
      trimmingOf("1.+2*3 / 4 - 5*48L").gives("2*3/4+1.-240L").gives("6/4+1.-240L").gives("1+1.-240L").gives("2.0-240L").gives("-238.0");
    }

    @Test public void issue92_47() {
      trimmingOf("10%2").gives("0");
    }

    @Test public void issue92_48() {
      trimmingOf("10%3").gives("1");
    }

    @Test public void issue92_49() {
      trimmingOf("10L%3").gives("1L");
    }

    @Test public void issue92_5() {
      trimmingOf("5.*5.").gives("25.0");
    }

    @Test public void issue92_50() {
      trimmingOf("100L%3%1").gives("0L");
    }

    @Test public void issue92_51() {
      trimmingOf("100%3L%1").gives("0L");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_52() {
      trimmingOf("100>>2").gives("25");
    }

    @Test public void issue92_53() {
      trimmingOf("-1/-2*-3/-4*-5*-6/-7/-8/-9") //
          .gives("-1/2*3/4*5*6/7/8/9") //
          .gives("5*6*-1/2*3/4/7/8/9") //
          .gives("3*5*6*-1/2/4/7/8/9") //
          .gives("-90/2/4/7/8/9").gives("0");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_54() {
      trimmingOf("100L>>2").gives("25L");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_55() {
      trimmingOf("100>>2L").gives("25");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_56() {
      trimmingOf("100<<2").gives("400");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_57() {
      trimmingOf("100L<<2").gives("400L");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_58() {
      trimmingOf("100<<2L").gives("400");
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_59() {
      trimmingOf("100L<<2L").gives("400L");
    }

    @Test public void issue92_6() {
      trimmingOf("3./4").gives("0.75").stays();
    }

    @Test @Ignore("Do not compute shifts, they have a reason") public void issue92_60() {
      trimmingOf("100L<<2L>>2L").gives("400L>>2L").gives("100L");
    }

    @Test public void issue92_61() {
      trimmingOf("-1.0/-2*-3/-4*-5*-6/-7/-8/-9") //
          .gives("-1.0/2*3/4*5*6/7/8/9") //
          .gives("5*6*-1.0/2*3/4/7/8/9") //
          .gives("3*5*6*-1.0/2/4/7/8/9") //
          .gives("-90.0/2/4/7/8/9").gives("-0.022321428571428572");
    }

    @Test public void issue92_62() {
      trimmingOf("1.984+0.006").gives("1.99");
    }

    @Test public void issue92_7() {
      trimmingOf("1L*2+1L*99").gives("2L+99L").gives("101L");
    }

    @Test public void issue92_8() {
      trimmingOf("3*4*2").gives("24");
    }

    @Test public void issue92_9() {
      trimmingOf("3*4+2").gives("12+2").gives("14");
    }
  }
}

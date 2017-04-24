package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.java.*;

/** * Unit tests for Arithmetic Operations Calculations
 * @author Dor Ma'ayan <code><dor.d.ma [at] gmail.com></code>
 * @since 2016-08-26 */
 //
@SuppressWarnings({ "static-method", "javadoc" }) //
final class ArithmeticTest {
  public static class Working {
    @Test public void additionIsLong() {
      assert atomic.isLong(2L);
    }

    @Test public void issue143_1() {
      trimminKof("1*31")//
          .gives("31")//
          .stays();
    }

    @Test public void issue143_2() {
      trimminKof("return ((31 * 1 + ((blockScope == null) ? 0 : blockScope.hashCode())) * 31"
          + "+ ((hiding == null) ? 0 : hiding.hashCode())) * 31 + ((self == null) ? 0 : self.hashCode());")
              .gives("return 31 * ((31 * 1 + ((blockScope == null) ? 0 : blockScope.hashCode())) * 31"
                  + "+ ((hiding == null) ? 0 : hiding.hashCode())) + ((self == null) ? 0 : self.hashCode());")
              .gives(
                  "return 31 * (((hiding == null) ? 0 : hiding.hashCode()) + (31 * 1 + ((blockScope == null) ? 0 : blockScope.hashCode())) * 31) + ((self == null) ? 0 : self.hashCode());")
              .gives(
                  "return 31 * (((hiding == null) ? 0 : hiding.hashCode()) + 31 * (31 * 1 + ((blockScope == null) ? 0 : blockScope.hashCode()))) + ((self == null) ? 0 : self.hashCode());")
              .gives(
                  "return 31 * (((hiding == null) ? 0 : hiding.hashCode()) + 31 * (31 + ((blockScope == null) ? 0 : blockScope.hashCode()))) + ((self == null) ? 0 : self.hashCode());")
              .gives(
                  "return 31 * (((hiding == null) ? 0 : hiding.hashCode()) + 31 * (((blockScope == null) ? 0 : blockScope.hashCode()) + 31)) + ((self == null) ? 0 : self.hashCode());")
              .stays();
    }

    @Test public void issue158_1() {
      trimminKof("wizard.assertEquals(5 / 9.0, s_4x0_5x1.mean(), 1E-6);")//
          .stays();
    }

    @Test public void issue158_2() {
      trimminKof("wizard.assertEquals(5 / 1, s_4x0_5x1.mean(), 1E-6);")//
          .gives("wizard.assertEquals(5, s_4x0_5x1.mean(), 1E-6);")//
          .stays();
    }

    @Test public void issue158_3() {
      trimminKof("wizard.assertEquals(99*2, s_4x0_5x1.mean(), 1E-6);")//
          .gives("wizard.assertEquals(198, s_4x0_5x1.mean(), 1E-6);")//
          .stays();
    }

    @Test public void issue171_1() {
      trimminKof("3*a+2")//
          .stays();
    }

    @Test public void issue171_2() {
      trimminKof("3+p")//
          .stays();
    }

    @Test public void issue171_3() {
      trimminKof("p%3%r")//
          .stays();
    }

    @Test public void issue171_4() {
      trimminKof("p+q+w-9*4")//
          .gives("p+q+w-36")//
          .stays();
    }

    @Test public void issue171_5() {
      trimminKof("5L%p+q+w-9*4")//
          .gives("q+w+5L%p-36")//
          .stays();
    }

    @Test public void issue171_6() {
      trimminKof("5L%6.")//
          .stays();
    }

    @Test public void issue171_7() {
      trimminKof("5%6.")//
          .stays();
    }

    @Test public void issue171_8() {
      trimminKof("5%6.F")//
          .stays();
    }

    @Test public void issue201_1() {
      trimminKof("(long) (1L * ++d * f--)")//
          .stays();
    }

    @Test public void issue206_1() {
      trimminKof("3+4+a")//
          .gives("7+a")//
          .stays();
    }

    @Test public void issue206_10() {
      trimminKof("1L*1L*a*b*c")//
          .gives("1L*a*b*c")//
          .stays();
    }

    @Test public void issue206_11() {
      trimminKof(
          "public int compareTo(final Fraction other) {return other == this || numerator == other.numerator && denominator == other.denominator"
              + "|| 1L * 1L * numerator * other.denominator == 1L * 1L * denominator * other.numerator ? 0"
              + ": 1L * 1L * numerator * other.denominator <1L * 1L * denominator * other.numerator ? -1 : 1;}")
                  .gives("public int compareTo(final Fraction other) {"
                      + "return other == this || numerator == other.numerator && denominator == other.denominator"
                      + "|| 1L * numerator * other.denominator == 1L * denominator * other.numerator ? 0"
                      + ": 1L * numerator * other.denominator <1L * denominator * other.numerator ? -1 : 1;}");
    }

    @Test public void issue206_12() {
      trimminKof("8%3%a")//
          .gives("2%a")//
          .stays();
    }

    @Test public void issue206_13() {
      trimminKof("a%8%3")//
          .stays();
    }

    @Test public void issue206_2() {
      trimminKof("3*4*a")//
          .gives("12*a")//
          .stays();
    }

    @Test public void issue206_3() {
      trimminKof("3-4-a")//
          .gives("-1-a")//
          .stays();
    }

    @Test public void issue206_4() {
      trimminKof("5/1/a")//
          .gives("5/a")//
          .stays();
    }

    @Test public void issue206_5() {
      trimminKof("a+8+9")//
          .gives("a+17")//
          .stays();
    }

    @Test public void issue206_6() {
      trimminKof("a*8*9")//
          .gives("a*72")//
          .gives("72*a")//
          .stays();
    }

    @Test public void issue206_7() {
      trimminKof("a-8-2-2")//
          .gives("a-4")//
          .stays();
    }

    @Test public void issue206_8() {
      trimminKof("a-8-2L-2")//
          .gives("a-4L")//
          .stays();
    }

    @Test public void issue206_9() {
      trimminKof("a-8-2L-2.0")//
          .gives("a-4.0")//
          .stays();
    }

    @Test public void issue92_1() {
      trimminKof("1.+2*3 / 4 - 5")//
          .gives("2*3/4+1.-5")//
          .gives("6/4+1.-5")//
          .gives("1+1.-5")//
          .gives("2.0-5")//
          .gives("-3.0")//
          .stays();
    }

    @Test public void issue92_10() {
      trimminKof("2+3*4")//
          .gives("3*4+2")//
          .gives("12+2")//
          .gives("14")//
          .stays();
    }

    @Test public void issue92_11() {
      trimminKof("100-49")//
          .gives("51")//
          .stays();
    }

    @Test public void issue92_12() {
      trimminKof("1-2")//
          .gives("-1")//
          .stays();
    }

    @Test public void issue92_13() {
      trimminKof("-9*2")//
          .gives("-18")//
          .stays();
    }

    @Test public void issue92_14() {
      trimminKof("9*6-4")//
          .gives("54-4")//
          .gives("50")//
          .stays();
    }

    @Test public void issue92_15() {
      trimminKof("4 + -9")//
          .gives("4-9")//
          .gives("-5")//
          .stays();
    }

    @Test public void issue92_16() {
      trimminKof("4 * -9")//
          .gives("-36")//
          .stays();
    }

    @Test public void issue92_17() {
      trimminKof("4 * -9 * -1")//
          .gives("36")//
          .stays();
    }

    @Test public void issue92_18() {
      trimminKof("4*-9 + 5*5")//
          .gives("5*5 -4*9")//
          .gives("25-36")//
          .gives("-11")//
          .stays();
    }

    @Test public void issue92_19() {
      trimminKof("a*-q + s*s")//
          .gives("s*s -a*q")//
          .stays();
    }

    @Test public void issue92_2() {
      trimminKof("1.")//
          .stays();
    }

    @Test public void issue92_20() {
      trimminKof("4*-9")//
          .gives("-36")//
          .stays();
    }

    @Test public void issue92_21() {
      trimminKof("5*5+6*7-9")//
          .gives("25+42-9")//
          .gives("67-9")//
          .gives("58")//
          .stays();
    }

    @Test public void issue92_22() {
      trimminKof("12/3")//
          .gives("4")//
          .stays();
    }

    @Test public void issue92_23() {
      trimminKof("12/5")//
          .gives("2")//
          .stays();
    }

    @Test public void issue92_24() {
      trimminKof("-12/5")//
          .gives("-2")//
          .stays();
    }

    @Test public void issue92_25() {
      trimminKof("12/-5")//
          .gives("-2")//
          .stays();
    }

    @Test public void issue92_26() {
      trimminKof("12/-5/2")//
          .gives("-1")//
          .stays();
    }

    @Test public void issue92_27() {
      trimminKof("1.0+2.0")//
          .gives("3.0")//
          .stays();
    }

    @Test public void issue92_28() {
      trimminKof("1.9+2.2")//
          .gives("4.1")//
          .stays();
    }

    @Test public void issue92_29() {
      trimminKof("1+2.2")//
          .gives("3.2")//
          .stays();
    }

    @Test public void issue92_3() {
      trimminKof("1+1+3")//
          .gives("5")//
          .stays();
    }

    @Test public void issue92_30() {
      trimminKof("1.+1.")//
          .gives("2.0")//
          .stays();
    }

    @Test public void issue92_31() {
      trimminKof("2*1.0")//
          .gives("2.0")//
          .stays();
    }

    @Test public void issue92_32() {
      trimminKof("4-5.0")//
          .gives("-1.0")//
          .stays();
    }

    @Test public void issue92_33() {
      trimminKof("5 *-9.0 +3")//
          .gives("3-5*9.0")//
          .gives("3-45.0")//
          .gives("-42.0")//
          .stays();
    }

    @Test public void issue92_34() {
      trimminKof("-a+5")//
          .gives("5-a")//
          .stays();
    }

    @Test public void issue92_35() {
      trimminKof("100/2/5")//
          .gives("10")//
          .stays();
    }

    @Test public void issue92_36() {
      trimminKof("100L+2L")//
          .gives("102L")//
          .stays();
    }

    @Test public void issue92_37() {
      trimminKof("100L+2.0")//
          .gives("102.0")//
          .stays();
    }

    @Test public void issue92_38() {
      trimminKof("100L+2+1.0")//
          .gives("103.0")//
          .stays();
    }

    @Test public void issue92_39() {
      trimminKof("100L*2+1.0")//
          .gives("200L+1.0")//
          .gives("201.0")//
          .stays();
    }

    @Test public void issue92_4() {
      trimminKof("1+1+3")//
          .gives("5")//
          .stays();
    }

    @Test public void issue92_40() {
      trimminKof("100L*9.0")//
          .gives("900.0")//
          .stays();
    }

    @Test public void issue92_41() {
      trimminKof("100L-9L")//
          .gives("91L")//
          .stays();
    }

    @Test public void issue92_42() {
      trimminKof("100L-9.0")//
          .gives("91.0")//
          .stays();
    }

    @Test public void issue92_43() {
      trimminKof("100L-9")//
          .gives("91L")//
          .stays();
    }

    @Test public void issue92_44() {
      trimminKof("100L/2")//
          .gives("50L")//
          .stays();
    }

    @Test public void issue92_45() {
      trimminKof("100L/10.0")//
          .gives("10.0")//
          .stays();
    }

    @Test public void issue92_46() {
      trimminKof("1.+2*3 / 4 - 5*48L")//
          .gives("2*3/4+1.-240L")//
          .gives("6/4+1.-240L")//
          .gives("1+1.-240L")//
          .gives("2.0-240L")//
          .gives("-238.0")//
          .stays();
    }

    @Test public void issue92_47() {
      trimminKof("10%2")//
          .gives("0")//
          .stays();
    }

    @Test public void issue92_48() {
      trimminKof("10%3")//
          .gives("1")//
          .stays();
    }

    @Test public void issue92_49() {
      trimminKof("10L%3")//
          .gives("1L")//
          .stays();
    }

    @Test public void issue92_5() {
      trimminKof("5.*5.")//
          .gives("25.0")//
          .stays();
    }

    @Test public void issue92_50() {
      trimminKof("100L%3%1")//
          .gives("0L")//
          .stays();
    }

    @Test public void issue92_51() {
      trimminKof("100%3L%1")//
          .gives("0L")//
          .stays();
    }

    @Test public void issue92_53() {
      trimminKof("-1/-2*-3/-4*-5*-6/-7/-8/-9")//
          .gives("-1/2*3/4*5*6/7/8/9")//
          .gives("-1/2*3/4*30/7/8/9")//
          .gives("30*-1/2*3/4/7/8/9").gives("3*30*-1/2/4/7/8/9")//
          .gives("-90/2/4/7/8/9")//
          .gives("0")//
          .stays();
    }

    @Test public void issue92_6() {
      trimminKof("3./4")//
          .gives("0.75")//
          .stays();
    }

    @Test public void issue92_61() {
      trimminKof("-1.0/-2*-3/-4*-5*-6/-7/-8/-9")//
          .gives("-1.0/2*3/4*5*6/7/8/9")//
          .gives("-1.0/2*3/4*30/7/8/9")//
          .gives("30*-1.0/2*3/4/7/8/9").gives("3*30*-1.0/2/4/7/8/9")//
          .gives("-90.0/2/4/7/8/9")//
          .gives("-0.022321428571428572")//
          .stays();
    }

    @Test public void issue92_62() {
      trimminKof("1.984+0.006")//
          .gives("1.99")//
          .stays();
    }

    @Test public void issue92_7() {
      trimminKof("1L*2+1L*99")//
          .gives("2L+99L")//
          .gives("101L")//
          .stays();
    }

    @Test public void issue92_8() {
      trimminKof("3*4*2")//
          .gives("24")//
          .stays();
    }

    @Test public void issue92_9() {
      trimminKof("3*4+2")//
          .gives("12+2")//
          .gives("14")//
          .stays();
    }

    @Test public void sanity() {
      trimminKof("10/2/1L")//
          .gives("5L");
    }

    @Test public void sanity2() {
      trimminKof("10/2/1.")//
          .gives("5.0");
    }

    @Test public void sanity3() {
      trimminKof("10/2/0L")//
          .stays();
    }
  }
}

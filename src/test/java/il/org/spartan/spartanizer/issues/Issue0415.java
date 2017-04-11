package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Ignored arithmetic tests of issue 92 (arithmetic simplification) are moved
 * here.
 * @author Yossi Gil
 * @since 2016 */
@Ignore("Do not compute shifts, they have a reason")
@SuppressWarnings("static-method")
public class Issue0415 {
  @Test public void issue92_52() {
    trimminKof("100>>2")//
        .gives("25")//
        .stays();
  }

  @Test public void issue92_54() {
    trimminKof("100L>>2")//
        .gives("25L")//
        .stays();
  }

  @Test public void issue92_55() {
    trimminKof("100>>2L")//
        .gives("25")//
        .stays();
  }

  @Test public void issue92_56() {
    trimminKof("100<<2")//
        .gives("400")//
        .stays();
  }

  @Test public void issue92_57() {
    trimminKof("100L<<2")//
        .gives("400L")//
        .stays();
  }

  @Test public void issue92_58() {
    trimminKof("100<<2L")//
        .gives("400")//
        .stays();
  }

  @Test public void issue92_59() {
    trimminKof("100L<<2L")//
        .gives("400L")//
        .stays();
  }

  @Test public void issue92_60() {
    trimminKof("100L<<2L>>2L")//
        .gives("400L>>2L")//
        .gives("100L")//
        .stays();
  }
}

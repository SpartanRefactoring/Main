package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Ignored arithmetic tests of issue 92 (arithmetic simplification) are moved
 * here.
 * @since 2016 */
@SuppressWarnings("static-method") @Ignore("Do not compute shifts, they have a reason") public class Issue415 {
  @Test public void issue92_52() {
    trimmingOf("100>>2").gives("25").stays();
  }
  @Test public void issue92_54() {
    trimmingOf("100L>>2").gives("25L").stays();
  }
  @Test public void issue92_55() {
    trimmingOf("100>>2L").gives("25").stays();
  }
  @Test public void issue92_56() {
    trimmingOf("100<<2").gives("400").stays();
  }
  @Test public void issue92_57() {
    trimmingOf("100L<<2").gives("400L").stays();
  }
  @Test public void issue92_58() {
    trimmingOf("100<<2L").gives("400").stays();
  }
  @Test public void issue92_59() {
    trimmingOf("100L<<2L").gives("400L").stays();
  }
  @Test public void issue92_60() {
    trimmingOf("100L<<2L>>2L").gives("400L>>2L").gives("100L").stays();
  }
}

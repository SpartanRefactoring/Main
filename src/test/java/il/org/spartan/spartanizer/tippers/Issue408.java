package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Test for issue 408
 * @author Matteo Orru'
 * @since 2016 */
@Ignore("Take note, you cannot convert x + 0 to x, unless you know that x is not a string")
public class Issue408 {
  @Test @SuppressWarnings("static-method") public void issue408_01() {
    trimmingOf("0+x").gives("x").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_02() {
    trimmingOf("0+(0+x+y+(4))").gives("x+y+4").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_02b() {
    trimmingOf("(0+x)+y").gives("x+y").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03() {
    trimmingOf("0+x+y+4").gives("x+y+4").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03b() {
    trimmingOf("0+x+y+4+5").gives("x+y+9").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03b1() {
    trimmingOf("0+(x + 0 + y + (x + 2))").gives("x+y+x+2").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03c() {
    trimmingOf("0+x+y+4+z").gives("x+y+4+z").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03d() {
    trimmingOf("0+x+y+4+z+w").gives("x+y+4+z+w").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03e() {
    trimmingOf("0+x+0+y").gives("x+y").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03f() {
    trimmingOf("0+x+0+y+4").gives("x+y+4").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_03g() {
    trimmingOf("0+x+0+0+0+y+4").gives("x+y+4").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_033() {
    trimmingOf("0+x+y+4+z+5").gives("x+y+4+z+5").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_04() {
    trimmingOf("x+0").gives("x").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_05() {
    trimmingOf("0+x+3").gives("x+3").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_06() {
    trimmingOf("x+0+y").gives("x+y").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_07() {
    trimmingOf("0+(0+x+y+(4))").gives("x+y+4").stays();
  }

  @Test @SuppressWarnings("static-method") public void issue408_08() {
    trimmingOf("0+0+x+4*y").gives("x+4*y").stays();
  }
}

package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0129 {
  @Test public void issue129_01() {
    trimmingOf("$ += s + (new Integer(i) + \"\")")//
        .gives("$ += s + (Integer.valueOf(i) + \"\")")//
        .stays();
  }
  @Test public void issue129_02() {
    trimmingOf("1 + 2 - (x+1)")//
        .gives("1+2-x-1")//
        .gives("3-x-1")//
        .stays();
  }
  @Test public void issue129_03() {
    trimmingOf("1 + 2 + (x+1)")//
        .gives("3 + x + 1")//
        .stays();
  }
  @Test public void issue129_04() {
    trimmingOf("\"\" + 0 + (x - 7)")//
        .gives("0 + \"\" + (x - 7)")//
        .stays();
  }
  @Test public void issue129_05() {
    trimmingOf("x + 5 + y + 7.0 +1.*f(3)")//
        .stays();
  }
}

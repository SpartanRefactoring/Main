package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Checking that the tipper handling multiplication with zero is working well
 * @author Dor Ma'ayan
 * @since 2016-09-25 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0193 {
  @Test public void t10() {
    trimmingOf("x*0")//
        .gives("0")//
        .stays();
  }
  @Test public void t20() {
    trimmingOf("0*x")//
        .gives("0")//
        .stays();
  }
  @Test public void t30() {
    trimmingOf("(x+y)*0")//
        .gives("0")//
        .stays();
  }
  @Test public void t40() {
    trimmingOf("calc()*0")//
        .gives("0*calc()")//
        .stays();
  }
  @Test public void t50() {
    trimmingOf("0*(f())")//
        .stays();
  }
  @Test public void t60() {
    trimmingOf("0*(new int[f()])")//
        .stays();
  }
  @Test public void t70() {
    trimmingOf("x*0*new int[f()]")//
        .stays();
  }
  @Test public void t80() {
    trimmingOf("calc()*x*0")//
        .stays();
  }
}

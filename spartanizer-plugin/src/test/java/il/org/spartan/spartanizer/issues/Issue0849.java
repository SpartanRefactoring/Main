package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Jan 6, 2017 */
@Ignore("Dor Ma'yan")
@SuppressWarnings("static-method")
public class Issue0849 {
  @Test public void test0() {
    trimmingOf("a-1+2")//
        .gives("a+1")//
        .stays();
  }
  @Test public void test1() {
    trimmingOf("a-1-2")//
        .gives("a-3")//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("2+a-1-9")//
        .gives("2+a-8")//
        .stays();
  }
  @Test public void test3() {
    trimmingOf("3+a+1+2")//
        .gives("a+1")//
        .stays();
  }
}

package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** TODO Yossi Gil LocalVariableInitializedStatement description
 * @author Yossi Gil
 * @since Jan 6, 2017 */
@Ignore("Dor Ma'yan")
@SuppressWarnings("static-method")
public class Issue0849 {
  @Test public void test0() {
    trimminKof("a-1+2")//
        .gives("a+1")//
        .stays();
  }

  @Test public void test1() {
    trimminKof("a-1-2")//
        .gives("a-3")//
        .stays();
  }

  @Test public void test2() {
    trimminKof("2+a-1-9")//
        .gives("2+a-8")//
        .stays();
  }

  @Test public void test3() {
    trimminKof("3+a+1+2")//
        .gives("a+1")//
        .stays();
  }
}

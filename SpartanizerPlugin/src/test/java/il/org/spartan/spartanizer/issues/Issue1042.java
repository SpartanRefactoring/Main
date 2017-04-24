package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link ModifierFinalAbstractMethodRedundant}
 * @author Yossi Gil
 * @since 2016-12-23 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1042 {
  @Test public void test0() {
    trimminKof("abstract int f(final int a, final int b);")//
        .gives("abstract int f(int a, int b);")//
        .stays();
  }

  @Test public void test1() {
    trimminKof("interface a { int f(final int a, final int b);}")//
        .gives("interface a { int f(int a, int b);}")//
        .stays();
  }

  @Test public void test2() {
    trimminKof("int f(final int a, final int b) { return a + b; }")//
        .stays();
  }

  @Test public void test3() {
    trimminKof("interface a { default int f(final int a, final int b) { return a + b;}")//
        .stays();
  }

  @Test public void test4() {
    trimminKof("for (final File f:fs);") //
        .stays();
  }
}

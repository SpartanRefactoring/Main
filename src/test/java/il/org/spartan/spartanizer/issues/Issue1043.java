package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link ModifierFinalTryResourceRedundant}
 * @author Yossi Gil
 * @since 2016-12-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1043 {
  @Test public void test0() {
    trimminKof("try (final File f = new File()){i++;}") //
        .gives("try (File f = new File()) {++i;}") //
        .stays();
  }

  @Test public void test1() {
    trimminKof("try (final File f = new File(); final B b = new B()){i++;}") //
        .gives("try (File f = new File(); B b = new B()) {++i;}") //
        .stays();
  }
}

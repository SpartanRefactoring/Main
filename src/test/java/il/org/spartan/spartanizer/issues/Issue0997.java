package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO Yossi Gil LocalVariableInitializedStatement description
 * @author Yossi Gil
 * @since 2016-12-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0997 {
  @Test public void a0() {
    trimminKof("@interface A{final int a=4;}")//
        .gives("@interface A{int a=4;}")//
        .stays();
  }

  @Test public void a1() {
    trimminKof("@interface A{final int a=4;final char c;}")//
        .gives("@interface A{int a=4;char c;}")//
        .stays();
  }
}

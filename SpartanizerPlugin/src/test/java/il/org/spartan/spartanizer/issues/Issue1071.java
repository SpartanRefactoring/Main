package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Tests of {@link InitializerEmptyRemove}
 * @author Yossi Gil
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1071 {
  @Test public void a() {
    trimminKof("class X extends Object{ }")//
        .gives("class X{}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("class X extends java.lang.Object { }")//
        .gives("class X{}")//
        .stays();
  }

  @Test public void c() {
    trimminKof("public class X extends java.lang.object { }")//
        .stays();
  }

  @Test public void d() {
    trimminKof("public class X extends y { }")//
        .stays();
  }
}

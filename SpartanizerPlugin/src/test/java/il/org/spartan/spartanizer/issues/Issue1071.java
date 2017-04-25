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
    trimmingOf("class X extends Object{ }")//
        .gives("class X{}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("class X extends java.lang.Object { }")//
        .gives("class X{}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("public class X extends java.lang.object { }")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("public class X extends y { }")//
        .stays();
  }
}

package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Tests of {@link InitializerEmptyRemove}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1071 {
  @Test public void a() {
    topDownTrimming("class X extends Object{ }")//
        .gives("class X{}")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("class X extends java.lang.Object { }")//
        .gives("class X{}")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("public class X extends java.lang.object { }")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("public class X extends y { }")//
        .stays();
  }
}

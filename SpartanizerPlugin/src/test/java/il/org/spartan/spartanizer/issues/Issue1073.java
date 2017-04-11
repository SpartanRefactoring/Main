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
public class Issue1073 {
  @Test public void a() {
    topDownTrimming("class X { {} }").gives("class X{}")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("class X { {} }").gives("class X{}")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("class X { static {} }").gives("class X{}")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("class X { /** JavaDOC */ {} }").stays();
  }

  @Test public void e() {
    topDownTrimming("class X { /** JavaDOC */ static {} }").stays();
  }
}

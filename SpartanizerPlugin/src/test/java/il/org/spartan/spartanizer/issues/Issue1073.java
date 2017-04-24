package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Tests of {@link InitializerEmptyRemove}
 * @author Yossi Gil
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1073 {
  @Test public void a() {
    trimminKof("class X { {} }").gives("class X{}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("class X { {} }").gives("class X{}")//
        .stays();
  }

  @Test public void c() {
    trimminKof("class X { static {} }").gives("class X{}")//
        .stays();
  }

  @Test public void d() {
    trimminKof("class X { /** JavaDOC */ {} }").stays();
  }

  @Test public void e() {
    trimminKof("class X { /** JavaDOC */ static {} }").stays();
  }
}

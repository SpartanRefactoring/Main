package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Tests of {@link InitializerEmptyRemove}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1073 {
  @Test public void a() {
    trimmingOf("class X { {} }").gives("class X{}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("class X { {} }").gives("class X{}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("class X { static {} }").gives("class X{}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("class X { /** JavaDOC */ {} }").stays();
  }

  @Test public void e() {
    trimmingOf("class X { /** JavaDOC */ static {} }").stays();
  }
}

package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0359 {
  @Test public void b() {
    trimmingOf("int i;++i;")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("int i=f();")//
        .stays();
  }

  @Test public void t20() {
    trimmingOf("for(;b==q;){int i;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
}

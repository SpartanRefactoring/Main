package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
@Ignore public class Issue337 {
  @Test public void t18() {
    trimmingOf("while(b==q){int i;double tipper; x=tipper+i;}")//
        .gives("for(;b==q;x=tipper+i){int i;double tipper;}")//
        .stays();
  }
}

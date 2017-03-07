package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0337 {
  @Test public void t18() {
    trimmingOf("while(b==q){int i;double tipper; x=tipper+i;}")//
        .stays();
  }
}

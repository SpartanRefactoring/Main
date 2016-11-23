package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Tests of {@link ThisClass#thatFunction}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue857 {
  @Test public void report234() {
    trimmingOf(//
        "public static A a() {" + //
            " A b = \"str\";" + //
            " B.d(b);" + //
            " return b + \"\";" + //
            "}"//
    ).gives(//
        "public static A a() {" + //
            " A $ = \"str\";" + //
            " B.d($);" + //
            " return $ + \"\";" + //
            "}"//
    ).stays();
  }
}

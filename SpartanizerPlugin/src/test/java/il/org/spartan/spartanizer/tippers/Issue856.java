package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Tests of inline into next statment even if not last in block
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue856 {
  @Test public void a() {
    trimmingOf(//
        "public static A a() {" + //
            " A b = \"one expression\";" + //
            " B.d(b);" + //
            " return \"and another\";" + //
            "}"//
    ).gives(//
        "public static A a() {" + //
            " B.d(\"one expression\");" + //
            " return \"and another\";" + //
            "}"//
    ).stays();
  }
}

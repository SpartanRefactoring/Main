package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" }) public class Issue307 {
  @Test public void a() {
    trimmingOf(
        "public ASTNode inclusiveLastFrom(final ASTNode n) {for (ASTNode $ = inclusiveFrom(n), p = $; ; p = from(p), $ = p)if (p == null)return $;}")
            .gives(
                "public ASTNode inclusiveLastFrom(final ASTNode n) {for (ASTNode $ = inclusiveFrom(n), ¢ = $; ; ¢ = from(¢), $ = ¢)if (¢ == null)return $;}")
            .stays();
  }
}

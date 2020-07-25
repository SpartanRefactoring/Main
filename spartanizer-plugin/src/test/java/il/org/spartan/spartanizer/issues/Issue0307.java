package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0307 {
  // p is not JD so no renaming happening
  @Test public void a() {
    trimmingOf(
        "public ASTNode inclusiveLastFrom(final ASTNode n) {for (ASTNode $ = inclusiveFrom(n), p = $; ; p = from(p), $ = p)if (p == null)return $;}")
            .stays();
  }
  @Test public void b() {
    trimmingOf(
        "public ASTNode inclusiveLastFrom(final ASTNode n) {for (ASTNode $ = inclusiveFrom(n), node = $; ; node = from(node), $ = node)if (node == null)return $;}")
            .gives(
                "public ASTNode inclusiveLastFrom(final ASTNode n) {for (ASTNode $ = inclusiveFrom(n), ¢ = $; ; ¢ = from(¢), $ = ¢)if (¢ == null)return $;}")
            .stays();
  }
}

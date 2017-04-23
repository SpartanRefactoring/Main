package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.jdt.core.dom.*;

/** Convert visibility levels into comparable integers
 * @author Yossi Gil
 * @since 2017-04-22 */
public interface visibility {
  static int of(final BodyDeclaration d) {
    final int m = d.getModifiers();
    return Modifier.isPublic(m) ? 4 : Modifier.isProtected(m) ? 3 : Modifier.isPrivate(m) ? 1 : 2;
  }
}

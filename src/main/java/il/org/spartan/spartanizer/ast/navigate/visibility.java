package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.jdt.core.dom.*;

/** Convert visibility levels into comparable integers
 * @author Yossi Gil
 * @since 2017-04-22 */
public interface visibility {
  static int of(final BodyDeclaration d) {
    final int m = d.getModifiers();
    if (Modifier.isPublic(m))
      return 4;
    if (Modifier.isProtected(m))
      return 3;
    if (Modifier.isDefault(m))
      return 2;
    if (Modifier.isPrivate(m))
      return 1;
    return 0;
  }
}

package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.jdt.core.dom.*;

/** Convert visibility levels into comparable integers
 * @author Yossi Gil
 * @since 2017-04-22 */
public interface visibility {
  static int of(final BodyDeclaration ¢) {
    final int $ = ¢.getModifiers();
    return Modifier.isPublic($) ? 4 : Modifier.isProtected($) ? 3 : Modifier.isPrivate($) ? 1 : 2;
  }
}

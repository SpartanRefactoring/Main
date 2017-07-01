package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.jdt.core.dom.*;

/** Convert visibility levels into comparable {@code int}s; {@code public} is 4,
 * protected is 3, private is 1, default is 2.
 * @author Yossi Gil
 * @since 2017-04-22 */
public interface visibility {
  static int of(final BodyDeclaration ¢) {
    final int ret = ¢.getModifiers();
    return Modifier.isPublic(ret) ? 4 : Modifier.isProtected(ret) ? 3 : Modifier.isPrivate(ret) ? 1 : 2;
  }
}

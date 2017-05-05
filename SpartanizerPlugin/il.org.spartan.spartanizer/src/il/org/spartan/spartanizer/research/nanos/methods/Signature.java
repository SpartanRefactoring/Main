package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method signature (no body)
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-13 */
public class Signature extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x6F61291D30836F0BL;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return noBody(¢);
  }
}

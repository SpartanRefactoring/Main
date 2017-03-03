package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method signature (no body)
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
public class Signature extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -8025741216295644939L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return noBody(¢);
  }
}

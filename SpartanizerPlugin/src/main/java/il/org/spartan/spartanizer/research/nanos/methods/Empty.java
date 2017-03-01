package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method with empty body
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
public class Empty extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -4795965741088687841L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢);
  }
}

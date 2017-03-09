package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** Including static setters.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-10-22 */
public class Setter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -4815724471383478205L;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    ___.nothing();
    return notConstructor(¢)//
        && notEmpty(¢) //
        && setter(¢);
  }
}

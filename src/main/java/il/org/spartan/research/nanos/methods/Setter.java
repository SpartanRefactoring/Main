package il.org.spartan.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.research.nanos.common.*;
import il.org.spartan.utils.*;

/** Including static setters.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-10-22 */
public class Setter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x42D4E3E454F7CFBDL;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    ___.nothing();
    return notConstructor(¢)//
        && notEmpty(¢) //
        && setter(¢);
  }
}

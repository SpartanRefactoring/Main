package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method with empty body
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-13 */
public class Empty extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -4795965741088687841L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢);
  }

  @Override @NotNull public String nanoName() {
    return Default.class.getSimpleName();
  }
}

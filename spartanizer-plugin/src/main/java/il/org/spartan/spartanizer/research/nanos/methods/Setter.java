package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Including static setters.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-10-22 */
public class Setter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x42D4E3E454F7CFBDL;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    return notConstructor(¢)//
        && notEmpty(¢) //
        && setter(¢);
  }
}

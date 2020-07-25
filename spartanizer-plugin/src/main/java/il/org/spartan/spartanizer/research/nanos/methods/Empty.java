package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Method with empty body
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-13 */
public class Empty extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x428EB16E7088C6E1L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢);
  }
  @Override public String tipperName() {
    return DefaultValue.class.getSimpleName();
  }
}

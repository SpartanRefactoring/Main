package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Constructor just delegating to another
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-21 */
public class ThisConstructor extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x3E5BD176F797AE7L;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        && iz.constructorInvocation(onlyStatement(¢));
  }
  @Override public String tipperName() {
    return "This";
  }
}

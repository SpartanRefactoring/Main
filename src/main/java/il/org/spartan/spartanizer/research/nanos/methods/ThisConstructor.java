package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constructor just delegating to another
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-21 */
public class ThisConstructor extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 280838460132653799L;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        && iz.constructorInvocation(onlyStatement(¢));
  }
}

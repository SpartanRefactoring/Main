package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Fluenter extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return returnTypeSameAsClass(¢)//
        && lastReturnsThis(¢);
  }
}
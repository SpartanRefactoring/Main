package il.org.spartan.spartanizer.research.nanos.characteristics;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class Fluenter extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return returnTypeSameAsClass(¢)//
        && lastReturnsThis(¢);
  }
}

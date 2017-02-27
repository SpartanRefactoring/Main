package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class Fluenter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 5268571128871336584L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return returnTypeSameAsClass(¢)//
        && lastReturnsThis(¢);
  }
}

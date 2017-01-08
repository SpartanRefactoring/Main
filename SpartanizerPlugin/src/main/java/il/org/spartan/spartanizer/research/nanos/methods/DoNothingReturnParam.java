package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DoNothingReturnParam extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢) && hazOneStatement(¢) && returnsParam(¢) && returnTypeSameAsParameter(¢);
  }
}

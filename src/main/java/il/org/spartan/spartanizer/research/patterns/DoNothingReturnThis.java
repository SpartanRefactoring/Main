package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DoNothingReturnThis extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && returnsThis(¢);
  }
}

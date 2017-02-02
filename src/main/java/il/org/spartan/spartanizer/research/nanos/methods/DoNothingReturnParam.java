package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class DoNothingReturnParam extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢) && hazOneStatement(¢) && returnsParam(¢) && returnTypeSameAsParameter(¢);
  }

  @NotNull
  @Override public Category category() {
    return Category.Default;
  }
}

package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;

/** Method returning parameter without doing anything else
 * @author Ori Marcovitch */
public class DoNothingReturnParam extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 5255790606867281484L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢)//
        && hazOneStatement(¢)//
        && returnsParam(¢)//
        && returnTypeSameAsParameter(¢);
  }

  @NotNull @Override public Category category() {
    return Category.Default;
  }
}

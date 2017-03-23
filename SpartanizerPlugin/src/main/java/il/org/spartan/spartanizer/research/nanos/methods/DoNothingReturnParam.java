package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method returning parameter without doing anything else
 * @author Ori Marcovitch */
public class DoNothingReturnParam extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x48F051B5E828B64CL;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢)//
        && hazOneStatement(¢)//
        && returnsParam(¢)//
        && returnTypeSameAsParameter(¢);
  }

  @Override @NotNull public Category category() {
    return Category.Default;
  }
}

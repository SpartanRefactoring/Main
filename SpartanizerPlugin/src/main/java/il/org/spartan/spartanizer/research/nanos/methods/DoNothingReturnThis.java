package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;

/** Method returning this without doing anything else
 * @author Ori Marcovitch */
public class DoNothingReturnThis extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 6445098077653629920L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && returnsThis(¢);
  }

  @NotNull @Override public Category category() {
    return Category.Default;
  }
}

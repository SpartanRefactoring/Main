package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method returning this without doing anything else
 * @author Ori Marcovitch */
public class DoNothingReturnThis extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && returnsThis(¢);
  }

  @Override public Category category() {
    return Category.Default;
  }
}

package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method returning this without doing anything else
 * @author Ori Marcovitch */
public class DoNothingReturnThis extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x5971968DD518FBE0L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && returnsThis(¢);
  }
  @Override public Category category() {
    return Category.Default;
  }
}

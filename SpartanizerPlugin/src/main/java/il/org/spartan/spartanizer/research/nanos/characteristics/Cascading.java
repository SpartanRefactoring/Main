package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Nano to match setter method which returns this
 * @author Ori Marcovitch */
public class Cascading {
  public static class FluentSetter extends JavadocMarkerNanoPattern {
    private static final long serialVersionUID = -2893413461939243057L;

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      return hazAtLeastTwoStatements(¢)//
          && lastReturnsThis(¢)//
          && setter(withoutLast(¢));
    }
  }
}

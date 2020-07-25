package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Nano to match setter method which returns this
 * @author Ori Marcovitch */
public class Cascading {
  public static class FluentSetter extends JavadocMarkerNanoPattern {
    private static final long serialVersionUID = -0x28277836B338C831L;

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      return hazAtLeastTwoStatements(¢)//
          && lastReturnsThis(¢)//
          && setter(withoutLast(¢));
    }
  }
}

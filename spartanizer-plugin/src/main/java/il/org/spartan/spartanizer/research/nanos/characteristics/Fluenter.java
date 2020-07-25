package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Fluent method characteristic
 * @author Ori Marcovitch
 * @since 2016 */
public class Fluenter extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x491DB98712413A88L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return returnTypeSameAsClass(¢)//
        && lastReturnsThis(¢);
  }
}

package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

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
  @Override public Category category() {
    return Category.Default;
  }
}

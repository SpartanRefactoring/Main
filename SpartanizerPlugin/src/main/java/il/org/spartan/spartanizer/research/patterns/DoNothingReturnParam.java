package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DoNothingReturnParam extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢) && hazOneStatement(¢) && returnsParam(¢) && returnTypeSameAsParameter(¢);
  }

  /** @param ¢
   * @return */
  private boolean returnsParam(final MethodDeclaration ¢) {
    return identifier(az.name(expression(az.returnStatement(onlyStatement(¢))))).equals(identifier(name(onlyParameter(¢))));
  }
}

package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Independent extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    final List<String> enviroment = step.parametersNames(d);
    for (AbstractTypeDeclaration ¢ = ancestorType(d); ¢ != null; ¢ = ancestorType(¢))
      if (iz.typeDeclaration(¢))
        enviroment.addAll(step.fieldDeclarationsNames(az.typeDeclaration(¢)));
    for (MethodDeclaration ¢ = ancestorMethod(d); ¢ != null; ¢ = ancestorMethod(¢))
      if (iz.methodDeclaration(¢))
        enviroment.addAll(step.parametersNames(az.methodDeclaration(¢)));
    for (final String ¢ : enviroment)
      if (analyze.dependencies(step.body(d)).stream().map(x -> x + "").collect(Collectors.toSet()).contains(¢))
        return false;
    return true;
  }

  private static AbstractTypeDeclaration ancestorType(final ASTNode ¢) {
    return searchAncestors.forContainingType().from(¢);
  }

  private static MethodDeclaration ancestorMethod(final ASTNode ¢) {
    return searchAncestors.forContainingMethod().from(¢);
  }
}
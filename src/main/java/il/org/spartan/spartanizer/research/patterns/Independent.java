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
    List<String> enviroment = step.parametersNames(d);
    for (AbstractTypeDeclaration ¢ = ancestorType(d); ¢ != null; ¢ = ancestorType(¢))
      if (iz.typeDeclaration(¢))
        enviroment.addAll(step.fieldDeclarationsNames(az.typeDeclaration(¢)));
    Set<String> dependencies = analyze.dependencies(step.body(d)).stream().map(x -> x + "").collect(Collectors.toSet());
    for (String ¢ : enviroment)
      if (dependencies.contains(¢))
        return false;
    return true;
  }
  private static AbstractTypeDeclaration ancestorType(final ASTNode ¢) {
    return searchAncestors.forContainingType().from(¢);
  }
}
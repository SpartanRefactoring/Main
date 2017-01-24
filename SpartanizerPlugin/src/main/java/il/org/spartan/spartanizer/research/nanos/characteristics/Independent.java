package il.org.spartan.spartanizer.research.nanos.characteristics;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class Independent extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    final List<String> $ = parametersNames(d);
    for (AbstractTypeDeclaration ¢ = ancestorType(d); ¢ != null; ¢ = ancestorType(¢))
      if (iz.typeDeclaration(¢))
        $.addAll(fieldDeclarationsNames(az.typeDeclaration(¢)));
    for (MethodDeclaration ¢ = ancestorMethod(d); ¢ != null; ¢ = ancestorMethod(¢))
      if (iz.methodDeclaration(¢))
        $.addAll(parametersNames(az.methodDeclaration(¢)));
    return $.stream().allMatch(¢ -> !analyze.dependencies(body(d)).stream().map(x -> x + "").collect(Collectors.toSet()).contains(¢));
  }

  private static AbstractTypeDeclaration ancestorType(final ASTNode ¢) {
    return yieldAncestors.untilContainingType().from(¢);
  }

  private static MethodDeclaration ancestorMethod(final ASTNode ¢) {
    return yieldAncestors.untilContainingMethod().from(¢);
  }
}

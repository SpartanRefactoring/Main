package il.org.spartan.spartanizer.research.nanos.characteristics;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static java.util.stream.Collectors.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method which is independent on he parameters
 * @author Ori Marcovitch */
public class Oblivious extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    final List<String> $ = parametersNames(d);
    for (AbstractTypeDeclaration ¢ = ancestorType(d); ¢ != null; ¢ = ancestorType(¢))
      if (iz.typeDeclaration(¢))
        $.addAll(fieldDeclarationsNames(az.typeDeclaration(¢)));
    for (MethodDeclaration ¢ = ancestorMethod(d); ¢ != null; ¢ = ancestorMethod(¢))
      if (iz.methodDeclaration(¢))
        $.addAll(parametersNames(az.methodDeclaration(¢)));
    return $.stream().noneMatch(λ -> analyze.dependencies(body(d)).stream().map(String::toString).collect(toSet()).contains(λ));
  }

  private static AbstractTypeDeclaration ancestorType(final ASTNode ¢) {
    return yieldAncestors.untilContainingType().from(¢);
  }

  private static MethodDeclaration ancestorMethod(final ASTNode ¢) {
    return yieldAncestors.untilContainingMethod().from(¢);
  }
}

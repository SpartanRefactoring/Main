package il.org.spartan.spartanizer.research.nanos.characteristics;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method which is independent on he parameters
 * @author Ori Marcovitch */
public class Oblivious extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 2377142689255053588L;

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    @Nullable final List<String> $ = parametersNames(d);
    for (@Nullable AbstractTypeDeclaration ¢ = ancestorType(d); ¢ != null; ¢ = ancestorType(¢))
      if (iz.typeDeclaration(¢))
        $.addAll(fieldDeclarationsNames(az.typeDeclaration(¢)));
    for (@Nullable MethodDeclaration ¢ = ancestorMethod(d); ¢ != null; ¢ = ancestorMethod(¢))
      if (iz.methodDeclaration(¢))
        $.addAll(parametersNames(az.methodDeclaration(¢)));
    return $.stream().noneMatch(λ -> analyze.dependencies(body(d)).stream().map(String::toString).collect(toSet()).contains(λ));
  }

  @Nullable private static AbstractTypeDeclaration ancestorType(final ASTNode ¢) {
    return yieldAncestors.untilContainingType().from(¢);
  }

  @Nullable private static MethodDeclaration ancestorMethod(final ASTNode ¢) {
    return yieldAncestors.untilContainingMethod().from(¢);
  }
}

package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.fieldDeclarationsNames;
import static il.org.spartan.spartanizer.ast.navigate.step.parametersNames;
import static java.util.stream.Collectors.toSet;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.navigate.analyze;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Method which is independent on he parameters
 * @author Ori Marcovitch */
public class Oblivious extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x20FD4EAE6576D914L;

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

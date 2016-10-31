package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class TypeChecker extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (step.body(d) == null || step.statements(step.body(d)) == null || step.statements(step.body(d)).size() != 1
        || !iz.returnStatement(step.statements(step.body(d)).get(0)) || step.parameters(d).size() != 1)
      return false;
    final ReturnStatement s = az.returnStatement(step.statements(step.body(d)).get(0));
    return iz.instanceofExpression(step.expression(s)) && ("boolean").equals(step.returnType(d) + "")
        && (step.parameters(d).get(0).getName() + "").equals(step.left(az.instanceofExpression(step.expression(s))) + "");
  }
}

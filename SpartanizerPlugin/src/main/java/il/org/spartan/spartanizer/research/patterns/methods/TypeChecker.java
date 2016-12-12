package il.org.spartan.spartanizer.research.patterns.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class TypeChecker extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (step.body(¢) == null || step.statements(step.body(¢)) == null || step.statements(step.body(¢)).size() != 1
        || !iz.returnStatement(step.statements(step.body(¢)).get(0)) || step.parameters(¢).size() != 1)
      return false;
    final ReturnStatement $ = az.returnStatement(step.statements(step.body(¢)).get(0));
    return iz.instanceofExpression(step.expression($)) && "boolean".equals(step.returnType(¢) + "")
        && (step.parameters(¢).get(0).getName() + "").equals(step.left(az.instanceofExpression(step.expression($))) + "");
  }
}

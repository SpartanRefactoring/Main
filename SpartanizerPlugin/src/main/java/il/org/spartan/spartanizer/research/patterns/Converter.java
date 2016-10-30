package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Converter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (step.body(d) == null || step.statements(step.body(d)) == null || step.statements(step.body(d)).size() != 1
        || !iz.returnStatement(step.statements(step.body(d)).get(0)) || step.parameters(d).size() != 1)
      return false;
    final ReturnStatement s = az.returnStatement(step.statements(step.body(d)).get(0));
    if (!iz.castExpression(step.expression(s)))
      return false;
    final SingleVariableDeclaration p = step.parameters(d).get(0);
    final CastExpression c = az.castExpression(step.expression(s));
    return (c.getType() + "").equals(step.returnType(d) + "") && (p.getName() + "").equals(c.getExpression() + "");
  }
}

package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DownCaster extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (step.body(d) == null)
      return false;
    final CastExpression c = az.castExpression(expression(az.returnStatement(lisp.onlyOne(statements(body(d))))));
    final SingleVariableDeclaration p = lisp.onlyOne(parameters(d));
    return c != null && p != null && (c.getType() + "").equals(step.returnType(d) + "") && (p.getName() + "").equals(expression(c) + "");
  }
}

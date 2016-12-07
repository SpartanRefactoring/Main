package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.onlyOne;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DownCaster extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (body(d) == null)
      return false;
    final CastExpression c = az.castExpression(expression(az.returnStatement(onlyOne(statements(body(d))))));
    final SingleVariableDeclaration p = onlyOne(parameters(d));
    return c != null && p != null && (type(c) + "").equals(returnType(d) + "") && (name(p) + "").equals(expression(c) + "");
  }
}

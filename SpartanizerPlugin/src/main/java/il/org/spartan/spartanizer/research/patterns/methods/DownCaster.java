package il.org.spartan.spartanizer.research.patterns.methods;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.onlyOne;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DownCaster extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (!hazOneStatement(d))
      return false;
    final CastExpression $ = az.castExpression(expression(az.returnStatement(onlyOne(statements(d)))));
    final SingleVariableDeclaration p = onlyOne(parameters(d));
    return $ != null && p != null && returnTypeSameAs(d, type($)) && same(name(p), expression($));
  }
}

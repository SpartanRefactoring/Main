package il.org.spartan.spartanizer.research.patterns.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DownCaster extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (!hazOneStatement(¢))
      return false;
    final CastExpression $ = az.castExpression(expression(az.returnStatement(onlyStatement(¢))));
    return returnTypeSameAs(¢, type($)) && same(name(onlyParameter(¢)), expression($));
  }
}

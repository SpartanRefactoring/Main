package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class SelfCaster extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x1EF61FDDF0B04334L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (!hazOneStatement(¢) || hazParameters(¢))
      return false;
    final CastExpression $ = az.castExpression(expression(az.returnStatement(onlyStatement(¢))));
    return returnTypeSameAs(¢, type($))//
        && iz.thisExpression(expression($));
  }
}

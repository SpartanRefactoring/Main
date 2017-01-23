package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since 2016 
 */

public class SelfCaster extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (!hazOneStatement(¢) || hazParameters(¢))
      return false;
    final CastExpression $ = az.castExpression(expression(az.returnStatement(onlyStatement(¢))));
    return returnTypeSameAs(¢, type($))//
        && iz.thisExpression(expression($));
  }
}


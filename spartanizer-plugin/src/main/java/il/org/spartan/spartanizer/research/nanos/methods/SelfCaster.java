package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

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

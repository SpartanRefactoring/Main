package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Down Caster
 * @author Ori Marcovitch */
public class Down {
  public static class Caster extends JavadocMarkerNanoPattern {
    private static final long serialVersionUID = 0x775920ED3B2F78F8L;

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      if (!hazOneStatement(¢))
        return false;
      final CastExpression $ = az.castExpression(expression(az.returnStatement(onlyStatement(¢))));
      return returnTypeSameAs(¢, type($))//
          && same(name(onlyParameter(¢)), expression($));
    }
  }
}

package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Setter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.patternTipper("this.$N", "", "");

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    if (parameters(¢) == null || parameters(¢).size() != 1 || body(¢) == null || iz.static¢(¢) || iz.constructor(¢) || statements(¢).size() != 1)
      return false;
    final Assignment a = az.assignment(expression(az.expressionStatement(onlyOne(statements(¢)))));
    return a != null && (iz.name(left(a)) || tipper.canTip(left(a))) && wizard.same(right(a), name(onlyOne(parameters(¢))));
  }
}

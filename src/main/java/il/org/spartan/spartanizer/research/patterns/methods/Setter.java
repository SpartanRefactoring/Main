package il.org.spartan.spartanizer.research.patterns.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Setter extends JavadocMarkerNanoPattern {
  private static final UserDefinedTipper<Expression> tipper = TipperFactory.patternTipper("this.$N", "", "");

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    if (!hazOneParameter(¢) || !hazOneStatement(¢) || iz.static¢(¢) || iz.constructor(¢))
      return false;
    final Assignment $ = az.assignment(expression(az.expressionStatement(onlyStatement(¢))));
    return (iz.name(left($)) || tipper.canTip(left($))) && wizard.same(right($), name(onlyParameter(¢)));
  }
}

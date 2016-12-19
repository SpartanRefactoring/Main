package il.org.spartan.spartanizer.research.patterns.characteristics;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class FluentSetter extends JavadocMarkerNanoPattern {
  private static final UserDefinedTipper<Block> tipper = TipperFactory.patternTipper("{this.$N = $N2; return this;}", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (!hazOneParameter(¢) || body(¢) == null || statements(¢).size() != 2)
      return false;
    final Assignment $ = az.assignment(expression(firstStatement(¢)));
    return $ != null && (iz.name(left($)) || tipper.canTip(body(¢))) && wizard.same(right($), name(onlyParameter(¢)));
  }
}

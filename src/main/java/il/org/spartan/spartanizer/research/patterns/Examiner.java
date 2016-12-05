package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Examiner extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<ReturnStatement> tipper = TipperFactory.patternTipper("return $X;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return body(¢) != null && statements(¢).size() == 1 && haz.booleanReturnType(¢) && iz.returnStatement(onlyOne(statements(¢)))
        && tipper.canTip(az.returnStatement(onlyOne(statements(¢))));
  }
}

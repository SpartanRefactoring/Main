package il.org.spartan.spartanizer.research.patterns.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Examiner extends JavadocMarkerNanoPattern {
  private static final UserDefinedTipper<ReturnStatement> tipper = TipperFactory.patternTipper("return $X;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && haz.booleanReturnType(¢) && tipper.canTip(az.returnStatement(onlyStatement(¢)));
  }
}

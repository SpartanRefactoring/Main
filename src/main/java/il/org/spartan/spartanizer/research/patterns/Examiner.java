package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Examiner extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<ReturnStatement> tipper = TipperFactory.patternTipper("return $X;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (step.body(¢) == null || !haz.booleanReturnType(¢))
      return false;
    @SuppressWarnings("unchecked") final List<Statement> ss = ¢.getBody().statements();
    return ss.size() == 1 && iz.returnStatement(ss.get(0)) && tipper.canTip(az.returnStatement(ss.get(0)));
  }
}

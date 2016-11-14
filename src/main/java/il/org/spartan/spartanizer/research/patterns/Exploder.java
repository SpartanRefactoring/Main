package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 May collide with {@link IfNullThrow} */
public class Exploder extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<IfStatement> tipper = TipperFactory.patternTipper("if($X1) throw $X2;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (step.body(¢) == null)
      return false;
    @SuppressWarnings("unchecked") final List<Statement> ss = ¢.getBody().statements();
    return ss.size() == 1 && iz.ifStatement(ss.get(0)) && tipper.canTip(az.ifStatement(ss.get(0)));
  }
}

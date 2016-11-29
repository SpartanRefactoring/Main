package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** A carrier method is such one that calls another method (usually with same
 * name) and just adds parameters to the method.
 * @author Ori Marcovitch
 * @since 2016 */
public class Carrier extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<ReturnStatement> tipper = TipperFactory.patternTipper("return $N($A);", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (step.body(¢) == null || !haz.booleanReturnType(¢))
      return false;
    @SuppressWarnings("unchecked") final List<Statement> ss = ¢.getBody().statements();
    if (ss.size() != 1 || !iz.returnStatement(ss.get(0)) || !tipper.canTip(az.returnStatement(ss.get(0))))
      return false;
    final Expression e = step.expression(az.returnStatement(ss.get(0)));
    return iz.methodInvocation(e) && containsParameters(¢, e) && step.arguments(az.methodInvocation(e)).size() > step.parametersNames(¢).size();
  }

  private static boolean containsParameters(final MethodDeclaration ¢, final Expression x) {
    for (final String pn : step.parametersNames(¢))
      if (!step.arguments(az.methodInvocation(x)).stream().filter(n -> iz.name(n)).map(n -> az.name(n) + "").collect(Collectors.toList())
          .contains(pn))
        return false;
    return true;
  }
}

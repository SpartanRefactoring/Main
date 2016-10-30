package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Delegator extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static final UserDefinedTipper<ReturnStatement> tipper = TipperFactory.tipper("return $N($A);", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (step.body(¢) == null || !haz.booleanReturnType(¢))
      return false;
    @SuppressWarnings("unchecked") final List<Statement> ss = ¢.getBody().statements();
    if (ss.size() != 1 || !iz.returnStatement(ss.get(0)) || !tipper.canTip(az.returnStatement(ss.get(0))))
      return false;
    final Expression e = step.expression(az.returnStatement(ss.get(0)));
    return iz.methodInvocation(e) && step.parametersNames(¢).containsAll(dependencies(step.arguments(az.methodInvocation(e))));
  }

  /** @param arguments
   * @return */
  private static List<String> dependencies(final List<Expression> arguments) {
    final Set<Name> names = new HashSet<>();
    for (final Expression ¢ : arguments) {
      names.addAll(analyze.dependencies(¢));
      if (iz.name(¢))
        names.add(az.name(¢));
    }
    return new ArrayList<>(names).stream().map(n -> n + "").collect(Collectors.toList());
  }
}

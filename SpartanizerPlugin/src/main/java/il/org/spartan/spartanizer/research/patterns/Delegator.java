package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.onlyOne;

/** @author Ori Marcovitch
 * @since 2016 */
public class Delegator extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("return $N($A);", "", ""));
      add(TipperFactory.patternTipper("return $N1().$N($A);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (statements(¢) == null || statements(¢).size() != 1 || !anyTips(tippers, onlyOne(statements(¢))))
      return false;
    final Expression $ = expression(az.returnStatement(onlyOne(statements(¢))));
    return iz.methodInvocation($) && areAtomic(arguments(az.methodInvocation($)))
        && parametersNames(¢).containsAll(dependencies(arguments(az.methodInvocation($))));
  }

  /** @param arguments
   * @return */
  private static boolean areAtomic(final List<Expression> arguments) {
    return arguments.stream().allMatch(¢ -> iz.name(¢) || iz.literal(¢));
  }

  /** @param arguments
   * @return */
  protected static List<String> dependencies(final List<Expression> arguments) {
    final Set<String> $ = new HashSet<>();
    for (final Expression ¢ : arguments) {
      $.addAll(analyze.dependencies(¢));
      if (iz.name(¢))
        $.add(az.name(¢) + "");
    }
    return new ArrayList<>($).stream().collect(Collectors.toList());
  }
}

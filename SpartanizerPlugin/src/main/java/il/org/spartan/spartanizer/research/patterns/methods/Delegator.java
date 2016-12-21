package il.org.spartan.spartanizer.research.patterns.methods;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Delegator extends JavadocMarkerNanoPattern {
  private static Set<UserDefinedTipper<Expression>> tippers = new HashSet<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("$N($A)", "", ""));
      add(TipperFactory.patternTipper("$N1.$N($A)", "", ""));
      add(TipperFactory.patternTipper("$N1().$N($A)", "", ""));
      add(TipperFactory.patternTipper("(($T)$N1).$N($A)", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢) && (delegation(¢, onlyStatement(¢)) || delegation(¢, onlySynchronizedStatementStatement(¢)));
  }

  private static boolean delegation(final MethodDeclaration d, final Statement ¢) {
    final Expression $ = expression(¢);
    return $ != null//
        && anyTips(tippers, expression(¢))//
        && iz.methodInvocation($)//
        && arePseudoAtomic(arguments(az.methodInvocation($)), parametersNames(d))//
        && parametersNames(d).containsAll(dependencies(arguments(az.methodInvocation($))));
  }

  /** @param arguments
   * @return */
  private static boolean arePseudoAtomic(final List<Expression> arguments, final List<String> parametersNames) {
    return arguments.stream()
        .allMatch(¢ -> iz.name(¢)//
            || iz.literal(¢)//
            || iz.methodInvocation(¢) && safeContains(parametersNames, ¢));
  }

  private static boolean safeContains(final List<String> parametersNames, final Expression ¢) {
    return parametersNames != null && parametersNames.contains(identifier(az.name(expression(¢))));
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

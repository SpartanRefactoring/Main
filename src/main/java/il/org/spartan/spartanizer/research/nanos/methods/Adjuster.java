package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-22 */
public class Adjuster extends JavadocMarkerNanoPattern {
  private static final List<UserDefinedTipper<Expression>> tippers = new ArrayList<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$N($A)", "", ""));
      add(patternTipper("$N1.$N($A)", "", ""));
      add(patternTipper("$N1().$N($A)", "", ""));
      add(patternTipper("$N1().$N2().$N($A)", "", ""));
      add(patternTipper("$N1.$N2().$N($A)", "", ""));
      add(patternTipper("(($T)$N1).$N($A)", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && (adjuster(¢, onlyStatement(¢)) || adjuster(¢, onlySynchronizedStatementStatement(¢)));
  }

  private static boolean adjuster(final MethodDeclaration d, final Statement ¢) {
    final Expression $ = expression(¢);
    return $ != null//
        && anyTips(tippers, $)//
        && iz.methodInvocation($)//
        && arePseudoAtomic(arguments(az.methodInvocation($)), parametersNames(d))//
    ;
  }

  private static boolean arePseudoAtomic(final List<Expression> arguments, final List<String> parametersNames) {
    return arguments.stream()
        .allMatch(λ -> iz.name(λ)//
            || iz.methodInvocation(λ)//
                && (safeContainsCallee(parametersNames, λ)//
                    || parametersContainAllArguments(parametersNames, λ))//
    ) && arguments.stream().anyMatch(λ -> helps(parametersNames, λ));
  }

  private static boolean helps(final List<String> parametersNames, final Expression ¢) {
    return arguments(az.methodInvocation(¢)) != null//
        && !arguments(az.methodInvocation(¢)).isEmpty()//
        && parametersContainAllArguments(parametersNames, ¢);
  }

  private static boolean parametersContainAllArguments(final List<String> parametersNames, final Expression ¢) {
    return parametersNames.containsAll(arguments(az.methodInvocation(¢)).stream().map(λ -> λ + "").collect(Collectors.toList()));
  }

  private static boolean safeContainsCallee(final List<String> parametersNames, final Expression ¢) {
    return parametersNames != null && parametersNames.contains(identifier(az.name(expression(¢))));
  }
}

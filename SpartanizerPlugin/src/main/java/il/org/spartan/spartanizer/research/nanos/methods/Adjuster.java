package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Delegate to another method but apply some method on a parameter
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-22 */
public class Adjuster extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x588E33100686496FL;
  private static final Collection<UserDefinedTipper<Expression>> tippers = as.list(//
      patternTipper("$N($A)", "", ""), //
      patternTipper("$N1.$N($A)", "", ""), //
      patternTipper("$N1().$N($A)", "", ""), //
      patternTipper("$N1().$N2().$N($A)", "", ""), //
      patternTipper("$N1.$N2().$N($A)", "", ""), //
      patternTipper("(($T)$N1).$N($A)", "", "") //
  );

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

  private static boolean arePseudoAtomic(final Collection<Expression> arguments, final Collection<String> parametersNames) {
    return arguments.stream()
        .allMatch(λ -> iz.name(λ)//
            || iz.methodInvocation(λ)//
                && (safeContainsCallee(parametersNames, λ)//
                    || parametersContainAllArguments(parametersNames, λ))//
    ) && arguments.stream().anyMatch(λ -> helps(parametersNames, λ));
  }

  private static boolean helps(final Collection<String> parametersNames, final Expression ¢) {
    return arguments(az.methodInvocation(¢)) != null//
        && !arguments(az.methodInvocation(¢)).isEmpty()//
        && parametersContainAllArguments(parametersNames, ¢);
  }

  private static boolean parametersContainAllArguments(final Collection<String> parametersNames, final Expression ¢) {
    return parametersNames.containsAll(arguments(az.methodInvocation(¢)).stream().map(ASTNode::toString).collect(toList()));
  }

  private static boolean safeContainsCallee(final Collection<String> parametersNames, final Expression ¢) {
    return parametersNames != null && parametersNames.contains(identifier(az.name(expression(¢))));
  }
}

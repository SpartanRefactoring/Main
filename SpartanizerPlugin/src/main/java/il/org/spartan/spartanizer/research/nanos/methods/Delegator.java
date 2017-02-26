package il.org.spartan.spartanizer.research.nanos.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method delegating to another
 * @author Ori Marcovitch */
public class Delegator extends JavadocMarkerNanoPattern {
  private static final NanoPatternContainer<Expression> tippers = new NanoPatternContainer<Expression>() {
    static final long serialVersionUID = 1L;
    {
      patternTipper("$N($A)", "", "");
      patternTipper("$N1.$N($A)", "", "");
      patternTipper("$N1().$N($A)", "", "");
      patternTipper("$N1().$N2().$N($A)", "", "");
      patternTipper("$N1.$N2().$N($A)", "", "");
      patternTipper("(($T)$N1).$N($A)", "", "");
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && (delegation(¢, onlyStatement(¢))//
            || delegation(¢, onlySynchronizedStatementStatement(¢)))//
        || hazTwoStatements(¢)//
            && lastReturnsThis(¢)//
            && delegation(¢, firstStatement(¢));
  }

  private static boolean delegation(final MethodDeclaration d, final Statement ¢) {
    final Expression $ = expression(¢);
    return $ != null//
        && tippers.canTip(expression(¢))//
        && iz.methodInvocation($)//
        && arePseudoAtomic(arguments(az.methodInvocation($)), parametersNames(d))//
        && parametersNames(d).containsAll(analyze.dependencies(arguments(az.methodInvocation($))));
  }

  private static boolean arePseudoAtomic(final Collection<Expression> arguments, final Collection<String> parametersNames) {
    return arguments.stream()//
        .allMatch(//
            λ -> iz.name(λ) || iz.methodInvocation(λ) && safeContains(parametersNames, λ)//
    );
  }

  private static boolean safeContains(final Collection<String> parametersNames, final Expression ¢) {
    return parametersNames != null && parametersNames.contains(identifier(az.name(expression(¢))));
  }
}

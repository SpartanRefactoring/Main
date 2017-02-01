package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** A DefaultParametersAdder method is such one that calls another method
 * (usually with same name) and just adds parameters to the method.
 * @author Ori Marcovitch */
public class DefaultParametersAdder extends JavadocMarkerNanoPattern {
  private static final Set<UserDefinedTipper<Expression>> tippers = new HashSet<UserDefinedTipper<Expression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$N($A)", "", ""));
      add(patternTipper("$N1.$N2($A)", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && (parametersAdder(¢, onlyStatement(¢))//
            || parametersAdder(¢, onlySynchronizedStatementStatement(¢)));
  }

  private static boolean parametersAdder(final MethodDeclaration d, final Statement s) {
    return parametersAdder(d, expression(s));
  }

  private static boolean parametersAdder(final MethodDeclaration d, final Expression $) {
    return anyTips(tippers, $)//
        && iz.methodInvocation($)//
        && containsParameters(d, $)//
        && arguments(az.methodInvocation($)).size() > parametersNames(d).size()//
        && identifier(name(az.methodInvocation($))).equals(identifier(name(d)))//
    ;
  }

  private static boolean containsParameters(final MethodDeclaration ¢, final Expression x) {
    return arguments(az.methodInvocation(x)).stream().filter(iz::name).map(λ -> identifier(az.name(λ))).collect(Collectors.toList())
        .containsAll(parametersNames(¢));
  }

  @Override public Category category() {
    return Category.Default;
  }
}

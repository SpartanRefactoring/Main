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

/** A DefaultParametersAdder method is such one that calls another method
 * (usually with same name) and just adds parameters to the method.
 * @author Ori Marcovitch */
public class DefaultParametersAdder extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0xA668A27D86C8B6EL;
  private static final Collection<UserDefinedTipper<Expression>> tippers = as.list( //
      patternTipper("$N($A)", "", ""), //
      patternTipper("$N1.$N2($A)", "", "")) //
  ;

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
    return arguments(az.methodInvocation(x)).stream().filter(iz::name).map(λ -> identifier(az.name(λ))).collect(toList())
        .containsAll(parametersNames(¢));
  }

  @Override public String tipperName() {
    return "DefaultArguments";
  }
}

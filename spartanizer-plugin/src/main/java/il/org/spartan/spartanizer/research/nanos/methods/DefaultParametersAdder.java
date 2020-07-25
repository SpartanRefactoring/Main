package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.parametersNames;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;
import static java.util.stream.Collectors.toList;

import java.util.Collection;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

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

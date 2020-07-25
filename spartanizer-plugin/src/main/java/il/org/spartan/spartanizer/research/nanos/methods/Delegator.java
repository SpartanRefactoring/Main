package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.parametersNames;

import java.util.Collection;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.navigate.analyze;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternContainer;

/** Method delegating to another
 * @author Ori Marcovitch */
public class Delegator extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x384105006E3D59D7L;
  private static final NanoPatternContainer<Expression> tippers = new NanoPatternContainer<Expression>().add("$N($A)", "", "") //
      .add("$N1.$N($A)", "", "") //
      .add("$N1().$N($A)", "", "") //
      .add("$N1().$N2().$N($A)", "", "") //
      .add("$N1.$N2().$N($A)", "", "") //
      .add("(($T)$N1).$N($A)", "", "") //
  ;

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

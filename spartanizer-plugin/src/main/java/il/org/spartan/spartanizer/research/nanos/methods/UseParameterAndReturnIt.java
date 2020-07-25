package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.parameters;
import static il.org.spartan.spartanizer.ast.navigate.step.returnType;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class UseParameterAndReturnIt extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x5CA73E4455E35806L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢)//
        && notEmpty(¢)//
        && !iz.constructor(¢)//
        && !iz.voidType(returnType(¢))
        && returnStatements(¢).stream().map(λ -> expression(λ) + "").allMatch(λ -> λ.equals(identifier(name(the.onlyOneOf(parameters(¢))))));
  }
}

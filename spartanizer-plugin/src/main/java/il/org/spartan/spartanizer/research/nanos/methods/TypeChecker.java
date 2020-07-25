package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.returnType;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** One statement method checking a parameter is of certain __
 * @author Ori Marcovitch */
public class TypeChecker extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x2A4AF30BA4892265L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    final ReturnStatement $ = az.returnStatement(onlyStatement(¢));
    return hazOneParameter(¢) //
        && iz.instanceofExpression(expression($)) //
        && "boolean".equals(returnType(¢) + "") //
        && identifier(name(onlyParameter(¢))).equals(left(az.instanceofExpression(expression($))) + "");
  }
}

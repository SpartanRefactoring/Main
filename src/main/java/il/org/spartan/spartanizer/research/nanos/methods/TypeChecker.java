package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class TypeChecker extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    final ReturnStatement $ = az.returnStatement(onlyStatement(¢));
    return hazOneParameter(¢) //
        && iz.instanceofExpression(expression($)) //
        && "boolean".equals(returnType(¢) + "") //
        && identifier(name(onlyParameter(¢))).equals(left(az.instanceofExpression(expression($))) + "");
  }
}

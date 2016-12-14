package il.org.spartan.spartanizer.research.patterns.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class TypeChecker extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    if (!hazOneStatement(¢) || !iz.returnStatement(onlyStatement(¢)) || !hazOneParameter(¢))
      return false;
    final ReturnStatement $ = az.returnStatement(onlyStatement(¢));
    return iz.instanceofExpression(expression($)) && "boolean".equals(returnType(¢) + "")
        && identifier(name(onlyParameter(¢))).equals(left(az.instanceofExpression(expression($))) + "");
  }
}

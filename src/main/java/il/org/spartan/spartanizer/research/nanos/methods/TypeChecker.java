package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** One statement method checking a parameter is of certain type
 * @author Ori Marcovitch */
public class TypeChecker extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 3047515329200202341L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    @Nullable final ReturnStatement $ = az.returnStatement(onlyStatement(¢));
    return hazOneParameter(¢) //
        && iz.instanceofExpression(expression($)) //
        && "boolean".equals(returnType(¢) + "") //
        && identifier(name(onlyParameter(¢))).equals(left(az.instanceofExpression(expression($))) + "");
  }
}

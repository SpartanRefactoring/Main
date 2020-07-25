package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Up caster
 * @author Ori Marcovitch */
public class Up {
  public static class Caster extends JavadocMarkerNanoPattern {
    private static final long serialVersionUID = -0x17831792D2C8B593L;

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      final Expression $ = expression(az.returnStatement(onlyStatement(¢)));
      return hazOneParameter(¢)//
          && hazOneStatement(¢)//
          && notConstructor(¢)//
          && returnTypeNotVoid(¢)//
          && !returnTypeSameAsParameter(¢)//
          && (iz.name($) || iz.name(expression($)))//
      ;
    }
  }
}

package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class Up {
  public static class Caster extends JavadocMarkerNanoPattern {
    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      return hazOneParameter(¢)//
          && hazOneStatement(¢)//
          && notConstructor(¢)//
          && returnTypeNotVoid(¢)//
          && !returnTypeSameAsParameter(¢)//
          && iz.name(expression(az.returnStatement(onlyStatement(¢))))//
      ;
    }
  }
}

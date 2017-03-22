package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;

/** Up caster
 * @author Ori Marcovitch */
public class Up {
  public static class Caster extends JavadocMarkerNanoPattern {
    private static final long serialVersionUID = -1694223804190471571L;

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      @NotNull final Expression $ = expression(az.returnStatement(onlyStatement(¢)));
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

package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Nano to match setter method which returns this
 * @author Ori Marcovitch */
public class Cascading {
  public static class CascadingSetter extends JavadocMarkerNanoPattern {
    private static final long serialVersionUID = -2893413461939243057L;
    private static final CascadingSetter setter = new CascadingSetter();

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      return hazAtLeastTwoStatements(¢)//
          && cascading(¢)//
          && setter(¢);
    }

    private boolean cascading(final MethodDeclaration ¢) {
      return lastReturnsThis(¢);
    }

    private static boolean setter(final MethodDeclaration ¢) {
      final MethodDeclaration $ = copy.of(¢);
      statements($).remove(statements($).size() - 1);
      return setter.canTip($);
    }
  }
}

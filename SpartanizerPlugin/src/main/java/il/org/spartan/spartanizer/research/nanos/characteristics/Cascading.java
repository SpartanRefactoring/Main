package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since 2016 
 */

public class Cascading {
  public static class Setter extends JavadocMarkerNanoPattern {
    private static final Setter setter = new Setter();

    @Override protected boolean prerequisites(final MethodDeclaration ¢) {
      return hazAtLeastTwoStatements(¢) && fluent(¢) && setter(¢);
    }

    private boolean fluent(final MethodDeclaration ¢) {
      return lastReturnsThis(¢);
    }

    private static boolean setter(final MethodDeclaration ¢) {
      final MethodDeclaration $ = copy.of(¢);
      statements($).remove(statements($).size() - 1);
      return setter.canTip($);
    }
  }
}


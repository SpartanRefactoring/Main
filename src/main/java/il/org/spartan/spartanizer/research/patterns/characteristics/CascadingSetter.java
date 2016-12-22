package il.org.spartan.spartanizer.research.patterns.characteristics;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import il.org.spartan.spartanizer.research.patterns.methods.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class CascadingSetter extends JavadocMarkerNanoPattern {
  private static final Setter setter = new Setter();

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return fluent(¢) && setter(¢);
  }

  private boolean fluent(final MethodDeclaration ¢) {
    return lastReturnsThis(¢);
  }

  private static boolean setter(final MethodDeclaration ¢) {
    MethodDeclaration $ = duplicate.of(¢);
    statements($).remove(statements($).size() - 1);
    return setter.canTip($);
  }
}

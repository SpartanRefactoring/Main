package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** Removes overriding methods that only call their counterpart in the parent
 * class, e.g., {@code @Override void foo(){super.foo();}}
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06 */
public final class MethodDeclarationOverrideDegenerateRemove extends RemovingTipper<MethodDeclaration>//
    implements TipperCategory.SyntacticBaggage {
  private static boolean shouldRemove(@NotNull final MethodDeclaration $, @NotNull final SuperMethodInvocation i) {
    for (final Object m : $.modifiers())
      if (m instanceof MarkerAnnotation && (((Annotation) m).getTypeName() + "").contains("Deprecated"))
        return false;
    return (i.getName() + "").equals($.getName() + "") && arguments(i).size() == parameters($).size();
  }

  @Override @NotNull public String description(@NotNull final MethodDeclaration ¢) {
    return "Remove vacous '" + ¢.getName() + "' overriding method";
  }

  @Override protected boolean prerequisite(@NotNull final MethodDeclaration ¢) {
    final ExpressionStatement $ = extract.expressionStatement(¢);
    return $ != null && $.getExpression() instanceof SuperMethodInvocation && shouldRemove(¢, (SuperMethodInvocation) $.getExpression());
  }
}

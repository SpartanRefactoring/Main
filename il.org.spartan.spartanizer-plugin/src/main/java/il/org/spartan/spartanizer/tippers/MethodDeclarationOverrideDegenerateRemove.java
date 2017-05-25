package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removes overriding methods that only call their counterpart in the parent
 * class, e.g., {@code @Override void foo(){super.foo();}}
 * <p>
 * Do not activate this class without checking with me (yg) or Ori Marcovitch.
 * It interferes with nano detection.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06 */
public final class MethodDeclarationOverrideDegenerateRemove extends RemovingTipper<MethodDeclaration>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -0x15F499B4EDEF2039L;

  private static boolean shouldRemove(final MethodDeclaration $, final SuperMethodInvocation i) {
    for (final Object m : $.modifiers())
      if (m instanceof MarkerAnnotation && (((Annotation) m).getTypeName() + "").contains("Deprecated"))
        return false;
    return (i.getName() + "").equals($.getName() + "") && arguments(i).size() == parameters($).size();
  }
  @Override public String description(final MethodDeclaration ¢) {
    return "Remove vacous '" + ¢.getName() + "' overriding method";
  }
  @Override protected boolean prerequisite(final MethodDeclaration ¢) {
    final ExpressionStatement $ = extract.expressionStatement(¢);
    return $ != null && $.getExpression() instanceof SuperMethodInvocation && shouldRemove(¢, (SuperMethodInvocation) $.getExpression());
  }
}

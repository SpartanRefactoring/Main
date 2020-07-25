package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.tipping.RemovingTipper;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Removes overriding methods that only call their counterpart in the parent
 * class, e.g., {@code @Override void foo(){super.foo();}}
 * <p>
 * Do not activate this class without checking with me (yg) or Ori Marcovitch.
 * It interferes with nano detection.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06 */
public final class MethodDeclarationOverrideDegenerateRemove extends RemovingTipper<MethodDeclaration>//
    implements Category.SyntacticBaggage {
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

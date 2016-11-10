package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removes overriding methods that only call their counterpart in the parent
 * class, e.g., <code>@Override void foo(){super.foo();}</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06 */
public final class MethodDeclarationOverrideDegenerateRemove extends EagerTipper<MethodDeclaration> implements TipperCategory.Collapse {
  private static boolean shouldRemove(final MethodDeclaration d, final SuperMethodInvocation i) {
    for (final Object m : d.modifiers())
      if (m instanceof MarkerAnnotation && (((MarkerAnnotation) m).getTypeName() + "").contains("Deprecated"))
        return false;
    return (i.getName() + "").equals(d.getName() + "") && arguments(i).size() == parameters(d).size();
  }
  @Override public String description(final MethodDeclaration ¢) {
    return "Remove vacous '" + ¢.getName() + "' overriding method";
  }
  @Override public Tip tip(final MethodDeclaration d) {
    final ExpressionStatement s = extract.expressionStatement(d);
    return s == null || !(s.getExpression() instanceof SuperMethodInvocation) || !shouldRemove(d, (SuperMethodInvocation) s.getExpression()) ? null
        : new Tip(description(d), d, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(d, g);
          }
        };
  }
}

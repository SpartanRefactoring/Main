package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Removes overriding methods that only call their counterpart in the parent
 * class, e.g., <code>@Override void foo(){super.foo();}</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06 */
public final class MethodDeclarationDegenerateOverrideRemove extends Wring<MethodDeclaration> implements Kind.Canonicalization {
  private static boolean shouldRemove(final MethodDeclaration d, final SuperMethodInvocation i) {
    for (final Object m : d.modifiers())
      if (m instanceof MarkerAnnotation && (((MarkerAnnotation) m).getTypeName() + "").contains("Deprecated"))
        return false;
    return (i.getName() + "").equals(d.getName() + "") && arguments(i).size() == parameters(d).size();
  }

  @Override String description(final MethodDeclaration d) {
    return "Remove vacous '" + d.getName() + "' overriding method";
  }

  @Override Rewrite make(final MethodDeclaration d) {
    final ExpressionStatement s = extract.expressionStatement(d);
    return s == null || !(s.getExpression() instanceof SuperMethodInvocation) || !shouldRemove(d, (SuperMethodInvocation) s.getExpression()) ? null
        : new Rewrite(description(d), d) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(d, g);
          }
        };
  }
}

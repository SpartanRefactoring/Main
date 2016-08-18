package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to remove overriding methods that only call their
 * counterpart in the parent class, for example: <code>
 *
 * <pre>
 * &#64;Override void foo() {
 *   super.foo();
 * }
 * </pre>
 *
 * </code> will be completely removed.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06 */
public class MethodRemoveDegenerateOverride extends Wring<MethodDeclaration> implements Kind.Canonicalization {
  @Override Rewrite make(final MethodDeclaration d) {
    final ExpressionStatement s = extract.expressionStatement(d);
    return s == null || !(s.getExpression() instanceof SuperMethodInvocation) || !shouldRemove(d, (SuperMethodInvocation) s.getExpression()) ? null
        : new Rewrite(description(d), d) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(d, g);
          }
        };
  }
  private static boolean shouldRemove(final MethodDeclaration d, final SuperMethodInvocation i) {
    for (final Object m : d.modifiers())
      if (m instanceof MarkerAnnotation && ((MarkerAnnotation) m).getTypeName().toString().contains("Deprecated"))
        return false;
    return i.getName().toString().equals(d.getName().toString()) && i.arguments().size() == expose.parameters(d).size();
  }
  @Override String description(final MethodDeclaration d) {
    return "Remove useless '" + d.getName() + "' overriding method";
  }
}

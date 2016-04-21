package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} to remove overriding methods that only call their counterpart
 * in the parent class, for example:
 * <code><pre>@Override void foo() { super.foo(); }</pre></code> will be
 * completely removed.
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-06
 */
public class MethodRemoveDegenerateOverride extends Wring<MethodDeclaration> {
  @Override Rewrite make(final MethodDeclaration d) {
    if (d.getBody().statements().size() != 1 || !((Statement) d.getBody().statements().get(0) instanceof ExpressionStatement))
      return null;
    final ExpressionStatement s = (ExpressionStatement) d.getBody().statements().get(0);
    return !(s.getExpression() instanceof SuperMethodInvocation) || !shouldRemove(d, ((SuperMethodInvocation) s.getExpression())) ? null
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
    return i.getName().toString().equals(d.getName().toString()) && i.arguments().size() == d.parameters().size();
  }
  @Override String description(final MethodDeclaration d) {
    return "Remove the useless " + d.getName() + " overriding method";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}

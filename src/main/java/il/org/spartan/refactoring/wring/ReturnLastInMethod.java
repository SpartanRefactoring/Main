package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.suggestions.*;

/**
 * A {@link Wring} that removes empty return statements, provided that they are
 * last in method.
 *
 * @author Yossi Gil
 * @since 2015-7-17
 */
public final class ReturnLastInMethod extends Wring<ReturnStatement> implements Kind.Simplify {
  @Override String description(final ReturnStatement s) {
    return "Remove redundant return statement: " + s;
  }
  @Override Suggestion make(final ReturnStatement s) {
    if (s.getExpression() != null)
      return null;
    final Block b = asBlock(s.getParent());
    return b == null || !lastIn(s, b.statements()) || !(b.getParent() instanceof MethodDeclaration) ? null //
        : new Suggestion(description(s), s) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(s, g);
          }
        };
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}

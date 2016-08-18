package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} that removes empty return statements, provided that they are
 * last in method.
 * @author Yossi Gil
 * @since 2015-7-17 */
public class ReturnLastInMethod extends Wring<ReturnStatement> implements Kind.SyntacticBaggage {
  @Override String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "Remove redundant return statement";
  }
  @Override Rewrite make(final ReturnStatement s) {
    if (s.getExpression() != null)
      return null;
    final Block b = asBlock(s.getParent());
    return b == null || !lastIn(s, b.statements()) || !(b.getParent() instanceof MethodDeclaration) ? null //
        : new Rewrite(description(s), s) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            r.remove(s, g);
          }
        };
  }
}

package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** convert <code>int a = 3;return a;</code> into <code>return a;</code>
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationInitializerReturnExpression extends VariableDeclarationFragementAndStatement implements Kind.Inlining {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate temporary " + ¢.getName() + " and inline its value into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f))
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null)
      return null;
    final Expression newReturnValue = s.getExpression();
    if (newReturnValue == null)
      return null;
    final InlinerWithValue i = new Inliner(n, r, g).byValue(initializer);
    if (wizard.same(n, newReturnValue) || !i.canSafelyInlineinto(newReturnValue)
        || i.replacedSize(newReturnValue) - eliminationSaving(f) - metrics.size(newReturnValue) > 0)
      return null;
    r.replace(s.getExpression(), newReturnValue, g);
    i.inlineInto(newReturnValue);
    eliminate(f, r, g);
    return r;
  }
}

package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** convert {@code int a = 3;return a;} into {@code return a;}
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnExpression extends $FragementAndStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 1067290925840665930L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate local " + ¢.getName() + " and inline its value into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (forbidden(f, initializer) || usedInSubsequentInitializers(f, n))
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null)
      return null;
    final Expression newReturnValue = s.getExpression();
    if (newReturnValue == null)
      return null;
    final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (wizard.same(n, newReturnValue) || !i.canSafelyInlineinto(newReturnValue)
        || i.replacedSize(newReturnValue) - eliminationSaving(f) - metrics.size(newReturnValue) > 0)
      return null;
    $.replace(s.getExpression(), newReturnValue, g);
    i.inlineInto(newReturnValue);
    eliminate(f, $, g);
    return $;
  }
}

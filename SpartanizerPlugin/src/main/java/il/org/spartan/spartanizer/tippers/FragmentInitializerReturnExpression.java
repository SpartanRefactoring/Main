package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** convert {@code int a = 3;return a;} into {@code return a;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnExpression extends $FragementInitializerStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 1067290925840665930L;

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Eliminate local " + ¢.getName() + " and inline its value into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    if (forbidden(fragment(), initializer()) || usedInSubsequentInitializers())
      return null;
    @Nullable final ReturnStatement s = az.returnStatement(nextStatement());
    if (s == null)
      return null;
    final Expression newReturnValue = s.getExpression();
    if (newReturnValue == null)
      return null;
    @NotNull final InlinerWithValue i = new Inliner(name(), $, g).byValue(initializer());
    if (wizard.same(name(), newReturnValue) || !i.canSafelyInlineinto(newReturnValue)
        || i.replacedSize(newReturnValue) - eliminationSaving() - metrics.size(newReturnValue) > 0)
      return null;
    $.replace(s.getExpression(), newReturnValue, g);
    i.inlineInto(newReturnValue);
    eliminateFragment($, g);
    return $;
  }
}

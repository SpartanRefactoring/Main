package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;

/** Converts {@code int a=3;return a;} into {@code return 3;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnAssignment extends $FragmentAndStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 1334179326644184831L;

  @Override @NotNull public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Eliminate local '" + ¢.getName() + "', inlining its value into the subsequent return statement";
  }

  @Override @Nullable protected ASTRewrite go(@NotNull final ASTRewrite $, @NotNull final VariableDeclarationFragment f, @NotNull final SimpleName n,
      @Nullable final Expression initializer, final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f))
      return null;
    @Nullable final Assignment a = az.assignment(expression(az.returnStatement(nextStatement)));
    if (a == null || !wizard.same(n, to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newReturnValue = copy.of(from(a));
    @NotNull final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving(f) - metrics.size(newReturnValue) > 0)
      return null;
    $.replace(a, newReturnValue, g);
    i.inlineInto(newReturnValue);
    wizard.eliminate(f, $, g);
    return $;
  }
}

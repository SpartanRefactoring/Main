package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentNoInitializerAssignment extends $FragmentAndStatement//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 0xCE4CF4E3910F992L;

  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = copy.of(f);
    $.setInitializer(copy.of(x));
    return $;
  }

  @Override @NotNull public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override @Nullable protected ASTRewrite go(@NotNull final ASTRewrite $, final VariableDeclarationFragment f, @NotNull final SimpleName n,
      @Nullable final Expression initializer, final Statement nextStatement, final TextEditGroup g) {
    if (initializer != null)
      return null;
    @Nullable final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(n, to(a)) || doesUseForbiddenSiblings(f, from(a)))
      return null;
    $.replace(f, makeVariableDeclarationFragement(f, from(a)), g);
    $.remove(extract.containingStatement(a), g);
    return $;
  }
}

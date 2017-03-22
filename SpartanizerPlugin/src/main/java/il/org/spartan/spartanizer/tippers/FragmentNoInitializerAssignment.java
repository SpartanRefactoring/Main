package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentNoInitializerAssignment extends $FragementInitializerStatement//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 929095358016977298L;

  private static VariableDeclarationFragment makeVariableDeclarationFragement(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = copy.of(f);
    $.setInitializer(copy.of(x));
    return $;
  }

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    if (initializer() != null)
      return null;
    @Nullable final Assignment a = extract.assignment(nextStatement());
    if (a == null || !wizard.same(name(), to(a)) || doesUseForbiddenSiblings(fragment(), from(a)))
      return null;
    $.replace(fragment(), makeVariableDeclarationFragement(fragment(), from(a)), g);
    $.remove(extract.containingStatement(a), g);
    return $;
  }
}

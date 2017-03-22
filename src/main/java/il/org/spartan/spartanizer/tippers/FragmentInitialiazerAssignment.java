package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.engine.nominal.*;
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
public final class FragmentInitialiazerAssignment extends $FragementInitializerStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 1477509470490701826L;

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + trivia.gist(¢.getName()) + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    if (initializer() == null)
      return null;
    @Nullable final Assignment a = extract.assignment(nextStatement());
    if (a == null || !wizard.same(name(), to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newInitializer = copy.of(from(a));
    if (doesUseForbiddenSiblings(fragment(), newInitializer))
      return null;
    @NotNull final InlinerWithValue i = new Inliner(name(), $, g).byValue(initializer());
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement(), initializer()) > 0)
      return null;
    $.replace(initializer(), newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement(), g);
    return $;
  }
}

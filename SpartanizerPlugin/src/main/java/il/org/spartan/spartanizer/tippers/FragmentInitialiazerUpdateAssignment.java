package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitialiazerUpdateAssignment extends $FragementAndStatement//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -6925930851197136485L;

  @Override @NotNull public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override @Nullable protected ASTRewrite go(@NotNull final ASTRewrite $, final VariableDeclarationFragment f, @NotNull final SimpleName n,
      @Nullable final Expression initializer, final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null)
      return null;
    @Nullable final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.same(n, to(a)) || doesUseForbiddenSiblings(f, from(a)))
      return null;
    final Operator o = a.getOperator();
    if (o == ASSIGN)
      return null;
    final InfixExpression newInitializer = subject.pair(to(a), from(a)).to(wizard.assign2infix(o));
    @NotNull final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement, g);
    return $;
  }
}

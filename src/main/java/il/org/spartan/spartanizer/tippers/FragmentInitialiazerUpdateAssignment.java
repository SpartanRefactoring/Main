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
public final class FragmentInitialiazerUpdateAssignment extends FragementInitializerStatement//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -6925930851197136485L;

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate declaration of " + ¢.getName() + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    @Nullable final Assignment assignment = extract.assignment(nextStatement());
    if (assignment == null || !wizard.same(name(), to(assignment)) || doesUseForbiddenSiblings(object(), from(assignment)))
      return null;
    final Operator o = assignment.getOperator();
    if (o == ASSIGN)
      return null;
    final InfixExpression newInitializer = subject.pair(to(assignment), from(assignment)).to(wizard.assign2infix(o));
    @NotNull final InlinerWithValue i = new Inliner(name(), $, g).byValue(initializer());
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement(), initializer()) > 0)
      return null;
    $.replace(initializer(), newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement(), g);
    return $;
  }
}

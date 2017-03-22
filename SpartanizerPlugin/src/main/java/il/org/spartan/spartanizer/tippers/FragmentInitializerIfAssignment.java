package il.org.spartan.spartanizer.tippers;

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

/** convert {@code
 * int a = 2;
 * if (b)
 *   a = 3;
 * } into {@code
 * int a = b ? 3 : 2;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerIfAssignment extends FragementInitializerStatement implements TipperCategory.Inlining {
  private static final long serialVersionUID = 748535255358071695L;

  @NotNull @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Consolidate initialization of " + ¢.getName() + " with the subsequent conditional assignment to it";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    @Nullable final IfStatement s = az.ifStatement(nextStatement());
    if (s == null || !iz.vacuousElse(s))
      return null;
    s.setElseStatement(null);
    final Expression condition = s.getExpression();
    if (condition == null)
      return null;
    @Nullable final Assignment a = extract.assignment(then(s));
    if (a == null || !wizard.same(to(a), name()) || a.getOperator() != Assignment.Operator.ASSIGN
        || doesUseForbiddenSiblings(object(), condition, from(a)))
      return null;
    @NotNull final InlinerWithValue i = new Inliner(name(), $, g).byValue(initializer());
    if (!i.canInlineinto(condition, from(a)))
      return null;
    final ConditionalExpression newInitializer = subject.pair(from(a), initializer()).toCondition(condition);
    if (i.replacedSize(newInitializer) > metrics.size(nextStatement(), initializer()))
      return null;
    $.replace(initializer(), newInitializer, g);
    i.inlineInto(then(newInitializer), newInitializer.getExpression());
    $.remove(nextStatement(), g);
    return $;
  }
}

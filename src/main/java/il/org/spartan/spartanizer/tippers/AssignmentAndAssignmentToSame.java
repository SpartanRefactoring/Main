package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** See {@link #examples()} for documentation
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-04 */
public final class AssignmentAndAssignmentToSame extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 1L;

  @NotNull @Override public Example[] examples() {
    return new Example[] { //
        convert("s=s.f();s=s.f();").to("s=s.f().f()"), //
    };
  }

  @NotNull @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " into subsequent assignment";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, @NotNull final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() != ASSIGN || !iz.statement(parent(a1)))
      return null;
    @Nullable final Assignment a2 = extract.assignment(nextStatement);
    if (operator(a2) != ASSIGN)
      return null;
    final SimpleName to = az.simpleName(to(a1));
    if (!wizard.same(to, to(a2)) || !sideEffects.free(to))
      return null;
    @Nullable final Expression from1 = from(a1), from2 = from(a2);
    final List<SimpleName> uses = collect.usesOf(to).in(from2);
    return uses.size() > 1 ? !sideEffects.free(from1) || !iz.deterministic(from1) ? null : go($, a1, g, to, from1, from2)
        : sideEffects.free(from1) && iz.deterministic(from1) && uses.size() == 1 ? go($, a1, g, to, from1, from2) : null;
  }

  @NotNull private static ASTRewrite go(@NotNull final ASTRewrite $, final Assignment a1, final TextEditGroup g, final SimpleName to,
      final Expression from1, final Expression from2) {
    $.remove(a1, g);
    final Expression newFrom = copy.of(from2);
    new Inliner(to, $, g).byValue(from1).inlineInto(newFrom);
    $.replace(from2, newFrom, g);
    return $;
  }
}

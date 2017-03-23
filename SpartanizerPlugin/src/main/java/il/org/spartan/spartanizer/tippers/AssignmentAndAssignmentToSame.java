package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;
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
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** See {@link #examples()} for documentation
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-04 */
public final class AssignmentAndAssignmentToSame extends GoToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 1;

  @Override @NotNull public Example[] examples() {
    return new Example[] { //
        convert("s=s.f();s=s.g();").to("s=s.f().g()"), //
    };
  }

  @Override @NotNull public String description(final Assignment ¢) {
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
    switch (collect.usesOf(to).in(from2).size()) {
      case 0:
        return null;
      case 1:
        return go($, a1, g, to, from1, from2);
      default:
        if (iz.deterministic(from1) && sideEffects.free(from1))
          return go($, a1, g, to, from1, from2);
        return null;
    }
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

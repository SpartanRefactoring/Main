package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code
 * a = 3;
 * return a;
 * } to {@code
 * return a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-28 */
public final class AssignmentAndReturn extends ReplaceToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -1263526923784459386L;

  @NotNull @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " into its subsequent 'return'";
  }

  @Override public ASTRewrite go(@NotNull final ASTRewrite $, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    @Nullable final Statement parent = az.statement(parent(a));
    if (parent == null || iz.forStatement(parent))
      return null;
    @Nullable final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.same(to(a), core(expression(s))))
      return null;
    $.remove(parent, g);
    $.replace(s, subject.operand(a).toReturn(), g);
    return $;
  }
}

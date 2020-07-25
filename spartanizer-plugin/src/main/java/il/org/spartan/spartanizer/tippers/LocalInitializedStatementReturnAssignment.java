package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.Inliner.InlinerWithValue;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Converts {@code int a=3;return a;} into {@code return 3;}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedStatementReturnAssignment extends $FragmentAndStatement//
    implements Category.Inlining {
  private static final long serialVersionUID = 0x1283F5075F4BE6FFL;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate local '" + ¢.getName() + "', inlining its value into the subsequent return statement";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f))
      return null;
    final Assignment a = az.assignment(expression(az.returnStatement(nextStatement)));
    if (a == null || !wizard.eq(n, to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newReturnValue = copy.of(from(a));
    final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving(f) - Metrics.size(newReturnValue) > 0)
      return null;
    $.replace(a, newReturnValue, g);
    i.inlineInto(newReturnValue);
    remove.deadFragment(f, $, g);
    return $;
  }
}

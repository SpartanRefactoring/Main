package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.nodes.metrics.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** convert {@code
 * int a = 3;
 * return a;
 * } into {@code
 * return 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedReturn extends $FragmentAndStatement//
    implements Category.Shortcircuit {
  private static final long serialVersionUID = 0x5D2F5CEC2756BC9DL;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate temporary '" + ¢.getName() + "' by inlining it into the expression of the subsequent return statement";
  }
  @Override public Examples examples() {
    return convert("int a = 3; return a += 2;").to("return 3 + 2;");
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f))
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null)
      return null;
    final Assignment a = az.assignment(expression(s));
    if (a == null || !wizard.eq(n, to(a)) || a.getOperator() == ASSIGN)
      return null;
    final Expression newReturnValue = make.assignmentAsExpression(a);
    final InlinerWithValue i = new Inliner(n, $, g).byValue(initializer);
    if (!i.canInlineinto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving(f) - Metrics.size(newReturnValue) > 0)
      return null;
    $.replace(a, newReturnValue, g);
    i.inlineInto(newReturnValue);
    remove.deadFragment(f, $, g);
    return $;
  }
}

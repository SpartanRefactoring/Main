package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.patterns.*;

/** Converts {@code int a=3;return a;} into {@code return 3;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturnAssignment extends LocalVariableInitializedStatement//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 0x1283F5075F4BE6FFL;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate local '" + ¢.getName() + "', inlining its value into the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final boolean doesUseForbiddenSiblings = doesUseForbiddenSiblings(az.returnStatement(nextStatement));
    if (doesUseForbiddenSiblings)
      return null;
    final Assignment a = az.assignment(nextStatement);
    if (a == null || !wizard.same(name, to(a)) || a.getOperator() != ASSIGN)
      return null;
    final Expression newReturnValue = copy.of(from(a));
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (!i.canInlineinto(newReturnValue) || i.replacedSize(newReturnValue) - wizard.eliminationSaving(fragment) - metrics.size(newReturnValue) > 0)
      return null;
    $.replace(a, newReturnValue, g);
    i.inlineInto(newReturnValue);
    action.removeDeadFragment(fragment, $, g);
    return $;
  }
}

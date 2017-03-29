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
import il.org.spartan.utils.*;

/** convert {@code
 * int a = 3;
 * return a;
 * } into {@code
 * return 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerReturn extends LocalVariableInitializedStatement implements TipperCategory.Shortcircuit {
  private static final long serialVersionUID = -942696098095011383L;
  private ReturnStatement returnStatement;

  public FragmentInitializerReturn() {
    andAlso(Proposition.of("Next statement must be return ", //
        () -> (returnStatement = az.returnStatement(nextStatement)) != null));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate temporary '" + ¢.getName() + "' by inlining it into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Assignment a = az.assignment(expression(returnStatement));
    if (a == null || !wizard.same(name, to(a)) || a.getOperator() == ASSIGN)
      return null;
    final Expression newReturnValue = make.assignmentAsExpression(a);
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (!i.canInlineinto(newReturnValue) || i.replacedSize(newReturnValue) - eliminationSaving() - metrics.size(newReturnValue) > 0)
      return null;
    $.replace(a, newReturnValue, g);
    i.inlineInto(newReturnValue);
    action.removeDeadFragment(object(), $, g);
    return $;
  }
}

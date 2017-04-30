package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * a = 3;
 * return a;
 * } to {@code
 * return a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class PrefixIncrementDecrementReturn extends GoToNextStatement<PrefixExpression>//
    implements TipperCategory.Collapse {
  private static final long serialVersionUID = -0x6380D3DFEF275DC1L;

  @Override public String description(final PrefixExpression ¢) {
    return "Consolidate " + ¢ + " with subsequent 'return' of " + operand(¢);
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final PrefixExpression x, final Statement nextStatement, final TextEditGroup g) {
    if (!in(x.getOperator(), INCREMENT, DECREMENT))
      return null;
    final Statement parent = az.statement(x.getParent());
    if (parent == null || parent instanceof ForStatement)
      return null;
    final ReturnStatement s = az.returnStatement(nextStatement);
    if (s == null || !wizard.eq(operand(x), expression(s)))
      return null;
    $.remove(parent, g);
    $.replace(s, subject.operand(x).toReturn(), g);
    return $;
  }
}

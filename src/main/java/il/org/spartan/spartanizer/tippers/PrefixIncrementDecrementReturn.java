package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code
 * a = 3;
 * return a;
 * } to {@code
 * return a = 3;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-28 */
public final class PrefixIncrementDecrementReturn extends ReplaceToNextStatement<PrefixExpression>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -7169963565517331905L;

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
    if (s == null || !wizard.same(operand(x), expression(s)))
      return null;
    $.remove(parent, g);
    $.replace(s, subject.operand(x).toReturn(), g);
    return $;
  }
}

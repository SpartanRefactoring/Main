package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.operand;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.DECREMENT;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.INCREMENT;

import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * a = 3;
 * return a;
 * } to {@code
 * return a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-28 */
public final class PrefixIncrementDecrementReturn extends GoToNextStatement<PrefixExpression>//
    implements Category.Collapse {
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

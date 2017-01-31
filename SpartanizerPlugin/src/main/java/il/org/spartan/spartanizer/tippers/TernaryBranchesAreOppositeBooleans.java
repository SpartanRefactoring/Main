package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <code>
 * ¢ ? Boolean.TRUE : Boolean.FALSE;
 * </code>
 *
 * into
 *
 * <code>
 * ¢
 * </code>
 *
 * or
 *
 * <code>
 * ¢ ? Boolean.FALSE : Boolean.TRUE;
 * </code>
 *
 * into
 *
 * <code>
 * !¢
 * </code>
 *
 * @author Dan Abramovich
 * @since 27-11-2016 */
public class TernaryBranchesAreOppositeBooleans extends ReplaceCurrentNode<ConditionalExpression>//
    implements TipperCategory.Unite {
  @Override public ASTNode replacement(final ConditionalExpression ¢) {
    final Expression $ = ¢.getElseExpression(), then = ¢.getThenExpression();
    return wizard.same($, truee) && wizard.same(then, falsee) ? make.notOf(copy.of(¢.getExpression()))
        : wizard.same($, falsee) && wizard.same(then, truee) ? copy.of(¢.getExpression()) : null;
  }

  public static final ASTNode truee = wizard.ast("Boolean.TRUE"), falsee = wizard.ast("Boolean.FALSE");

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression ¢) {
    return "eliminate teranry that evaluates to either Boolean.FALSE or Boolean.TRUE (not just one of these)";
  }
}

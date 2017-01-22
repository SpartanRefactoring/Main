package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <pre>
 * ¢ ? Boolean.TRUE : Boolean.FALSE;
 * </pre>
 *
 * into
 *
 * <pre>
 * ¢
 * </pre>
 *
 * or
 *
 * <pre>
 * ¢ ? Boolean.FALSE : Boolean.TRUE;
 * </pre>
 *
 * into
 *
 * <pre>
 * !¢
 * </pre>
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

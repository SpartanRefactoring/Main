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
public class TernaryBranchesAreOppositeBooleans extends ReplaceCurrentNode<ConditionalExpression> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final ConditionalExpression ¢) {
    return wizard.same(¢.getElseExpression(), wizard.ast("Boolean.TRUE")) && wizard.same(¢.getThenExpression(), wizard.ast("Boolean.FALSE"))
        ? make.notOf(duplicate.of(¢.getExpression()))
        : wizard.same(¢.getElseExpression(), wizard.ast("Boolean.FALSE")) && wizard.same(¢.getThenExpression(), wizard.ast("Boolean.TRUE"))
            ? duplicate.of(¢.getExpression()) : null;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression ¢) {
    return "eliminate teranry that evaluates to either Boolean.FALSE or Boolean.TRUE (not just one of these)";
  }
}

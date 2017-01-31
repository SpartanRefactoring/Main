package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue1045} Issue #1045 Convert: {@code if (a > 1 || b >
 * 2 || c + e > 3) { return 1; } } to: {@code if (((a > 1) || (b > 2)) || ((c +
 * e) > 3)) { return 1; } } Currently the expander goes over expressions and for
 * each InfixExpression who's parent is also an InfixExpression, It will make it
 * parenthesized.
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-11 */
public class ParenthesesBloater extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final InfixExpression ¢) {
    if (iz.parenthesizedExpression(¢) || !iz.infixExpression(¢.getParent()))
      return null;
    final ParenthesizedExpression $ = ¢.getAST().newParenthesizedExpression();
    $.setExpression(copy.of(¢));
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Add parentheses to InfixExpression who's parent is also InfixExpression";
  }
}

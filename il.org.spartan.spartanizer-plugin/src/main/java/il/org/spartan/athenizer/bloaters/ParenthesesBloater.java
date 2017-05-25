package il.org.spartan.athenizer.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case is {@link Issue1045} Issue #1045 Convert: {@code if (a > 1 || b >
 * 2 || c + e > 3) { return 1; } } to: {@code if (((a > 1) || (b > 2)) || ((c +
 * e) > 3)) { return 1; } } Currently the expander goes over expressions and for
 * each InfixExpression who's parent is also an InfixExpression, It will make it
 * parenthesized.
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-11 */
public class ParenthesesBloater extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x3B51DC170DB9CF0DL;

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

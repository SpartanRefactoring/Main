package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Expend :
 *
 * <pre>
 *  a+ "";
 * </pre>
 *
 * To:
 *
 * <pre>
 * a.toString();
 * </pre>
 * 
 * Important : Works only in cases where binding exists, otherwise does nothing
 * Issue #965
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-20 */
public class toStringExpander extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Expander {
  @Override public ASTNode replacement(final InfixExpression ¢) {
    if (¢.getLeftOperand().resolveTypeBinding() == null || ¢.getRightOperand().resolveTypeBinding() == null || extract.allOperands(¢).size() != 2)
      return null;
    final MethodInvocation $ = ¢.getAST().newMethodInvocation();
    if ("\"\"".equals(¢.getRightOperand() + "") && !(¢.getLeftOperand()).resolveTypeBinding().isPrimitive())
      $.setExpression(duplicate.of(¢.getLeftOperand()));
    else {
      if (!"\"\"".equals(¢.getLeftOperand() + "") || (¢.getRightOperand()).resolveTypeBinding().isPrimitive())
        return null;
      $.setExpression(duplicate.of(¢.getRightOperand()));
    }
    $.setName(¢.getAST().newSimpleName("toString"));
    return $;
  }

  @Override @SuppressWarnings("unused") public String description(final InfixExpression __) {
    return null;
  }
}

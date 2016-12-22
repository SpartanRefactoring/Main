package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
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
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-20 */
public class toStringExpander extends ReplaceCurrentNode<InfixExpression> {
  @Override public ASTNode replacement(final InfixExpression ¢) {
    if (extract.allOperands(¢).size() != 2)
      return null;
    final MethodInvocation $ = ¢.getAST().newMethodInvocation();
    if ("\"\"".equals(¢.getRightOperand() + "") && !iz.literal(¢.getLeftOperand()))
      $.setExpression(duplicate.of(¢.getLeftOperand()));
    else {
      if (!"\"\"".equals(¢.getLeftOperand() + "") || iz.literal(¢.getRightOperand()))
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

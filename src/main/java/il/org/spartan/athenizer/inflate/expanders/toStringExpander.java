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
  @Override public ASTNode replacement(final InfixExpression n) {
    if (extract.allOperands(n).size() != 2)
      return null;
    final MethodInvocation ret = n.getAST().newMethodInvocation();
    if (n.getRightOperand().toString().equals("\"\"") && !iz.literal(n.getLeftOperand()))
      ret.setExpression(duplicate.of(n.getLeftOperand()));
    else if (n.getLeftOperand().toString().equals("\"\"") && !iz.literal(n.getRightOperand()))
      ret.setExpression(duplicate.of(n.getRightOperand()));
    else
      return null;
    final SimpleName methodName = n.getAST().newSimpleName("toString");
    ret.setName(methodName);
    return ret;
  }

  @SuppressWarnings("unused") @Override public String description(final InfixExpression n) {
    return null;
  }
}

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
  @Override public ASTNode replacement(InfixExpression x) {
    if (extract.allOperands(x).size() != 2)
      return null;
    MethodInvocation $ = x.getAST().newMethodInvocation();
    if ("\"\"".equals((x.getRightOperand() + "")) && !iz.literal(x.getLeftOperand()))
      $.setExpression(duplicate.of(x.getLeftOperand()));
    else {
      if (!"\"\"".equals((x.getLeftOperand() + "")) || iz.literal(x.getRightOperand()))
        return null;
      $.setExpression(duplicate.of(x.getRightOperand()));
    }
    SimpleName methodName = x.getAST().newSimpleName("toString");
    $.setName(methodName);
    return $;
  }

  @SuppressWarnings("unused") @Override public String description(InfixExpression __) {
    return null;
  }
}

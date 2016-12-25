package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import static il.org.spartan.spartanizer.ast.safety.iz.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * ++i;
 * --i;
 * <pre>
 * to
 *
 * <pre>
 * i++;
 * i--;
 * <pre>
 * 
 * Test case is {@link Issue1005}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */
public class PrefixToPostfix extends ReplaceCurrentNode<PrefixExpression> implements TipperCategory.Idiomatic {
  @Override public ASTNode replacement(final PrefixExpression ¢) {
    return subject.operand(step.operand(¢)).to(step.operator(¢) == PrefixExpression.Operator.DECREMENT ? PostfixExpression.Operator.DECREMENT : PostfixExpression.Operator.INCREMENT);
  }
  
  @Override protected boolean prerequisite(PrefixExpression e) {
    ASTNode parent = step.parent(e);
    return expressionStatement(parent) || forStatement(parent);
  }

  @Override @SuppressWarnings("unused") public String description(final PrefixExpression __) {
    return null;
  }
}

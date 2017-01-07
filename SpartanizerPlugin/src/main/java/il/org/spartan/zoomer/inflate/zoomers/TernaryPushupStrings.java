package il.org.spartan.zoomer.inflate.zoomers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Converts addition of string literal and ternary string literal to ternary string literal of the concatenation.
 * for example
 * <pre>
 * "abc" + (condition ? "123" : "456")
 * <pre>
 * to
 * <pre>
 * condition ? "abc123" : "abc456"
 * <pre>
 * 
 * Test case is {@link Issue1003}
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-08
 */
public class TernaryPushupStrings extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  @Override public ASTNode replacement(InfixExpression x) {
    AST ast = x.getAST();
    InfixExpression nn = duplicate.of(x);
    StringLiteral l = az.stringLiteral(step.left(nn));
    ConditionalExpression r = az.conditionalExpression(step.expression(step.right(nn)));
    ConditionalExpression $ = ast.newConditionalExpression();
    $.setExpression(duplicate.of(r.getExpression()));
    StringLiteral l1 = az.stringLiteral(step.then(r));
    StringLiteral l2 = az.stringLiteral(step.elze(r));
    l1.setLiteralValue(l.getLiteralValue() + l1.getLiteralValue());
    l2.setLiteralValue(l.getLiteralValue() + l2.getLiteralValue());
    $.setThenExpression(duplicate.of(l1));
    $.setElseExpression(duplicate.of(l2));
    return $;
  }
  
  @Override protected boolean prerequisite(InfixExpression x) {
    Expression l = step.left(x);
    if (!iz.parenthesizedExpression(step.right(x)))
      return false;
    Expression r = step.expression(step.right(x));
    if (!iz.stringLiteral(l) || step.operator(x) != InfixExpression.Operator.PLUS || !iz.conditionalExpression(r))
      return false;
    ConditionalExpression $ = az.conditionalExpression(r);
    return iz.stringLiteral(step.then($)) && iz.stringLiteral(step.elze($));
  }

  @Override public String description(@SuppressWarnings("unused") InfixExpression __) {
    return null;
  }
  
} 

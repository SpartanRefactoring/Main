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
 * (condition ? "abc" : "def") + 123"
 * <pre>
 * to
 * <pre>
 * condition ? "abc123" : "abc456"
 * condition ? "abc123" : "def123"
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
    StringLiteral l;
    ConditionalExpression r;
    if(iz.stringLiteral(step.left(nn))) {
      l = az.stringLiteral(step.left(nn));
      r = az.conditionalExpression(step.expression(step.right(nn)));
    }
    else {
      l = az.stringLiteral(step.right(nn));
      r = az.conditionalExpression(step.expression(step.left(nn)));
    }
    ConditionalExpression $ = ast.newConditionalExpression();
    $.setExpression(duplicate.of(r.getExpression()));
    StringLiteral l1 = az.stringLiteral(step.then(r));
    StringLiteral l2 = az.stringLiteral(step.elze(r));
    StringLiteral n1 = duplicate.of(l1);
    StringLiteral n2 = duplicate.of(l2);
    if(iz.stringLiteral(step.left(nn))) {
      n1.setLiteralValue(l.getLiteralValue() + l1.getLiteralValue());
      n2.setLiteralValue(l.getLiteralValue() + l2.getLiteralValue());
    }
    else {
      n1.setLiteralValue(l1.getLiteralValue() + l.getLiteralValue());
      n2.setLiteralValue(l2.getLiteralValue() + l.getLiteralValue());
    }
    $.setThenExpression(duplicate.of(n1));
    $.setElseExpression(duplicate.of(n2));
    return $;
  }
  
  @Override protected boolean prerequisite(InfixExpression ¢) {
    return step.operator(¢) == InfixExpression.Operator.PLUS && (iz.stringLiteral(step.left(¢)) && isStringTernaryWithParenth(step.right(¢))
        || iz.stringLiteral(step.right(¢)) && isStringTernaryWithParenth(step.left(¢)));
  }
  
  private static boolean isStringTernaryWithParenth(Expression x) {
    if (!iz.parenthesizedExpression(x))
      return false;
    Expression r = step.expression(x);
    if (!iz.conditionalExpression(r))
      return false;
    ConditionalExpression $ = az.conditionalExpression(r);
    return iz.stringLiteral(step.then($)) && iz.stringLiteral(step.elze($));
  }

  @Override public String description(@SuppressWarnings("unused") InfixExpression __) {
    return null;
  }
  
} 

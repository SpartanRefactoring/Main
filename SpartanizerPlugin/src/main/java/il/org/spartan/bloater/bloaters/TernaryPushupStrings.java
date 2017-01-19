package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Converts addition of string literal and ternary string literal to ternary
 * string literal of the concatenation. for example
 *
 * <pre>
 * "abc" + (condition ? "123" : "456") (condition ? "abc" : "def") + 123"
 *
 * <pre>
 * to
 *
 * <pre>
 * condition ? "abc123" : "abc456" condition ? "abc123" : "def123"
 *
 * <pre>
 * Test case is {@link Issue1003}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-08 */
public class TernaryPushupStrings extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  @Override public ASTNode replacement(final InfixExpression x) {
    final AST ast = x.getAST();
    final InfixExpression nn = copy.of(x);
    StringLiteral l;
    ConditionalExpression r;
    if (iz.stringLiteral(step.left(nn))) {
      l = az.stringLiteral(step.left(nn));
      r = az.conditionalExpression(step.expression(step.right(nn)));
    } else {
      l = az.stringLiteral(step.right(nn));
      r = az.conditionalExpression(step.expression(step.left(nn)));
    }
    final ConditionalExpression $ = ast.newConditionalExpression();
    $.setExpression(copy.of(r.getExpression()));
    final StringLiteral l1 = az.stringLiteral(step.then(r)), l2 = az.stringLiteral(step.elze(r)), n1 = copy.of(l1), n2 = copy.of(l2);
    if (iz.stringLiteral(step.left(nn))) {
      n1.setLiteralValue(l.getLiteralValue() + l1.getLiteralValue());
      n2.setLiteralValue(l.getLiteralValue() + l2.getLiteralValue());
    } else {
      n1.setLiteralValue(l1.getLiteralValue() + l.getLiteralValue());
      n2.setLiteralValue(l2.getLiteralValue() + l.getLiteralValue());
    }
    $.setThenExpression(copy.of(n1));
    $.setElseExpression(copy.of(n2));
    return $;
  }

  @Override protected boolean prerequisite(final InfixExpression ¢) {
    return step.operator(¢) == InfixExpression.Operator.PLUS && (iz.stringLiteral(step.left(¢)) && isStringTernaryWithParenth(step.right(¢))
        || iz.stringLiteral(step.right(¢)) && isStringTernaryWithParenth(step.left(¢)));
  }

  private static boolean isStringTernaryWithParenth(final Expression x) {
    if (!iz.parenthesizedExpression(x))
      return false;
    final Expression r = step.expression(x);
    if (!iz.conditionalExpression(r))
      return false;
    final ConditionalExpression $ = az.conditionalExpression(r);
    return iz.stringLiteral(step.then($)) && iz.stringLiteral(step.elze($));
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return null;
  }
}

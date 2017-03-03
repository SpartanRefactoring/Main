package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Converts addition of string literal and ternary string literal to ternary
 * string literal of the concatenation. for example {@code
 * "abc" + (condition ? "123" : "456") (condition ? "abc" : "def") + 123"
} to * {@code
 * condition ? "abc123" : "abc456" condition ? "abc123" : "def123"
 *
 * } Test case is {@link Issue1003}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-08 */
public class TernaryPushupStrings extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -5602484588967209664L;

  @Override public ASTNode replacement(final InfixExpression x) {
    final AST ast = x.getAST();
    final InfixExpression nn = copy.of(x);
    StringLiteral l;
    ConditionalExpression r;
    if (iz.stringLiteral(left(nn))) {
      l = az.stringLiteral(left(nn));
      r = az.conditionalExpression(expression(right(nn)));
    } else {
      l = az.stringLiteral(right(nn));
      r = az.conditionalExpression(expression(left(nn)));
    }
    final ConditionalExpression $ = ast.newConditionalExpression();
    $.setExpression(copy.of(r.getExpression()));
    final StringLiteral l1 = az.stringLiteral(then(r)), l2 = az.stringLiteral(elze(r)), n1 = copy.of(l1), n2 = copy.of(l2);
    if (iz.stringLiteral(left(nn))) {
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
    return operator(¢) == InfixExpression.Operator.PLUS
        && (iz.stringLiteral(left(¢)) && isStringTernaryWithParenth(right(¢)) || iz.stringLiteral(right(¢)) && isStringTernaryWithParenth(left(¢)));
  }

  private static boolean isStringTernaryWithParenth(final Expression x) {
    if (!iz.parenthesizedExpression(x))
      return false;
    final Expression r = expression(x);
    if (!iz.conditionalExpression(r))
      return false;
    final ConditionalExpression $ = az.conditionalExpression(r);
    return iz.stringLiteral(then($)) && iz.stringLiteral(elze($));
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return null;
  }
}

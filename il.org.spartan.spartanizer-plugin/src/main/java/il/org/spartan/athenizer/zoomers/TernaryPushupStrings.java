package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.zoom.zoomers.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Converts addition of string literal and ternary string literal to ternary
 * string literal of the concatenation. for example {@code
 * "abc" + (condition ? "123" : "456") (condition ? "abc" : "def") + 123"
} to * {@code
 * condition ? "abc123" : "abc456" condition ? "abc123" : "def123"
 *
 * } Test case is {@link Issue1003}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-08 */
public class TernaryPushupStrings extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -0x4DC0060CE8FDAAC0L;

  @Override public Examples examples() {
    return //
    convert("s = \"abc\" + (condition ? \"123\" : \"456\");") //
        .to("s = condition ? \"abc123\" : \"abc456\";") //
    ;
  }
  @Override public ASTNode replacement(final InfixExpression x) {
    final InfixExpression nn = copy.of(x);
    final StringLiteral l;
    final ConditionalExpression $;
    if (iz.stringLiteral(left(nn))) {
      l = az.stringLiteral(left(nn));
      $ = az.conditionalExpression(expression(right(nn)));
    } else {
      l = az.stringLiteral(right(nn));
      $ = az.conditionalExpression(expression(left(nn)));
    }
    final StringLiteral l1 = az.stringLiteral(then($)), l2 = az.stringLiteral(elze($)), n1 = copy.of(l1), n2 = copy.of(l2);
    if (iz.stringLiteral(left(nn))) {
      n1.setLiteralValue(l.getLiteralValue() + l1.getLiteralValue());
      n2.setLiteralValue(l.getLiteralValue() + l2.getLiteralValue());
    } else {
      n1.setLiteralValue(l1.getLiteralValue() + l.getLiteralValue());
      n2.setLiteralValue(l2.getLiteralValue() + l.getLiteralValue());
    }
    return subject.pair(copy.of(n1), copy.of(n2)).toCondition(copy.of($.getExpression()));
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

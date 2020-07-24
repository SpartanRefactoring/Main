package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue1005;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tippers.Prefix;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** convert {@code
 * ++i; --i;
} to * {@code
 * i+=1; i-=1;
 *
 * } Test case is {@link Issue1005}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @author Dor Ma'ayan
 * @since 2016-12-24 */
public class PrefixToInfix extends Prefix implements Category.Bloater {
  private static final long serialVersionUID = -0x5414F7B0C6AE739BL;

  public PrefixToInfix() {
    andAlso("Can be changed", () -> (iz.expressionStatement(current.getParent()) || iz.forStatement(current.getParent())));
  }
  @Override public Examples examples() {
    return convert("++i;").to("i += 1;").convert("--i;").to("i-=1;");
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final NumberLiteral one = current.getAST().newNumberLiteral();
    one.setToken("1");
    if (operator == PrefixExpression.Operator.INCREMENT)
      $.replace(current, subject.pair(operand, one).to(Assignment.Operator.PLUS_ASSIGN), g);
    if (operator == PrefixExpression.Operator.DECREMENT)
      $.replace(current, subject.pair(operand, one).to(Assignment.Operator.MINUS_ASSIGN), g);
    return $;
  }
  @Override public String description() {
    return "Convert PrefixExpression to Infix Expreesion";
  }
}

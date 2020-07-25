package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue0974;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tippers.Postfix;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Chage prfix expression to infix expression when possible toList Expand :
 * {@code
 * i++ / i-- ;
 * } To : {@code
 * i=i+1 / i=i-1 ;
 * } Test class is {@link Issue0974}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @author Raviv Rachmiel
 * @since 2017-01-09 */
public class PostFixToInfixExpander extends Postfix//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x2039A31D98B2AA4CL;

  public PostFixToInfixExpander() {
    andAlso("Can be changed", () -> (iz.expressionStatement(current.getParent()) || iz.forStatement(current.getParent())));
  }
  @Override public Examples examples() {
    return convert("i++;").to("i += 1;").convert("i--;").to("i-=1;");
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final NumberLiteral one = current.getAST().newNumberLiteral();
    one.setToken("1");
    if (operator == PostfixExpression.Operator.INCREMENT)
      $.replace(current, subject.pair(operand, one).to(Assignment.Operator.PLUS_ASSIGN), g);
    if (operator == PostfixExpression.Operator.DECREMENT)
      $.replace(current, subject.pair(operand, one).to(Assignment.Operator.MINUS_ASSIGN), g);
    return $;
  }
  @Override public String description() {
    return "replace postfix with infix";
  }
}

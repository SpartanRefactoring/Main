package il.org.spartan.bloater.bloaters;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert {@code
 * ++i; --i;
} to * {@code
 * i+=1; i-=1;
 *
 * } Test case is {@link Issue1005}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @author Dor Ma'ayan
 * @since 2016-12-24 */
public class PrefixToInfix extends PrefixExprezzion implements TipperCategory.Bloater {
  private static final long serialVersionUID = -6058739737339786139L;

  public PrefixToInfix() {
    andAlso("Can be changed", () -> (iz.expressionStatement(current.getParent()) || iz.forStatement(current.getParent())));
  }

  @Override public Example[] examples() {
    return new Example[] { convert("++i;").to("i += 1;"), convert("--i;").to("i-=1;"), };
  }

  @Override protected ASTRewrite go(ASTRewrite $, TextEditGroup g) {
    NumberLiteral one = current.getAST().newNumberLiteral();
    one.setToken("1");
    if (operator == PrefixExpression.Operator.INCREMENT)
      $.replace(current, subject.pair(operand, one).to(Assignment.Operator.PLUS_ASSIGN), g);
    if (operator == PrefixExpression.Operator.DECREMENT)
      $.replace(current, subject.pair(operand, one).to(Assignment.Operator.MINUS_ASSIGN), g);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") PrefixExpression __) {
    return "Convert PrefixExpression to Infix Expreesion";
  }
}

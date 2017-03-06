package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert {@code
 * ++i; --i;
} to * {@code
 * i+=1; i-=1;
 *
 * } Test case is {@link Issue1005}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */
public class PrefixToInfix extends ReplaceCurrentNode<PrefixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 3775670587866472032L;

  @Override public ASTNode replacement(final PrefixExpression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1");
    return subject.pair(operand(¢), $).to(operator(¢) != PrefixExpression.Operator.DECREMENT ? Operator.PLUS_ASSIGN : Operator.MINUS_ASSIGN);
  }

  @Override protected boolean prerequisite(final PrefixExpression ¢) {
    final ASTNode $ = parent(¢);
    return (operator(¢) == PrefixExpression.Operator.INCREMENT || operator(¢) == PrefixExpression.Operator.DECREMENT)
        && (iz.expressionStatement($) || iz.forStatement($));
  }

  @Override @SuppressWarnings("unused") public String description(final PrefixExpression __) {
    return null;
  }
}

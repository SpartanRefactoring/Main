package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.safety.iz.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert
 *
 * <pre>
 * ++i; --i;
 *
 * <pre>
 * to
 *
 * <pre>
 * i+=1; i-=1;
 *
 * <pre>
 * Test case is {@link Issue1005}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */
public class PrefixToInfix extends ReplaceCurrentNode<PrefixExpression>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final PrefixExpression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1");
    return subject.pair(step.operand(¢), $)
        .to(step.operator(¢) != PrefixExpression.Operator.DECREMENT ? Operator.PLUS_ASSIGN : Operator.MINUS_ASSIGN);
  }

  @Override protected boolean prerequisite(final PrefixExpression ¢) {
    final ASTNode $ = step.parent(¢);
    return (step.operator(¢) == PrefixExpression.Operator.INCREMENT || step.operator(¢) == PrefixExpression.Operator.DECREMENT)
        && (expressionStatement($) || forStatement($));
  }

  @Override @SuppressWarnings("unused") public String description(final PrefixExpression __) {
    return null;
  }
}

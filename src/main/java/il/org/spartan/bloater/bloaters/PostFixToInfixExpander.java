package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Chage prfix expression to infix expression when possible toList Expand :
 * {@code
 * i++ / i-- ;
 * } To : {@code
 * i=i+1 / i=i-1 ;
 * } Test class is {@link Issue0974}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-09 */
public class PostFixToInfixExpander extends ReplaceCurrentNode<PostfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 2322066430397426252L;

  @Override @Nullable public ASTNode replacement(@NotNull final PostfixExpression x) {
    if (x.getOperator() != Operator.INCREMENT && x.getOperator() != Operator.DECREMENT)
      return null;
    final Expression one = az.expression(wizard.ast("1"));
    final Assignment $ = subject
        .pair(x.getOperand(),
            x.getOperator() == Operator.DECREMENT ? subject.pair(x.getOperand(), one).to(InfixExpression.Operator.MINUS)
                : x.getOperator() == Operator.INCREMENT ? subject.pair(x.getOperand(), one).to(InfixExpression.Operator.PLUS) : null)
        .to(Assignment.Operator.ASSIGN);
    return !needWrap(x) ? $ : subject.operand($).parenthesis();
  }

  private static boolean needWrap(@NotNull final PostfixExpression ¢) {
    final ASTNode $ = ¢.getParent();
    return iz.infixExpression($) || iz.prefixExpression($) || iz.postfixExpression($);
  }

  @Override @Nullable public String description(@SuppressWarnings("unused") final PostfixExpression __) {
    return null;
  }
}

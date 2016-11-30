package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert
 * 
 * <pre>
 * a - b // ==//<//> c - d
 * </pre>
 * 
 * into
 * 
 * <pre>
 * a +d// ==//<//> c + b
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 18-11-2016 */
public class SimplifyComparisionOfSubtractions extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(InfixExpression x) {
    if (!isLiegal(x) || !az.infixExpression(x.getLeftOperand()).extendedOperands().isEmpty()
        || !az.infixExpression(x.getRightOperand()).extendedOperands().isEmpty())
      return null;
    Expression ll = az.infixExpression(x.getLeftOperand()).getLeftOperand();
    Expression lr = az.infixExpression(x.getLeftOperand()).getRightOperand();
    Expression rl = az.infixExpression(x.getRightOperand()).getLeftOperand();
    Expression rr = az.infixExpression(x.getRightOperand()).getRightOperand();
    return iz.infixExpression(rr) || iz.infixExpression(rl) || iz.infixExpression(lr) || iz.infixExpression(ll) || iz.numberLiteral(ll)
        || iz.numberLiteral(lr) || iz.numberLiteral(rl) || iz.numberLiteral(rr) ? null
            : subject.pair(subject.pair(ll, rr).to(Operator.PLUS), subject.pair(rl, lr).to(Operator.PLUS)).to(x.getOperator());
  }
  
  private static boolean isLiegal(InfixExpression ¢){
    return iz.infixMinus(¢.getLeftOperand()) && iz.infixMinus(¢.getRightOperand());
  }

  @Override public String description(InfixExpression ¢) {
    return "Simplify the comparison expression: " + ¢;
  }
}

package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

/**
 * Convert a multiplication of expression\statement by zero to zero <br/>
 * where there is no any side effect
 * @author Dor Ma'ayan
 * @since 2016-09-25
 * @see {@link sideEffects}
 *
 */
public class InfixMultiplicationByZero extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {

  @Override public ASTNode replacement(InfixExpression n) {
    if(n.getOperator()!=TIMES)
      return null;
    if(iz.numberLiteral(n.getLeftOperand()) && //
       az.numberLiteral(n.getLeftOperand()).getToken().equals("0")){
      NumberLiteral $ = n.getAST().newNumberLiteral();
      $.setToken("0");
      return $; 
    }
    if(iz.numberLiteral(n.getRightOperand()) && //
        az.numberLiteral(n.getRightOperand()).getToken().equals("0")){
      NumberLiteral $ = n.getAST().newNumberLiteral();
      $.setToken("0");
      return $; 
    }
    return null;
  }

  @Override public String description(InfixExpression n) {
    return "Convert" + n + " to 0";
  }
}

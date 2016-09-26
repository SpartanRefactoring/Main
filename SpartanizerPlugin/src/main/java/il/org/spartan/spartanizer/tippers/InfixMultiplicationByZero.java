package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

/**
 * Convert a multiplication of expression\statement by zero to zero <br/>
 * where there is no any side effect
 * @author Dor Ma'ayan
 * @since 2016-09-25
 * @see {@link sideEffects}
 *
 */
public class InfixMultiplicationByZero extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {

  private boolean isContainsZero(InfixExpression ¢){
    List<Expression> operands = extract.allOperands(¢);
    for(Expression e : operands)
      if (iz.numberLiteral(e) && az.numberLiteral(e).getToken().equals("0"))
        return true;
    return false;
  }
  
  private boolean isContainsSideEffect(InfixExpression ¢){
    List<Expression> operands = extract.allOperands(¢);
    for(Expression e : operands)
      if (!sideEffects.free(e))
        return true;
    return false;
  }
  @Override public ASTNode replacement(InfixExpression ¢) {
    if (¢.getOperator() != TIMES || !isContainsZero(¢) || isContainsSideEffect(¢))
      return null;
    NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("0");
    return $;
  }

  @Override public String description(InfixExpression ¢) {
    return "Convert" + ¢ + " to 0";
  }
}

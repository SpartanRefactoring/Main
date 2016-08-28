package il.org.spartan.refactoring.wring;


import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/**
 * Evaluate the addition of double numbers :
 * <pre>
 * 3.0+4+1
 * </pre>
 * to:
 * <pre>
 * 8.0
 * </pre>
 * @author Dor Ma'ayan 
 * @since 2016
 */
public class EvaluateAdditionDouble extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {

  @Override public String description() {
      return "Evaluate addition of double numbers";
  }

  @Override String description(@SuppressWarnings("unused") InfixExpression e) {
    return "Evaluate addition of double numbers";
  }

  @Override ASTNode replacement(InfixExpression e) {
    return e.getOperator() != PLUS ? null : replacement(extract.allOperands(e),e);
  }
  
  private static boolean isDouble(Expression e){
    if(!(e instanceof NumberLiteral))
      return false;
    return ((NumberLiteral) e).getToken().matches("[0-9]+.[0-9]+");
  }
  
  private static ASTNode replacement(final List<Expression> es, InfixExpression e) {
    double sum = 0;
    for (final Expression ¢ : es){
      if (!(¢ instanceof NumberLiteral && isDouble(¢)))
        return null;
      sum=sum + Double.parseDouble(((NumberLiteral) ¢).getToken());
    }  
    return e.getAST().newNumberLiteral(Double.toString(sum));
  }
}

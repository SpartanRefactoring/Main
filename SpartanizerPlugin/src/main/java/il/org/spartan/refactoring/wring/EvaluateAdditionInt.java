package il.org.spartan.refactoring.wring;


import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/**
 * Evaluate the addition of integer numbers :
 * <pre>
 * 3+4+1
 * </pre>
 * to:
 * <pre>
 * 8
 * </pre>
 * @author Dor Ma'ayan 
 * @since 2016
 */
public class EvaluateAdditionInt extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {

  @Override public String description() {
      return "Evaluate addition of int numbers";
  }

  @Override String description(@SuppressWarnings("unused") InfixExpression e) {
    return "Evaluate addition of int numbers";
  }

  @Override ASTNode replacement(InfixExpression e) {
    return e.getOperator() != PLUS ? null : replacement(extract.allOperands(e),e);
  }
  
  private static boolean isInt(Expression e){
    if(!(e instanceof NumberLiteral))
      return false;
    return ((NumberLiteral) e).getToken().matches("[0-9]+");
  }
  
  private static ASTNode replacement(final List<Expression> es, InfixExpression e) {
    int sum = 0;
    for (final Expression ¢ : es){
      if (!(¢ instanceof NumberLiteral && isInt(¢)))
        return null;
      sum=sum + Integer.parseInt(((NumberLiteral) ¢).getToken());
    }  
    return e.getAST().newNumberLiteral(Integer.toString(sum));
  }
}

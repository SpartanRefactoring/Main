package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/**
 * Evaluate the multiplication of integer numbers :
 * <pre>
 * 3*4*2
 * </pre>
 * to:
 * <pre>
 * 24
 * </pre>
 * @author Dor Ma'ayan 
 * @since 2016
 */
public class EvaluateMultiplicationInt extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {

  @Override public String description() {
      return "Evaluate multiplication of int numbers";
  }

  @Override String description(@SuppressWarnings("unused") InfixExpression __) {
    return "Evaluate multiplication of int numbers";
  }

  @Override ASTNode replacement(InfixExpression e) {
    return e.getOperator() != TIMES ? null : replacement(extract.allOperands(e),e);
  }
  
  
  private static int extractNumber(Expression e){
    return !(e instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) e).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }
  
  private static boolean isInt(Expression e){
    return e instanceof NumberLiteral && ((NumberLiteral) e).getToken().matches("[0-9]+");
  }
  
  private static ASTNode replacement(final List<Expression> es, InfixExpression e) {
    int mul = 1;
    for (final Expression ¢ : es){
      if ((!(¢ instanceof NumberLiteral) || !isInt(¢))
          && (!(¢ instanceof PrefixExpression) || ((PrefixExpression) ¢).getOperator() != PrefixExpression.Operator.MINUS
              || !(((PrefixExpression) ¢).getOperand() instanceof NumberLiteral)))
        return null;
        mul = mul * extractNumber(¢);
    }  
    return e.getAST().newNumberLiteral(Integer.toString(mul));
  }
}

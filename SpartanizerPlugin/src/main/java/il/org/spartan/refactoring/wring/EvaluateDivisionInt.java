package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/**
 * Evaluate the division of integer numbers --> the result is also an integer :
 * <pre>
 * 7/8
 * </pre>
 * to:
 * <pre>
 * 0
 * </pre>
 * @author Dor Ma'ayan 
 * @since 2016
 */
public class EvaluateDivisionInt extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {

  @Override public String description() {
      return "Evaluate division of int numbers";
  }

  @Override String description(@SuppressWarnings("unused") InfixExpression __) {
    return "Evaluate division of int numbers";
  }

  @Override ASTNode replacement(InfixExpression e) {
    return e.getOperator() != DIVIDE ? null : replacement(extract.allOperands(e),e);
  }
  
  private static int extractNumber(Expression e){
    return !(e instanceof PrefixExpression) ? Integer.parseInt(((NumberLiteral) e).getToken())
        : -1 * Integer.parseInt(((NumberLiteral) ((PrefixExpression) e).getOperand()).getToken());
  }
  
  private static boolean isInt(Expression e){
    return e instanceof NumberLiteral && ((NumberLiteral) e).getToken().matches("[0-9]+");
  }

  private static boolean isCompitable(Expression e){
    return ((!(e instanceof NumberLiteral) || !isInt(e)) && (!(e instanceof PrefixExpression)
        || ((PrefixExpression) e).getOperator() != PrefixExpression.Operator.MINUS || !(((PrefixExpression) e).getOperand() instanceof NumberLiteral)));
  }
  
  private static ASTNode replacement(final List<Expression> es, InfixExpression e) {
    if (es.isEmpty() || isCompitable(es.get(0)))
      return null;
    int divide = extractNumber(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if (isCompitable(¢))
        return null;
      if (index != 0)
        divide = divide / extractNumber(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Integer.toString(divide));
  }
}

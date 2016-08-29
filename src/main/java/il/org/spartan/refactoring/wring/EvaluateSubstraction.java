package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/**
 * Evaluate the subtraction of numbers according to the following rules <br/> <br/>
 * <code>
 * int - int --> int <br/>
 * double - double --> double <br/>
 * long - long --> long <br/>
 * int - double --> double <br/>
 * int - long --> long <br/>
 * long - double --> double <br/>
 * </code>
 * 
 * 
 * @author Dor Ma'ayan
 * @since 2016 */

public class EvaluateSubstraction extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate substraction of int numbers";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    if( e.getOperator() != MINUS )
      return null;
    switch(EvaluateAux.getEvaluatedType(e)){
      case INT :
        return replacementInt(extract.allOperands(e),e);
      case DOUBLE :
        return replacementDouble(extract.allOperands(e),e);
      default:
        return null;
    }
  }

  private static ASTNode replacementInt(final List<Expression> es, InfixExpression e) {
    if (es.isEmpty() && !EvaluateAux.isCompitable(es.get(0)))
      return null;
    int sub = EvaluateAux.extractInt(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if ((!(¢ instanceof NumberLiteral) || !EvaluateAux.isInt(¢)))
        return null;
      if (index != 0)
        sub = sub - EvaluateAux.extractInt(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Integer.toString(sub));
  }
  
  private static ASTNode replacementDouble(final List<Expression> es, InfixExpression e) {
    if (es.isEmpty()&& !EvaluateAux.isCompitable(es.get(0)))
      return null;
    double sub = EvaluateAux.extractDouble(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if ((!(¢ instanceof NumberLiteral) || !EvaluateAux.isNumber(¢)))
        return null;
      if (index != 0)
        sub = sub - EvaluateAux.extractDouble(¢);
      index++;
    }
    return e.getAST().newNumberLiteral(Double.toString(sub));
  }
}

package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Evaluate the remainder of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int % int --> int <br/>
 * long % long --> long <br/>
 * int % long --> long <br/>
 * long % int --> long <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class EvaluateRemainder extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    int remainder = EvaluateAux.extractInt(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        remainder %= EvaluateAux.extractInt(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(remainder));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    long remainder = EvaluateAux.extractLong(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        remainder %= EvaluateAux.extractLong(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Long.toString(remainder) + "L");
  }

  @Override public String description() {
    return "Evaluate remainder of numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression ____) {
    return "Evaluate remainder of numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    int sourceLength = x.toString().length();
    ASTNode resultExpression;
    if (x.getOperator() != REMAINDER)
      return null;
    switch (EvaluateAux.getEvaluatedType(x)) {
      case INT:
        resultExpression =  replacementInt(extract.allOperands(x), x);
        break;
      case LONG:
        resultExpression =  replacementLong(extract.allOperands(x), x);
        break;
      default :
        return null;
    }
    if(resultExpression == null)
      return null;
    int resultLength = az.numberLiteral(resultExpression).getToken().length();
    if(resultLength < sourceLength){
      return resultExpression;
    }
    return null;
  }
}

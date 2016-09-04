package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;

/** Evaluate the subtraction of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int - int --> int <br/>
 * double - double --> double <br/>
 * long - long --> long <br/>
 * int - double --> double <br/>
 * int - long --> long <br/>
 * long - double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class EvaluateSubstraction extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    double sub = EvaluateAux.extractDouble(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!(¢ instanceof NumberLiteral) || !EvaluateAux.isNumber(¢))
        return null;
      if (index != 0)
        sub -= EvaluateAux.extractDouble(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Double.toString(sub));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    int sub = EvaluateAux.extractInt(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!(¢ instanceof NumberLiteral) || !EvaluateAux.isInt(¢))
        return null;
      if (index != 0)
        sub -= EvaluateAux.extractInt(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(sub));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    long sub = EvaluateAux.extractLong(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!(¢ instanceof NumberLiteral) || !EvaluateAux.isNumber(¢))
        return null;
      if (index != 0)
        sub -= EvaluateAux.extractLong(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Long.toString(sub) + "L");
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate substraction of numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != MINUS)
      return null;
    switch (EvaluateAux.getEvaluatedType(x)) {
      case INT:
        return replacementInt(extract.allOperands(x), x);
      case DOUBLE:
        return replacementDouble(extract.allOperands(x), x);
      case LONG:
        return replacementLong(extract.allOperands(x), x);
      default:
        return null;
    }
  }
}

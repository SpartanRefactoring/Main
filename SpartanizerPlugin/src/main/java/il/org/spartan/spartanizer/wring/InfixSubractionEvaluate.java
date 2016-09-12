package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

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
public class InfixSubractionEvaluate extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompatible(first(xs)))
      return null;
    double sub = EvaluateAux.extractDouble(first(xs));
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
    if (xs.isEmpty() && !EvaluateAux.isCompatible(first(xs)))
      return null;
    int sub = EvaluateAux.extractInt(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!(¢ instanceof NumberLiteral) || !type.isInt(¢))
        return null;
      if (index != 0)
        sub -= EvaluateAux.extractInt(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(sub));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompatible(first(xs)))
      return null;
    long sub = EvaluateAux.extractLong(first(xs));
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
    return "Evaluate subtraction of numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != MINUS || EvaluateAux.getEvaluatedType(x) == null)
      return null;
    switch (EvaluateAux.getEvaluatedType(x).asPrimitiveCertain()) {
      case INT:
        $ = replacementInt(extract.allOperands(x), x);
        break;
      case DOUBLE:
        $ = replacementDouble(extract.allOperands(x), x);
        break;
      case LONG:
        $ = replacementLong(extract.allOperands(x), x);
        break;
      default:
        return null;
    }
    return $ != null && az.numberLiteral($).getToken().length() < sourceLength ? $ : null;
  }
}

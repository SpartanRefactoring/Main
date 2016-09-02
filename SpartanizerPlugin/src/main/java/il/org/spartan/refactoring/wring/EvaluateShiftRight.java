package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;

/** Evaluate the arithmetic shift right of numbers according to the following
 * rules <br/>
 * <br/>
 * <code>
 * int >> int --> int <br/>
 * long >> long --> long <br/>
 * long >> int --> long <br/>
 * int >> long --> int <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class EvaluateShiftRight extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate substraction of numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != RIGHT_SHIFT_SIGNED)
      return null;
    switch (EvaluateAux.getEvaluatedTypeForShift(x)) {
      case INT:
        return replacementInt(extract.allOperands(x), x);
      case LONG:
        return replacementLong(extract.allOperands(x), x);
      default:
        return null;
    }
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    int shifted = EvaluateAux.extractInt(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!(¢ instanceof NumberLiteral) && !EvaluateAux.isInt(¢) && !EvaluateAux.isLong(¢))
        return null;
      if (index != 0) {
        if (EvaluateAux.isInt(¢))
          shifted >>= EvaluateAux.extractInt(¢);
        if (EvaluateAux.isLong(¢))
          shifted >>= EvaluateAux.extractLong(¢);
      }
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(shifted));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() && !EvaluateAux.isCompitable(xs.get(0)))
      return null;
    long shifted = EvaluateAux.extractLong(xs.get(0));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!(¢ instanceof NumberLiteral) && !EvaluateAux.isInt(¢) && !EvaluateAux.isLong(¢))
        return null;
      if (index != 0) {
        if (EvaluateAux.isInt(¢))
          shifted >>= EvaluateAux.extractInt(¢);
        if (EvaluateAux.isLong(¢))
          shifted >>= EvaluateAux.extractLong(¢);
      }
      ++index;
    }
    return x.getAST().newNumberLiteral(Long.toString(shifted) + "L");
  }
}

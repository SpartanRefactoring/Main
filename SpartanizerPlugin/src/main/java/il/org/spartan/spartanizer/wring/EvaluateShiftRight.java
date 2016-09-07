package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

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
  private static ASTNode replacementInt(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() && !EvaluateAux.isCompitable(es.get(0)))
      return null;
    int shifted = EvaluateAux.extractInt(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
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
    return e.getAST().newNumberLiteral(Integer.toString(shifted));
  }

  private static ASTNode replacementLong(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() && !EvaluateAux.isCompitable(es.get(0)))
      return null;
    long shifted = EvaluateAux.extractLong(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
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
    return e.getAST().newNumberLiteral(Long.toString(shifted) + "L");
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate substraction of numbers";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    if (e.getOperator() != RIGHT_SHIFT_SIGNED)
      return null;
    switch (EvaluateAux.getEvaluatedTypeForShift(e)) {
      case INT:
        return replacementInt(extract.allOperands(e), e);
      case LONG:
        return replacementLong(extract.allOperands(e), e);
      default:
        return null;
    }
  }
}

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
  private static ASTNode replacementInt(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() || !EvaluateAux.isCompitable(es.get(0)))
      return null;
    int remainder = EvaluateAux.extractInt(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        remainder %= EvaluateAux.extractInt(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Integer.toString(remainder));
  }

  private static ASTNode replacementLong(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() || !EvaluateAux.isCompitable(es.get(0)))
      return null;
    long remainder = EvaluateAux.extractLong(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        remainder %= EvaluateAux.extractLong(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Long.toString(remainder) + "L");
  }

  @Override public String description() {
    return "Evaluate remainder of numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate remainder of numbers";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    if (e.getOperator() != REMAINDER)
      return null;
    switch (EvaluateAux.getEvaluatedType(e)) {
      case INT:
        return replacementInt(extract.allOperands(e), e);
      case LONG:
        return replacementLong(extract.allOperands(e), e);
      default:
        return null;
    }
  }
}

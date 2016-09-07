package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Evaluate the subtraction of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int / int --> int <br/>
 * double / double --> double <br/>
 * long / long --> long <br/>
 * int / double --> double <br/>
 * int / long --> long <br/>
 * long / double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class EvaluateDivision extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacementDouble(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() || !EvaluateAux.isCompitable(es.get(0)))
      return null;
    double divide = EvaluateAux.extractDouble(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        divide /= EvaluateAux.extractDouble(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Double.toString(divide));
  }

  private static ASTNode replacementInt(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() || !EvaluateAux.isCompitable(es.get(0)))
      return null;
    int divide = EvaluateAux.extractInt(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        divide /= EvaluateAux.extractInt(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Integer.toString(divide));
  }

  private static ASTNode replacementLong(final List<Expression> es, final InfixExpression e) {
    if (es.isEmpty() || !EvaluateAux.isCompitable(es.get(0)))
      return null;
    long divide = EvaluateAux.extractLong(es.get(0));
    int index = 0;
    for (final Expression ¢ : es) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      if (index != 0)
        divide /= EvaluateAux.extractLong(¢);
      ++index;
    }
    return e.getAST().newNumberLiteral(Long.toString(divide) + "L");
  }

  @Override public String description() {
    return "Evaluate division of numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate division of numbers";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    if (e.getOperator() != DIVIDE)
      return null;
    switch (EvaluateAux.getEvaluatedType(e)) {
      case INT:
        return replacementInt(extract.allOperands(e), e);
      case DOUBLE:
        return replacementDouble(extract.allOperands(e), e);
      case LONG:
        return replacementLong(extract.allOperands(e), e);
      default:
        return null;
    }
  }
}

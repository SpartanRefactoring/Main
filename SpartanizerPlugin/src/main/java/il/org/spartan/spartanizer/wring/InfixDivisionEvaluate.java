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
 * int / int --> int <br/>
 * double / double --> double <br/>
 * long / long --> long <br/>
 * int / double --> double <br/>
 * int / long --> long <br/>
 * long / double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class InfixDivisionEvaluate extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompatible(first(xs)))
      return null;
    double divide = EvaluateAux.extractDouble(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompatible(¢))
        return null;
      if (index != 0)
        divide /= EvaluateAux.extractDouble(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Double.toString(divide));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompatible(first(xs)))
      return null;
    int divide = EvaluateAux.extractInt(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompatible(¢))
        return null;
      if (index != 0)
        divide /= EvaluateAux.extractInt(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(divide));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !EvaluateAux.isCompatible(first(xs)))
      return null;
    long divide = EvaluateAux.extractLong(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompatible(¢))
        return null;
      if (index != 0)
        divide /= EvaluateAux.extractLong(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Long.toString(divide) + "L");
  }

  @Override public String description() {
    return "Evaluate division of numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate division of numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != DIVIDE)
      return null;
    switch (EvaluateAux.getEvaluatedType(x)) {
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

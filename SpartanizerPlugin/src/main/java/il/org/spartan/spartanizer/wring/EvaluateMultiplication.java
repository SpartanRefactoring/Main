package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Evaluate the multiplication of numbers according to the following rules :
 * </br>
 * </br>
 * <code>
 * int * int --> int <br/>
 * double * double --> double <br/>
 * long * long --> long <br/>
 * int * double --> double <br/>
 * int * long --> long <br/>
 * long * double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class EvaluateMultiplication extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    double mul = 1;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      mul *= EvaluateAux.extractDouble(¢);
    }
    return x.getAST().newNumberLiteral(Double.toString(mul));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    int mul = 1;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      mul *= EvaluateAux.extractInt(¢);
    }
    return x.getAST().newNumberLiteral(Integer.toString(mul));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    long mul = 1;
    for (final Expression ¢ : xs) {
      if (!EvaluateAux.isCompitable(¢))
        return null;
      mul *= EvaluateAux.extractLong(¢);
    }
    return x.getAST().newNumberLiteral(Long.toString(mul) + "L");
  }

  @Override public String description() {
    return "Evaluate multiplication of numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate multiplication numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != TIMES)
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

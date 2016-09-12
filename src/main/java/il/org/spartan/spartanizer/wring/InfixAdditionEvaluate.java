package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Evaluate the addition of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int + int --> int <br/>
 * double + double --> double <br/>
 * long + long --> long <br/>
 * int + double --> double <br/>
 * int + long --> long <br/>
 * long + double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public class InfixAdditionEvaluate extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    double sum = 0;
    for (final Expression ¢ : xs) {
      if (!iz.computable(¢))
        return null;
      sum += extract.doubleNumber(¢);
    }
    return x.getAST().newNumberLiteral(Double.toString(sum));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    int sum = 0;
    for (final Expression ¢ : xs) {
      if (!iz.computable(¢))
        return null;
      sum += extract.intNumber(¢);
    }
    return x.getAST().newNumberLiteral(Integer.toString(sum));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    long sum = 0;
    for (final Expression ¢ : xs) {
      if (!iz.computable(¢))
        return null;
      sum += extract.longNumber(¢);
    }
    return x.getAST().newNumberLiteral(Long.toString(sum) + "L");
  }

  @Override public String description() {
    return "Evaluate addition of int numbers";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate addition of int numbers";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    if(!iz.validForEvaluation(x))
      return null;
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != PLUS)
      return null;
    if (type.get(x) == INT)
      $ = replacementInt(extract.allOperands(x), x);
    else {
      if (type.get(x) == DOUBLE)
        $ = replacementDouble(extract.allOperands(x), x);
      else {
        if (type.get(x) == LONG)
          $ = replacementLong(extract.allOperands(x), x);
        else
          return null;
      }
    }
    return $!=null && az.numberLiteral($).getToken().length() < sourceLength ? $ : null;
  }
}

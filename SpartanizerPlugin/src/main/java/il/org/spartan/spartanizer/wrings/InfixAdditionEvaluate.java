package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

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
public final class InfixAdditionEvaluate extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    double sum = 0;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      sum += az.boxed.double¢(¢);
    }
    return x.getAST().newNumberLiteral(Double.toString(sum));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    int sum = 0;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      final Integer int¢ = az.boxed.int¢(¢);
      if (int¢ == null)
        return null;
      sum += int¢.intValue();
    }
    return x.getAST().newNumberLiteral(Integer.toString(sum));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    long sum = 0;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      sum += az.boxed.long¢(¢);
    }
    return x.getAST().newNumberLiteral(Long.toString(sum) + "L");
  }

  @Override public String description() {
    return "Evaluate addition of int numbers";
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate addition of int numbers";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    if (!iz.validForEvaluation(x))
      return null;
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != PLUS)
      return null;
    if (type.get(x) == INT)
      $ = replacementInt(extract.allOperands(x), x);
    else if (type.get(x) == DOUBLE)
      $ = replacementDouble(extract.allOperands(x), x);
    else {
      if (type.get(x) != LONG)
        return null;
      $ = replacementLong(extract.allOperands(x), x);
    }
    return $ != null && az.numberLiteral($).getToken().length() < sourceLength ? $ : null;
  }
}

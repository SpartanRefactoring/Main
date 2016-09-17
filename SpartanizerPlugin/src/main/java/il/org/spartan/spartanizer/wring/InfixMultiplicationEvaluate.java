package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.ast.extract.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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
public class InfixMultiplicationEvaluate extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    double $ = 1;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      Double d = az.boxed.double¢(¢);
      if (d == null)
        return null;
      $ *= d.doubleValue();
    }
    return x.getAST().newNumberLiteral(Double.toString($));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    int $ = 1;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      Integer i = az.boxed.int¢(¢);
      if (i == null)
        return null;
      $ *= i.intValue();
    }
    return x.getAST().newNumberLiteral(Integer.toString($));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    long $ = 1;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      Long l = az.boxed.long¢(¢);
      if (l == null)
        return null;
      $ *= l.longValue(); 
    }
    return x.getAST().newNumberLiteral(Long.toString($) + "L");
  }

  @Override public String description() {
    return "Evaluate multiplication of numbers";
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate multiplication numbers";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    if (!iz.validForEvaluation(x))
      return null;
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != TIMES)
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

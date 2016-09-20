package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

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
public final class InfixMultiplicationEvaluate extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    double $ = 1;
    for (final Expression ¢ : xs) {
      if (!iz.pseudoNumber(¢))
        return null;
      final Double d = az.boxed.double¢(¢);
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
      final Integer i = az.boxed.int¢(¢);
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
      final Long l = az.boxed.long¢(¢);
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
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (type.of(x) == INT)
      $ = replacementInt(extract.allOperands(x), x);
    else if (type.of(x) == DOUBLE)
      $ = replacementDouble(extract.allOperands(x), x);
    else {
      if (type.of(x) != LONG)
        return null;
      $ = replacementLong(extract.allOperands(x), x);
    }
    return $ != null && az.numberLiteral($).getToken().length() < sourceLength ? $ : null;
  }

  @Override protected boolean prerequisite(final InfixExpression ¢) {
    return ¢.getOperator() == TIMES && iz.validForEvaluation(¢);
  }
}

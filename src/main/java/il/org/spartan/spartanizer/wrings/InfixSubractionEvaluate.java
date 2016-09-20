package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Evaluate the subtraction of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int - int --> int <br/>
 * double - double --> double <br/>
 * long - long --> long <br/>
 * int - double --> double <br/>
 * int - long --> long <br/>
 * long - double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixSubractionEvaluate extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.numberLiteral(first(xs)) || !iz.pseudoNumber(first(xs)))
      return null;
    double sub = az.boxed.double¢(first(xs));
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!iz.numberLiteral(¢) || !iz.pseudoNumber(¢))
        return null;
      if (index != 0)
        sub -= az.boxed.double¢(¢);
      ++index;
    }
    return x.getAST().newNumberLiteral(Double.toString(sub));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.numberLiteral(first(xs)) || !iz.pseudoNumber(first(xs)))
      return null;
    final Integer z = az.boxed.int¢(first(xs));
    if (z == null)
      return null;
    int sub = z.intValue();
    int index = 0;
    for (final Expression ¢ : xs) {
      if (!iz.numberLiteral(¢) || !type.isInt(¢))
        return null;
      if (index != 0) {
        final Integer int¢ = az.boxed.int¢(¢);
        if (int¢ == null)
          return null;
        sub -= int¢.intValue();
      }
      ++index;
    }
    return x.getAST().newNumberLiteral(Integer.toString(sub));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.numberLiteral(first(xs)) || !iz.pseudoNumber(first(xs)))
      return null;
    long sub = az.boxed.long¢(first(xs));
    for (final Expression ¢ : lisp.rest(xs)) {
      if (!iz.numberLiteral(¢) || !iz.pseudoNumber(¢))
        return null;
      sub -= az.boxed.long¢(¢);
    }
    return x.getAST().newNumberLiteral(Long.toString(sub) + "L");
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate subtraction of numbers";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    if (!iz.validForEvaluation(x))
      return null;
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != MINUS)
      return null;
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
}

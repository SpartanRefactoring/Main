package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

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
public class InfixDivisionEvaluate extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacementDouble(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.pseudoNumber(first(xs)))
      return null;
    double divide = az.boxed.double¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (!iz.pseudoNumber(¢) || az.boxed.double¢(¢) == 0)
        return null;
      divide /= az.boxed.double¢(¢);
    }
    return x.getAST().newNumberLiteral(Double.toString(divide));
  }

  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.pseudoNumber(first(xs)))
      return null;
    int divide = az.boxed.int¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (!iz.pseudoNumber(¢) || az.boxed.int¢(¢) == 0)
        return null;
      divide /= az.boxed.int¢(¢);
    }
    return x.getAST().newNumberLiteral(Integer.toString(divide));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.pseudoNumber(first(xs)))
      return null;
    long divide = az.boxed.long¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (!iz.pseudoNumber(¢) || az.boxed.long¢(¢) == 0)
        return null;
      divide /= az.boxed.long¢(¢);
    }
    return x.getAST().newNumberLiteral(Long.toString(divide) + "L");
  }

  @Override public String description() {
    return "Evaluate division of numbers";
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate division of numbers";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    if (!iz.validForEvaluation(x))
      return null;
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (x.getOperator() != DIVIDE)
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

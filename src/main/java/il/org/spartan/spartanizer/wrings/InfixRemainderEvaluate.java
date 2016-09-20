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
public final class InfixRemainderEvaluate extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  private static ASTNode replacementInt(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.pseudoNumber(first(xs)))
      return null;
    int remainder = az.boxed.int¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (!iz.pseudoNumber(¢))
        return null;
      final Integer int¢ = az.boxed.int¢(¢);
      if (int¢ == null || int¢.intValue() == 0)
        return null;
      remainder %= int¢.intValue();
    }
    return x.getAST().newNumberLiteral(Integer.toString(remainder));
  }

  private static ASTNode replacementLong(final List<Expression> xs, final InfixExpression x) {
    if (xs.isEmpty() || !iz.pseudoNumber(first(xs)))
      return null;
    long remainder = az.boxed.long¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (!iz.pseudoNumber(¢) || az.boxed.long¢(¢) == 0)
        return null;
      remainder %= az.boxed.long¢(¢);
    }
    return x.getAST().newNumberLiteral(Long.toString(remainder) + "L");
  }

  @Override public String description() {
    return "Evaluate remainder of numbers";
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Evaluate remainder of numbers";
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    final int sourceLength = (x + "").length();
    ASTNode $;
    if (type.of(x) == INT)
      $ = replacementInt(extract.allOperands(x), x);
    else {
      if (type.of(x) != LONG)
        return null;
      $ = replacementLong(extract.allOperands(x), x);
    }
    return $ != null && az.numberLiteral($).getToken().length() < sourceLength ? $ : null;
  }

  @Override protected boolean prerequisite(final InfixExpression ¢) {
    return ¢.getOperator() == REMAINDER && iz.validForEvaluation(¢);
  }
}

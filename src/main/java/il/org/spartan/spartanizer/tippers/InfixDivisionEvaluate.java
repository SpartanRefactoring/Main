package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.utils.*;

/** Evaluate the subtraction of numbers according to the following rules {@code
 * int / int --> int
 * double / double --> double
 * long / long --> long
 * int / double --> double
 * int / long --> long
 * long / double --> double
 * }
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixDivisionEvaluate extends $EvaluateInfixExpression {
  private static final long serialVersionUID = 3552466561840604448L;

  @Override double evaluateDouble(final List<Expression> xs) throws IllegalArgumentException {
    double $ = 0;
    try {
      $ = az.throwing.double¢(first(xs));
      for (final Expression ¢ : rest(xs)) {
        if (az.throwing.double¢(¢) == 0)
          throw new IllegalArgumentException("Cannot evaluate division by zero");
        $ /= az.throwing.double¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override int evaluateInt(final List<Expression> xs) throws IllegalArgumentException {
    int $ = 0;
    try {
      if (type.of(first(xs)) == Certain.DOUBLE || type.of(first(xs)) == Certain.LONG)
        throw new NumberFormatException();
      $ = az.throwing.int¢(first(xs));
      for (final Expression ¢ : rest(xs)) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        if (az.throwing.int¢(¢) == 0)
          throw new IllegalArgumentException("Cannot evaluate division by zero");
        $ /= az.throwing.int¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) throws IllegalArgumentException {
    long $ = 0;
    try {
      if (type.of(first(xs)) == Certain.DOUBLE)
        throw new NumberFormatException();
      $ = az.throwing.long¢(first(xs));
      for (final Expression ¢ : rest(xs)) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        if (az.throwing.long¢(¢) == 0)
          throw new IllegalArgumentException("Cannot evaluate division by zero");
        $ /= az.throwing.long¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override String operation() {
    return "division";
  }

  @Override Operator operator() {
    return DIVIDE;
  }
}

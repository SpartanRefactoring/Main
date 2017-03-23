package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.utils.*;

/** Evaluate the multiplication of numbers according to the following rules :
 * toList toList {@code
 * int * int --> int
 * double * double --> double
 * long * long --> long
 * int * double --> double
 * int * long --> long
 * long * double --> double
 * }
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixMultiplicationEvaluate extends $EvaluateInfixExpression {
  private static final long serialVersionUID = 8014753129449325929L;

  @Override @SuppressWarnings("boxing") double evaluateDouble(@NotNull final List<Expression> $) {
    try {
      return $.stream().map(az.throwing::double¢).reduce(1.0, (x, y) -> x * y);
    } catch (@NotNull final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return 1;
  }

  @Override int evaluateInt(@NotNull final List<Expression> xs) {
    int $ = 1;
    try {
      for (@NotNull final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        $ *= az.throwing.int¢(¢);
      }
    } catch (@NotNull final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override long evaluateLong(@NotNull final List<Expression> xs) {
    long $ = 1;
    try {
      for (@NotNull final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        $ *= az.throwing.long¢(¢);
      }
    } catch (@NotNull final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override String operation() {
    return "multiplication";
  }

  @Override Operator operator() {
    return TIMES;
  }
}

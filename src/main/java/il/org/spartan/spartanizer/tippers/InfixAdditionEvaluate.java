package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.utils.*;

/** Evaluate the addition of numbers according to the following rules {@code
 * int + int --> int
 * double + double --> double
 * long + long --> long
 * int + double --> double
 * int + long --> long
 * long + double --> double
 * }
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixAdditionEvaluate extends $EvaluateInfixExpression {
  private static final long serialVersionUID = 675377550531970020L;

  @Override @SuppressWarnings("boxing") double evaluateDouble(@NotNull final List<Expression> xs) {
    double $ = 0;
    try {
      $ = xs.stream().map(az.throwing::double¢).reduce((x, y) -> x + y).get();
    } catch (@NotNull final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override int evaluateInt(@NotNull final List<Expression> xs) {
    int $ = 0;
    try {
      for (@NotNull final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        $ += az.throwing.int¢(¢);
      }
    } catch (@NotNull final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override long evaluateLong(@NotNull final List<Expression> xs) {
    long $ = 0;
    try {
      for (@NotNull final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        $ += az.throwing.long¢(¢);
      }
    } catch (@NotNull final NumberFormatException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return $;
  }

  @Override String operation() {
    return "addition";
  }

  @Override Operator operator() {
    return PLUS2;
  }
}

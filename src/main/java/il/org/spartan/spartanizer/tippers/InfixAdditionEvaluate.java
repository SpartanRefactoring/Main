package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;

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
public final class InfixAdditionEvaluate extends $EvaluateInfixExpression {
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
      for (final Expression ¢ : xs) {
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
      for (final Expression ¢ : xs) {
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

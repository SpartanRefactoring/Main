package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;

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
  private static final long serialVersionUID = 0x6F3A1F81DB470569L;

  @Override @SuppressWarnings("boxing") double evaluateDouble(final List<Expression> $) {
    try {
      return $.stream().map(az.throwing::double¢).reduce(1.0, (x, y) -> x * y);
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return 1;
  }

  @Override int evaluateInt(final List<Expression> xs) {
    int $ = 1;
    try {
      for (final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        $ *= az.throwing.int¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) {
    long $ = 1;
    try {
      for (final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        $ *= az.throwing.long¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
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

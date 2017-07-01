package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;

/** Evaluate the $ of numbers according to the following rules {@code
 * int % int --> int
 * long % long --> long
 * int % long --> long
 * long % int --> long
 * }
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixRemainderEvaluate extends $EvaluateInfixExpression {
  private static final long serialVersionUID = -0x466C277145AD335EL;

  @Override double evaluateDouble(final List<Expression> ¢) throws IllegalArgumentException {
    throw new IllegalArgumentException("no remainder among doubles" + ¢);
  }
  @Override int evaluateInt(final List<Expression> xs) throws IllegalArgumentException {
    int ret = 0;
    try {
      if (type.of(the.firstOf(xs)) == Certain.DOUBLE || type.of(the.firstOf(xs)) == Certain.LONG)
        throw new NumberFormatException();
      ret = az.throwing.int¢(the.firstOf(xs));
      for (final Expression ¢ : the.tailOf(xs)) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        final int int¢ = az.throwing.int¢(¢);
        if (int¢ == 0)
          throw new IllegalArgumentException("remainder in division by zero is undefined");
        ret %= int¢;
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return ret;
  }
  @Override long evaluateLong(final List<Expression> xs) throws IllegalArgumentException {
    for (final Expression ¢ : xs)
      if (type.of(¢) == Certain.DOUBLE)
        throw new NumberFormatException("Expected long or int in " + xs + " but found: " + ¢);
    long ret = 0;
    try {
      ret = az.throwing.long¢(the.firstOf(xs));
      for (final Expression ¢ : the.tailOf(xs)) {
        final long long¢ = az.throwing.long¢(¢);
        if (long¢ == 0)
          throw new IllegalArgumentException("Remainder in division by zero is undefined");
        ret %= long¢;
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return ret;
  }
  @Override String operation() {
    return "remainder";
  }
  @Override Operator operator() {
    return REMAINDER;
  }
}

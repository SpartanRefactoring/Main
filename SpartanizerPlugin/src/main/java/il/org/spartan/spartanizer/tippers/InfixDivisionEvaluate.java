package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;

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
  private static final long serialVersionUID = 0x314CE58426F66520L;

  @Override double evaluateDouble(final List<Expression> xs) throws IllegalArgumentException {
    double $ = 0;
    try {
      $ = az.throwing.double¢(the.headOf(xs));
      for (final Expression ¢ : the.tailOf(xs)) {
        if (az.throwing.double¢(¢) == 0)
          throw new IllegalArgumentException("Cannot evaluate division by zero");
        $ /= az.throwing.double¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return $;
  }
  @Override int evaluateInt(final List<Expression> xs) throws IllegalArgumentException {
    int $ = 0;
    try {
      if (type.of(the.headOf(xs)) == Certain.DOUBLE || type.of(the.headOf(xs)) == Certain.LONG)
        throw new NumberFormatException();
      $ = az.throwing.int¢(the.headOf(xs));
      for (final Expression ¢ : the.tailOf(xs)) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        if (az.throwing.int¢(¢) == 0)
          throw new IllegalArgumentException("Cannot evaluate division by zero");
        $ /= az.throwing.int¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return $;
  }
  @Override long evaluateLong(final List<Expression> xs) throws IllegalArgumentException {
    long $ = 0;
    try {
      if (type.of(the.headOf(xs)) == Certain.DOUBLE)
        throw new NumberFormatException();
      $ = az.throwing.long¢(the.headOf(xs));
      for (final Expression ¢ : the.tailOf(xs)) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        if (az.throwing.long¢(¢) == 0)
          throw new IllegalArgumentException("Cannot evaluate division by zero");
        $ /= az.throwing.long¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
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

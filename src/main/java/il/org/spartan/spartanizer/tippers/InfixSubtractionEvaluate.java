package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.utils.*;

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
public final class InfixSubtractionEvaluate extends $EvaluateInfixExpression {
  @Override double evaluateDouble(final List<Expression> xs) {
    double $ = 0;
    try {
      $ = az.throwing.double¢(first(xs));
      for (final Expression ¢ : rest(xs))
        $ -= az.throwing.double¢(¢);
    } catch (final NumberFormatException e) {
      monitor.logEvaluationError(this, e);
    }
    return $;
  }
  @Override int evaluateInt(final List<Expression> xs) {
    int $ = 0;
    try {
      $ = az.throwing.int¢(first(xs));
      for (final Expression ¢ : rest(xs)) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        $ -= az.throwing.int¢(¢);
      }
    } catch (final NumberFormatException e) {
      monitor.logEvaluationError(this, e);
    }
    return $;
  }
  @Override long evaluateLong(final List<Expression> xs) {
    long $ = 0;
    try {
      $ = az.throwing.long¢(first(xs));
      for (final Expression ¢ : rest(xs)) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        $ -= az.throwing.long¢(¢);
      }
    } catch (final NumberFormatException e) {
      monitor.logEvaluationError(this, e);
    }
    return $;
  }
  @Override String operation() {
    return "subtraction";
  }
  @Override Operator operator() {
    return MINUS2;
  }
}

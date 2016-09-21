package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.*;

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
public final class InfixDivisionEvaluate extends $EvaluateInfixExpression {
  double evaluateDouble(final List<Expression> xs) throws Exception {
    double $ = az.throwing.double¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (az.throwing.double¢(¢) == 0)
        throw new Exception("Cannot evaluate division by zero");
      $ /= az.throwing.double¢(¢);
    }
    return ($);
  }

  int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = az.throwing.int¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (az.throwing.int¢(¢) == 0)
        throw new Exception("Cannot evaluate division by zero");
      $ /= az.throwing.int¢(¢);
    }
    return ($);
  }

  long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = az.throwing.long¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (az.throwing.long¢(¢) == 0)
        throw new Exception("Cannot evaluate division by zero");
      $ /= az.throwing.long¢(¢);
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

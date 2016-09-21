package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.*;

/** Evaluate the $ of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int % int --> int <br/>
 * long % long --> long <br/>
 * int % long --> long <br/>
 * long % int --> long <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixRemainderEvaluate extends $EvaluateInfixExpression {
  @Override double evaluateDouble(final List<Expression> es) throws Exception {
    throw new Exception("no remainder among doubles" + es);
  }

  @Override int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = az.throwing.int¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      final int int¢ = az.throwing.int¢(¢);
      if (int¢ == 0)
        throw new Exception("remainder in division by zero is undefined");
      $ %= int¢;
    }
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = az.throwing.long¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (az.throwing.long¢(¢) == 0)
        throw new Exception("remainder in division by zero is undefined");
      $ %= az.throwing.long¢(¢);
    }
    return $;
  }

  @Override String operation() {
    return "remainder";
  }

  @Override Operator operator() {
    return REMAINDER;
  }
}

package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.safety.*;

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
  @Override double evaluateDouble(final List<Expression> ¢) throws Exception {
    throw new Exception("no remainder among doubles" + ¢);
  }

  @Override int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = 0 ;
    try{
    $ = az.throwing.int¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      final int int¢ = az.throwing.int¢(¢);
      if (int¢ == 0)
        throw new Exception("remainder in division by zero is undefined");
      $ %= int¢;
    }
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
    }
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = 0 ;
    try{
    $ = az.throwing.long¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      final long long¢ = az.throwing.long¢(¢);
      if (long¢ == 0)
        throw new Exception("remainder in division by zero is undefined");
      $ %= long¢;
    }
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
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

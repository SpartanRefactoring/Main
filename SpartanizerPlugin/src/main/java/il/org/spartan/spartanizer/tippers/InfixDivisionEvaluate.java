package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;

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
  @Override double evaluateDouble(final List<Expression> xs) throws Exception {
    double $ = 0;
    try{
    $ = az.throwing.double¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if (az.throwing.double¢(¢) == 0)
        throw new Exception("Cannot evaluate division by zero");
      $ /= az.throwing.double¢(¢);
    }
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
    }
    return $;
  }

  @Override int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = 0;
    try{
      if(type.of(first(xs)) == Certain.DOUBLE || type.of(first(xs)) == Certain.LONG)
        throw new NumberFormatException();
    $ = az.throwing.int¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if(type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
        throw new NumberFormatException();
      if (az.throwing.int¢(¢) == 0)
        throw new Exception("Cannot evaluate division by zero");
      $ /= az.throwing.int¢(¢);
    }
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
    }
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = 0;
    try{
    if(type.of(first(xs)) == Certain.DOUBLE )
        throw new NumberFormatException();
    $ = az.throwing.long¢(first(xs));
    for (final Expression ¢ : rest(xs)) {
      if(type.of(¢) == Certain.DOUBLE)
        throw new NumberFormatException();
      if (az.throwing.long¢(¢) == 0)
        throw new Exception("Cannot evaluate division by zero");
      $ /= az.throwing.long¢(¢);
    }
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
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

package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Evaluate the multiplication of numbers according to the following rules :
 * </br>
 * </br>
 * <code>
 * int * int --> int <br/>
 * double * double --> double <br/>
 * long * long --> long <br/>
 * int * double --> double <br/>
 * int * long --> long <br/>
 * long * double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixMultiplicationEvaluate extends $EvaluateInfixExpression {
  @Override double evaluateDouble(final List<Expression> xs) {
    double $ = 1;
    try{
    for (final Expression ¢ : xs)
      $ *= az.throwing.double¢(¢);
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
    }
    return $;
  }

  @Override int evaluateInt(final List<Expression> xs) {
    int $ = 1;
    try{
    for (final Expression ¢ : xs)
      $ *= az.throwing.int¢(¢);
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
    }
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs){
    long $ = 1;
    try{
    for (final Expression ¢ : xs)
      $ *= az.throwing.long¢(¢);
    }
    catch(NumberFormatException e){
      monitor.logEvaluationError(this, e);
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

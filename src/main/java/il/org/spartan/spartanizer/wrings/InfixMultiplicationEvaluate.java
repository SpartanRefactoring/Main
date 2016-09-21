package il.org.spartan.spartanizer.wrings;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.*;

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
  @Override double evaluateDouble(final List<Expression> xs) throws Exception {
    double $ = 1;
    for (final Expression ¢ : xs) {
      $ *= az.throwing.double¢(¢);
    }
    return $;
  }

  @Override int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = 1;
    for (final Expression ¢ : xs) {
      $ *= az.throwing.int¢(¢);
    }
    return ($);
  }

  @Override long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = 1;
    for (final Expression ¢ : xs) {
      $ *= az.throwing.long¢(¢);
    }
    return ($); 
  }

  @Override String operation() {
    return "multiplication";
  }

  @Override Operator operator() {
    return TIMES;
  }


}

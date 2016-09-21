package il.org.spartan.spartanizer.wrings;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.spartanizer.ast.*;

/** Evaluate the addition of numbers according to the following rules <br/>
 * <br/>
 * <code>
 * int + int --> int <br/>
 * double + double --> double <br/>
 * long + long --> long <br/>
 * int + double --> double <br/>
 * int + long --> long <br/>
 * long + double --> double <br/>
 * </code>
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixAdditionEvaluate extends $EvaluateInfixExpression {
  @Override double evaluateDouble(final List<Expression> xs) throws Exception {
    double $ = 0;
    for (final Expression ¢ : xs)
      $ += az.throwing.double¢(¢);
    return $;
  }

  @Override int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = 0;
    for (final Expression ¢ : xs)
      $ += az.throwing.int¢(¢);
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = 0;
    for (final Expression ¢ : xs)
      $ += az.throwing.long¢(¢);
    return $;
  }

  @Override String operation() {
    return "multiplication";
  }

  @Override Operator operator() {
    return PLUS2;
  }
}

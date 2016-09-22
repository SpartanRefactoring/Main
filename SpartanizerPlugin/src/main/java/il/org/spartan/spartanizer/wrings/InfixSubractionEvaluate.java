package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.spartanizer.ast.*;

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
public final class InfixSubractionEvaluate extends $EvaluateInfixExpression {
  @Override double evaluateDouble(final List<Expression> xs) throws Exception {
    double $ = az.throwing.double¢(first(xs));
    for (final Expression ¢ : rest(xs))
      $ -= az.throwing.double¢(¢);
    return $;
  }

  @Override int evaluateInt(final List<Expression> xs) throws Exception {
    int $ = az.throwing.int¢(first(xs));
    for (final Expression ¢ : rest(xs))
      $ -= az.throwing.int¢(¢);
    return $;
  }

  @Override long evaluateLong(final List<Expression> xs) throws Exception {
    long $ = az.throwing.long¢(first(xs));
    for (final Expression ¢ : rest(xs))
      $ -= az.throwing.long¢(¢);
    return $;
  }

  @Override String operation() {
    return "subtraction";
  }

  @Override Operator operator() {
    return MINUS2;
  }
}

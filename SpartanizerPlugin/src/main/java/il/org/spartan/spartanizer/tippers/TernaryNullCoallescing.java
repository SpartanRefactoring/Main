package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace X != null ? X : Y with X ?? Y <br>
 * replace X == null ? Y : X with X ?? Y <br>
 * replace null == X ? Y : X with X ?? Y <br>
 * replace null != X ? X : Y with X ?? Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class TernaryNullCoallescing extends NanoPatternTipper<ConditionalExpression> implements Kind.CommnoFactoring {
  @Override public boolean prerequisite(ConditionalExpression x) {
    if (!iz.comparison(az.infixExpression(step.expression(x))))
      return false;
    InfixExpression condition = az.comparison((step.expression(x)));
    Expression left = step.left(condition);
    Expression right = step.right(condition);
    return step.operator(condition) == EQUALS ? prerequisite(left, right, step.elze(x))
        : step.operator(condition) == NOT_EQUALS && prerequisite(left,right,step.then(x));
  }

  private static boolean prerequisite(Expression left, Expression right, Expression elze) {
    if ((!iz.nullLiteral(left) && iz.nullLiteral(right) && wizard.same(left, elze))
        || (iz.nullLiteral(left) && !iz.nullLiteral(right) && wizard.same(right, elze)))
      Counter.count(TernaryNullCoallescing.class);
    return true;
  }

  @Override public String description(@SuppressWarnings("unused") ConditionalExpression __) {
    return "replace null coallescing ternary with ??";
  }
}

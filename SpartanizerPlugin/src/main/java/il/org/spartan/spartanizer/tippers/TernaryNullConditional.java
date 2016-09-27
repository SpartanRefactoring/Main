package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace X == null ? null : X.Y with X?.Y <br>
 * replace X != null ? X.Y : null with X?.Y <br>
 * replace null == X ? null : X.Y with X?.Y <br>
 * replace null != X ? X.Y : null with X?.Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class TernaryNullConditional extends NanoPatternTipper<ConditionalExpression> implements Kind.CommnoFactoring {
  private static boolean prerequisite(final Expression left, final Expression right, final Expression elze) {
    if (!iz.nullLiteral(left) && iz.nullLiteral(right) && wizard.same(left, elze))
      Counter.count(TernaryNullConditional.class);
    if (iz.nullLiteral(left) && !iz.nullLiteral(right) && wizard.same(right, elze))
      Counter.count(TernaryNullConditional.class);
    return true;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "replace null coallescing ternary with ??";
  }

  @Override public boolean prerequisite(final ConditionalExpression x) {
    if (!iz.comparison(az.infixExpression(step.expression(x))))
      return false;
    final InfixExpression condition = az.comparison(step.expression(x));
    final Expression left = step.left(condition);
    final Expression right = step.right(condition);
    step.then(x);
    step.elze(x);
    //
    //
    //// MemberRef m = ;
    return step.operator(condition) == EQUALS ? prerequisite(left, right, step.elze(x))
        : step.operator(condition) == NOT_EQUALS && prerequisite(left, right, step.then(x));
  }
}

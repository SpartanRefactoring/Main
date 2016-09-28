package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace X != null ? X : Y with X ?? Y <br>
 * replace X == null ? Y : X with X ?? Y <br>
 * replace null == X ? Y : X with X ?? Y <br>
 * replace null != X ? X : Y with X ?? Y <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class TernaryNullCoallescing extends NanoPatternTipper<ConditionalExpression> implements TipperCategory.CommnoFactoring {
  private static boolean prerequisite(final Expression left, final Expression right, final Expression elze) {
    if (!iz.nullLiteral(left) && iz.nullLiteral(right) && wizard.same(left, elze)
        || iz.nullLiteral(left) && !iz.nullLiteral(right) && wizard.same(right, elze))
      Counter.count(TernaryNullCoallescing.class);
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
    return step.operator(condition) == EQUALS ? prerequisite(left, right, step.elze(x))
        : step.operator(condition) == NOT_EQUALS && prerequisite(left, right, step.then(x));
  }

  @Override public Tip tip(final ConditionalExpression x) {
    return new Tip(description(x), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(x, into.e("If.True(" + az.comparison(step.expression(x)) + ").then(" + step.then(x) + ").elze(" + step.elze(x) + ")"), g);
      }
    };
  }
}

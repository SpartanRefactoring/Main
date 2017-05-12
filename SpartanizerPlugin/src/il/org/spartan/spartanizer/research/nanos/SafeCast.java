package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** A cast which is inside an IfStatement body which checks the casted
 * expression is actually of the casted to __
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-23 */
public class SafeCast extends NanoPatternTipper<CastExpression> {
  private static final long serialVersionUID = -0x64DE8550C12A371EL;

  @Override public boolean canTip(final CastExpression ¢) {
    return (expression(yieldAncestors.untilClass(IfStatement.class).from(¢)) + "").contains(expression(¢) + " instanceof " + type(¢));
  }

  @Override public Tip pattern(final CastExpression ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, make.ast("safeCast(" + expression(¢) + ")"), g);
      }
    };
  }
}

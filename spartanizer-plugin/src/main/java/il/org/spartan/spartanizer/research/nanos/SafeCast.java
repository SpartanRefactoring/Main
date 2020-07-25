package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

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

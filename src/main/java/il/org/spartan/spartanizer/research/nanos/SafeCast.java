package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** A cast which is inside an IfStatement body which checks the casted
 * expression is actually of the casted to type
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-23 */
public class SafeCast extends NanoPatternTipper<CastExpression> {
  @Override public boolean canTip(final CastExpression ¢) {
    String string = expression(yieldAncestors.untilClass(IfStatement.class).from(¢)) + "";
    return string.contains(expression(¢) + " instanceof " + type(¢));
  }

  @Override public Tip pattern(final CastExpression ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, wizard.ast("safeCast(" + expression(¢) + ")"), g);
      }
    };
  }
}

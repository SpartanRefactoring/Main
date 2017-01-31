package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <code>
 * if (x)
 *   if (a)
 *     f();
 * </code>
 *
 * into
 *
 * <code>
 * if (x &amp;&amp; a)
 *   f();
 * </code>
 *
 * @author Yossi Gil
 * @since 2015-09-01 */
public final class IfThenIfThenNoElseNoElse extends EagerTipper<IfStatement>//
    implements TipperCategory.CommnonFactoring {
  static void collapse(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    final IfStatement then = az.ifStatement(extract.singleThen(s));
    r.replace(s.getExpression(), subject.pair(s.getExpression(), then.getExpression()).to(CONDITIONAL_AND), g);
    r.replace(then, copy.of(then(then)), g);
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Merge conditionals of nested if staement";
  }

  @Override public Tip tip(final IfStatement ¢) {
    return tip(¢, null);
  }

  @Override public Tip tip(final IfStatement $, final ExclusionManager exclude) {
    if (!iz.vacuousElse($))
      return null;
    final IfStatement then = az.ifStatement(extract.singleThen($));
    if (then == null || !iz.vacuousElse(then))
      return null;
    if (exclude != null)
      exclude.exclude(then);
    return new Tip(description($), $, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        collapse(Tippers.blockIfNeeded($, r, g), r, g);
      }
    };
  }
}

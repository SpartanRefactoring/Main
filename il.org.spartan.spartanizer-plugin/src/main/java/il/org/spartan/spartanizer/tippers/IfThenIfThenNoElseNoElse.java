package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code
 * if (x)
 *   if (a)
 *     f();
 * } into {@code
 * if (x && a)
 *   f();
 * }
 * @author Yossi Gil
 * @since 2015-09-01 */
public final class IfThenIfThenNoElseNoElse extends EagerTipper<IfStatement>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x23F015E81A561C0DL;

  static void collapse(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    final IfStatement then = az.ifStatement(extract.singleThen(s));
    r.replace(s.getExpression(), subject.pair(s.getExpression(), then.getExpression()).to(CONDITIONAL_AND), g);
    r.replace(then, copy.of(then(then)), g);
  }
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Merge nested if staements, &&ing their conditions";
  }
  @Override public Tip tip(final IfStatement $) {
    if (!iz.vacuousElse($))
      return null;
    final IfStatement ret = az.ifStatement(extract.singleThen($));
    return ret == null || !iz.vacuousElse(ret) ? null : new Tip(description($), getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        collapse(misc.blockIfNeeded($, r, g), r, g);
      }
    }.spanning(ret);
  }
}

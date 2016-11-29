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
 * <pre>
 * if (x)
 *   if (a)
 *     f();
 * </pre>
 *
 * into
 *
 * <pre>
 * if (x &amp;&amp; a)
 *   f();
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-09-01 */
public final class IfThenIfThenNoElseNoElse extends EagerTipper<IfStatement> implements TipperCategory.CommnoFactoring {
  static void collapse(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    final IfStatement then = az.ifStatement(extract.singleThen(s));
    r.replace(s.getExpression(), subject.pair(s.getExpression(), then.getExpression()).to(CONDITIONAL_AND), g);
    r.replace(then, duplicate.of(then(then)), g);
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Merge conditionals of nested if staement";
  }

  @Override public Tip tip(final IfStatement ¢) {
    return tip(¢, null);
  }

  @Override public Tip tip(final IfStatement s, final ExclusionManager exclude) {
    if (!iz.vacuousElse(s))
      return null;
    final IfStatement then = az.ifStatement(extract.singleThen(s));
    if (then == null || !iz.vacuousElse(then))
      return null;
    if (exclude != null)
      exclude.exclude(then);
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        collapse(Tippers.blockIfNeeded(s, r, g), r, g);
      }
    };
  }
}

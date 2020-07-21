package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** A {@link Tipper} to eliminate degenerate if sideEffects such as {@code
 * if (x)
 *   ;
 * else
 *   ;
 * }
 * @author Yossi Gil
 * @since 2015-08-26 */
public final class IfEmptyThenEmptyElse extends CarefulTipper<IfStatement>//
    implements Category.Transformation.Remorph {
  private static final long serialVersionUID = 0x5956B839ADCAD9C5L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove 'if' statement with vacous 'then' and 'else' parts";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return iz.block(¢.getParent()) && iz.vacuousThen(¢) && iz.vacuousElse(¢);
  }
  @Override public Tip tip(final IfStatement s) {
    return new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(az.block(s.getParent()), Block.STATEMENTS_PROPERTY);
        for (final Statement x : compute.decompose(s.getExpression()))
          l.insertBefore(copy.of(x), s, g);
        l.remove(s, g);
      }
    };
  }
}

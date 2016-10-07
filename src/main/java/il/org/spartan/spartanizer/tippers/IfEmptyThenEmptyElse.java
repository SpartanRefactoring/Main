package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to eliminate degenerate if sideEffects such as
 *
 * <pre>
 * if (x)
 *   ;
 * else
 *   ;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-26 */
public final class IfEmptyThenEmptyElse extends CarefulTipper<IfStatement> implements TipperCategory.InVain {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove 'if' statement with vacous 'then' and 'else' parts";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && iz.vacuousThen(¢) && iz.vacuousElse(¢);
  }

  @Override public Tip tip(final IfStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        s.setElseStatement(null);
        r.remove(s, g);
      }
    };
  }
}

package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** A {@link Tipper} to eliminate degenerate if sideEffects such as {@code
 * if (x)
 *   ;
 * else
 *   ;
 * }
 * @author Yossi Gil
 * @since 2015-08-26 */
public final class IfEmptyThenEmptyElse extends CarefulTipper<IfStatement>//
    implements TipperCategory.NOP {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove 'if' statement with vacous 'then' and 'else' parts";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return iz.vacuousThen(¢) && iz.vacuousElse(¢);
  }

  @Override @NotNull public Tip tip(@NotNull final IfStatement s) {
    return new Tip(description(s), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        s.setElseStatement(null);
        r.remove(s, g);
      }
    };
  }
}

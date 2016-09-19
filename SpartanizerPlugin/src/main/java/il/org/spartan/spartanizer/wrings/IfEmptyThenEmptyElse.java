package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** A {@link Wring} to eliminate degenerate if statements such as
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
public final class IfEmptyThenEmptyElse extends CarefulWring<IfStatement> implements Kind.NOP {
  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && iz.vacuousThen(¢) && iz.vacuousElse(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove 'if' statement with vacous 'then' and 'else' parts";
  }

  @Override public Suggestion suggest(final IfStatement s) {
    return new Suggestion(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        s.setElseStatement(null);
        r.remove(s, g);
      }
    };
  }
}

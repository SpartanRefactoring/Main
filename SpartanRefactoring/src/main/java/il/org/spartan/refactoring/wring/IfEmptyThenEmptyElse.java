package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to eliminate degenerate if statements such as <code>if (x) ;
 * else ;</code>
 *
 * @author Yossi Gil
 * @since 2015-08-26
 */
public final class IfEmptyThenEmptyElse extends Wring<IfStatement> implements Kind.Simplify {
  @Override final Rewrite make(final IfStatement s) {
    return new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        s.setElseStatement(null);
        r.remove(s, g);
      }
    };
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && Is.vacuousThen(s) && Is.vacuousElse(s);
  }
  @Override String description(final IfStatement s) {
    return "Remove if(" + s.getExpression() + ") with vacous 'then' and 'else' parts";
  }
}
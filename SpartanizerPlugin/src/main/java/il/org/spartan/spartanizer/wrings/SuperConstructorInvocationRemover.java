package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Removes <code>super()</code> calls which take no arguments, as typically
 * created by Eclipse's template for constructors.
 * @author Daniel Mittelman
 * @since 2015-08-26 */
public final class SuperConstructorInvocationRemover extends CarefulWring<SuperConstructorInvocation> implements Kind.SyntacticBaggage {
  @Override public String description(final SuperConstructorInvocation ¢) {
    return "Remove vacuous 'super()' invocation";
  }

  @Override public boolean prerequisite(final SuperConstructorInvocation ¢) {
    return ¢.getExpression() == null && ¢.arguments().isEmpty();
  }

  @Override public Suggestion suggest(final SuperConstructorInvocation i) {
    return new Suggestion(description(i), i) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }
}

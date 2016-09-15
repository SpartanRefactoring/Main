package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** A wring to remove
 *
 * <pre>
 * super()
 * </pre>
 *
 * calls which take no arguments, as typically created by Eclipse's template for
 * constructors.
 * @author Daniel Mittelman
 * @since 2015-08-26 */
public final class SuperConstructorInvocationRemover extends Wring<SuperConstructorInvocation> implements Kind.SyntacticBaggage {
  @Override public boolean claims(final SuperConstructorInvocation ¢) {
    return ¢.getExpression() == null && ¢.arguments().isEmpty();
  }

  @Override public String description(final SuperConstructorInvocation ¢) {
    return "Remove vacuous 'super()' invocation in " + ¢;
  }

  @Override public Rewrite wring(final SuperConstructorInvocation i) {
    return new Rewrite(description(i), i) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }
}

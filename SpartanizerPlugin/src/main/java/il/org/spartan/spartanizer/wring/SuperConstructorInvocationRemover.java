package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

/** A wring to remove
 *
 * <pre>
 * super()
 * </pre>
 *
 * calls which take no arguments, as typically created by Eclipse's template for
 * constructors.
 * @author Daniel Mittelman?
 * @since 2015-08-26 */
public final class SuperConstructorInvocationRemover extends Wring<SuperConstructorInvocation> implements Kind.SyntacticBaggage {
  @Override String description(@SuppressWarnings("unused") final SuperConstructorInvocation __) {
    return "Remove empty 'super()' invocation";
  }

  @Override Rewrite make(final SuperConstructorInvocation i) {
    return new Rewrite(description(i), i) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }

  @Override boolean scopeIncludes(final SuperConstructorInvocation i) {
    return i.arguments().isEmpty();
  }
}

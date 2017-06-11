package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Removes {@code super()} calls which take no arguments, as typically created
 * by Eclipse's template for constructors.
 * @author Daniel Mittelman
 * @since 2015-08-26 */
public final class SuperConstructorInvocationRemover extends CarefulTipper<SuperConstructorInvocation>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x22E9BC9648E5BBCEL;

  @Override public String description(@SuppressWarnings("unused") final SuperConstructorInvocation __) {
    return "Remove vacuous 'super()' invocation";
  }
  @Override public boolean prerequisite(final SuperConstructorInvocation ¢) {
    return ¢.getExpression() == null && ¢.arguments().isEmpty();
  }
  @Override public Tip tip(final SuperConstructorInvocation i) {
    return new Tip(description(i), getClass(), i) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(i, g);
      }
    };
  }
}

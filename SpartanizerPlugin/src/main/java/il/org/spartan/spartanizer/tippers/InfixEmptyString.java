package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to remove the empty String "" in String conversion
 * expression like <code> "" + X </code> but ONLY if X is a String.
 * @author Matteo Orru' <code><matt.orru [at] gmail.com></code>
 * @since 2016-08-14 */
public final class InfixEmptyString extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  @Override public String description() {
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove \"\" from \"\" + X if X is a String";
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return Tippers.eliminateLiteral(¢, true);
  }
  @Override public TipperGroup tipperGroup() {
    return null;
  }
}

package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.plugin.PluginPreferencesResources.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** A {@link Wring} to remove the empty String "" in String conversion
 * expression like <code> "" + X </code> but ONLY if X is a String.
 * @author Matteo Orru' <code><matt.orru [at] gmail.com></code>
 * @since 2016-08-14 */
public class InfixEmptyString extends ReplaceCurrentNode<InfixExpression> {
  @Override public String description() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public WringGroup wringGroup() {
    return null;
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove \"\" from \"\" + X if X is a String";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return Wrings.eliminateLiteral(e, true);
  }
}

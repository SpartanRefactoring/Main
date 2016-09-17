package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** A {@link Wring} to remove the empty String "" in String conversion
 * expression like <code> "" + X </code> but ONLY if X is a String.
 * @author Matteo Orru' <code><matt.orru [at] gmail.com></code>
 * @since 2016-08-14 */
public class InfixEmptyString extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  @Override public String description() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove \"\" from \"\" + X if X is a String";
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return Wrings.eliminateLiteral(¢, true);
  }

  @Override public WringGroup wringGroup() {
    return null;
  }
}

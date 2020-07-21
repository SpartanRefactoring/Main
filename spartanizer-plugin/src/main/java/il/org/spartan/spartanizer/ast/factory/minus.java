package il.org.spartan.spartanizer.ast.factory;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** takes care of of multiplicative terms with minus symbol in them.
 * <p>
 * An empty {@code enum} for fluent programming. The name should say it all: The
 * name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public enum minus {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Remove the last statement residing under a given {@link Statement}, if ¢
   * is empty or has only one statement return empty statement.
   * @param ¢ JD {@code null if not such sideEffects exists.
   * @return Given {@link Statement} without the last inner statement, if ¢ is
   *         empty or has only one statement return empty statement. */
  public static Statement lastStatement(final Statement $) {
    if (!iz.block($))
      return atomic.emptyStatement($);
    final List<Statement> ss = step.statements(az.block($));
    if (!ss.isEmpty())
      ss.remove(ss.size() - 1);
    return $;
  }
}

package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * switch (x) {
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * </pre>
 *
 * .
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  // TODO: yuval - check the below method and finish the other methods
  @Override public Statement replacement(@SuppressWarnings("unused") final SwitchStatement __) {
    // return instanceOf(EmptyStatement.class, ¢);
    return (Statement) wizard.ast(";");
  }

  @Override public boolean prerequisite(@SuppressWarnings("unused") final SwitchStatement __) {
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement";
  }
}

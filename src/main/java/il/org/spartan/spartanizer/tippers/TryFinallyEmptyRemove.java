package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts {@code try { a;} catch(Exception e) { b;} finally {} } into
 * {@code try { a;} catch(Exception e) { b;} }
 * @author kobybs
 * @author Dor Ma'ayan
 * @since 16-11-2016 */
public final class TryFinallyEmptyRemove extends ReplaceCurrentNode<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x2D910A689FB05A35L;

  @Override public boolean prerequisite(final TryStatement ¢) {
    return !statements(body(¢)).isEmpty() && ¢.getFinally() != null && statements(¢.getFinally()).isEmpty() && !¢.catchClauses().isEmpty();
  }

  @Override public ASTNode replacement(final TryStatement ¢) {
    final TryStatement $ = copy.of(¢);
    $.setFinally(null);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final TryStatement __) {
    return "Prune empty finally block";
  }
}

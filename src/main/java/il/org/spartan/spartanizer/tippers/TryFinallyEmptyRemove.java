package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <pre>
 * try { a;} catch(Exception e) { b;} finally {}
 * </pre>
 *
 * into
 *
 * <pre>
 * try { a;} catch(Exception e) { b;}
 * </pre>
 *
 * @author kobybs
 * @author Dor Ma'ayan
 * @since 16-11-2016 */
public final class TryFinallyEmptyRemove extends ReplaceCurrentNode<TryStatement> implements TipperCategory.Unite {
  @Override public boolean prerequisite(final TryStatement ¢) {
    return !¢.getBody().statements().isEmpty() && //
        ¢.getFinally() != null && ¢.getFinally().statements().isEmpty() && //
        !¢.catchClauses().isEmpty();
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

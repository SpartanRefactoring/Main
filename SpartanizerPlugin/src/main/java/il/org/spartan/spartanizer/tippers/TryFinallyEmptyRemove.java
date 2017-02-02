package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** converts {@code try { a;} catch(Exception e) { b;} finally {} } into
 * {@code try { a;} catch(Exception e) { b;} }
 * @author kobybs
 * @author Dor Ma'ayan
 * @since 16-11-2016 */
public final class TryFinallyEmptyRemove extends ReplaceCurrentNode<TryStatement>//
    implements TipperCategory.SyntacticBaggage {
  @Override public boolean prerequisite(@NotNull final TryStatement ¢) {
    return !statements(body(¢)).isEmpty() && ¢.getFinally() != null && statements(¢.getFinally()).isEmpty() && !¢.catchClauses().isEmpty();
  }

  @Override @Nullable public ASTNode replacement(final TryStatement ¢) {
    final TryStatement $ = copy.of(¢);
    $.setFinally(null);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final TryStatement __) {
    return "Prune empty finally block";
  }
}

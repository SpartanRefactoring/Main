package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert pattern <code>try {s} [ finally { <i>empty</i> }]</code>,
 * <code>s</code> not empty, to <code>{s}</code>.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-19 */
public final class TryBodyNotEmptyNoCatchesNoFinallyRemove extends ReplaceCurrentNode<TryStatement> implements TipperCategory.Collapse {
  @Override public boolean prerequisite(final TryStatement ¢) {
    return !¢.getBody().statements().isEmpty() && ¢.resources().isEmpty() && ¢.catchClauses().isEmpty()
        && (¢.getFinally() == null || ¢.getFinally().statements().isEmpty());
  }

  @Override public ASTNode replacement(final TryStatement ¢) {
    return ¢.getBody();
  }

  @Override public String description(final TryStatement ¢) {
    return "Remove the do-nothing try wrap around block " + trivia.gist(¢.getBody());
  }
}

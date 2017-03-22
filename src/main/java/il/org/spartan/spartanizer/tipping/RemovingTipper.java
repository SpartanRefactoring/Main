package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.engine.*;

/** An abstract tipping strategy that removes a node if
 * {@link RemovingTipper#prerequisite(ASTNode)} holds.
 * @param <N> Type of node to remove
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-15 */
public abstract class RemovingTipper<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 1791362595323626807L;

  @NotNull @Override public final Tip tip(@NotNull final N n) {
    return new Tip(description(n), n, myClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.remove(n, g);
      }
    };
  }

  @Override protected abstract boolean prerequisite(N n);
}

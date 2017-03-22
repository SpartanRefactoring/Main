package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Replace current node strategy
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class ReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 5806282917111501608L;

  @Nullable public abstract ASTNode replacement(N n);

  @Override public final Tip tip(@NotNull final N n) {
    assert prerequisite(n) : fault.dump() + "\n n = " + n + fault.done();
    @Nullable final ASTNode $ = replacement(n);
    @NotNull @SuppressWarnings("unchecked") final Class<? extends Tipper<N>> class1 = (Class<? extends Tipper<N>>) getClass();
    return $ == null ? null : new Tip(description(n), n, class1) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }
}

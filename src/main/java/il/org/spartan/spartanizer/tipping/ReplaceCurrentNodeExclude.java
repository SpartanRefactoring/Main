package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Similar to {@link ReplaceCurrentNode}, but with an {@link ExclusionManager}
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
public abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends ReplaceCurrentNode<N> {
  @Override public final Tip tip(@NotNull final N n, final ExclusionManager m) {
    assert prerequisite(n) : fault.dump() + "\n n = " + n + "\n m = " + m + fault.done();
    final ASTNode $ = replacement(n, m);
    return $ == null ? null : new Tip(description(n), n, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }

  @Override protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }

  @Nullable protected abstract ASTNode replacement(N n, ExclusionManager m);
}

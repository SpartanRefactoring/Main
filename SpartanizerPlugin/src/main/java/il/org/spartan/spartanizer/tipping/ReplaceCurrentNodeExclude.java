package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Similar to {@link ReplaceCurrentNode}, but with an {@link ExclusionManager}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends ReplaceCurrentNode<N> {
  private static final long serialVersionUID = 8188241616526954088L;

  @Override public final Tip tip(@NotNull final N n, final ExclusionManager m) {
    assert prerequisite(n) : fault.dump() + "\n n = " + n + "\n m = " + m + fault.done();
    @Nullable final ASTNode $ = replacement(n, m);
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

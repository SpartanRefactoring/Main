package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Similar to {@link ReplaceCurrentNode}, but with an
 * {@link ExclusionManager} */
public abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends ReplaceCurrentNode<N> {
  @Override public final Tip tip(final N n, final ExclusionManager m) {
    assert prerequisite(n) : fault.dump() + "\n n = " + n + "\n m = " + m + fault.done();
    final ASTNode $ = replacement(n, m);
    return $ == null ? null : new Tip(description(n), n, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }
  @Override protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
  protected abstract ASTNode replacement(N n, final ExclusionManager m);
}
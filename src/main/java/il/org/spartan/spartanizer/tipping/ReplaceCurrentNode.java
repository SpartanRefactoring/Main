package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Replace current node strategy
 * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class ReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 5806282917111501608L;

  public abstract ASTNode replacement(N n);

  @Override public final Tip tip(final N n) {
    assert prerequisite(n) : fault.dump() + "\n n = " + n + fault.done();
    final ASTNode $ = replacement(n);
    return $ == null ? null : new Tip(description(n), n, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }
}

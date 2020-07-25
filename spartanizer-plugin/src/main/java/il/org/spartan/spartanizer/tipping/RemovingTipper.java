package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

/** An abstract tipping strategy that removes a node if
 * {@link RemovingTipper#prerequisite(ASTNode)} holds.
 * @param <N> Type of node to remove
 * @author Yossi Gil
 * @since 2017-01-15 */
public abstract class RemovingTipper<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 0x18DC32CA162FC537L;

  @Override @SuppressWarnings("unchecked") public final Tip tip(final N n) {
    return new Tip(description(n), (Class<? extends RemovingTipper<N>>) getClass(), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(n, g);
      }
    };
  }
  @Override protected abstract boolean prerequisite(N n);
}

package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

/** An abstract tipping strategy that removes a node if
 * {@link RemovingTipper#prerequisite(ASTNode)} holds.
 * @param <N> Type of node to remove
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-15 */
public abstract class RemovingTipper<N extends ASTNode> extends $CarefulTipper<N> {
  @Override public final Tip tip(final N n) {
    return new Tip(description(n), n, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(n, g);
      }
    };
  }

  @Override protected abstract boolean prerequisite(N n);
}

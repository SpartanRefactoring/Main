package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Similar to {@link ReplaceCurrentNode}, but with an {@link ExclusionManager}
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends ReplaceCurrentNode<N> {
  private static final long serialVersionUID = 0x71A27A5DF828CE68L;

  @Override public final Tip tip(final N n) {
    assert prerequisite(n) : fault.dump() + "\n n = " + n + "\n" + fault.done();
    final ASTNode $ = replacement(n);
    return $ == null ? null : new Tip(description(n), myClass(), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }

  @Override protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }

  @Override public abstract ASTNode replacement(N n);
}

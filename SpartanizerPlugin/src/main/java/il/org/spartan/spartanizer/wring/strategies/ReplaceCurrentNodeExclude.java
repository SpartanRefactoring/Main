package il.org.spartan.spartanizer.wring.strategies;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;

/** Similar to {@link ReplaceCurrentNode}, but with an
 * {@link ExclusionManager} */
public abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends Wring<N> {
  @Override public boolean claims(final N ¢) {
    return replacement(¢, new ExclusionManager()) != null;
  }

  @Override public final Rewrite make(final N n, final ExclusionManager m) {
    return cantWring(n) ? null : new Rewrite(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, replacement(n, m), g);
      }
    };
  }

  protected abstract ASTNode replacement(N n, final ExclusionManager m);
}
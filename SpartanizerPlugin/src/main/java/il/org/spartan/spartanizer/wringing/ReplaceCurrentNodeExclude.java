package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** Similar to {@link ReplaceCurrentNode}, but with an
 * {@link ExclusionManager} */
public abstract class ReplaceCurrentNodeExclude<N extends ASTNode> extends ReplaceCurrentNode<N> {
  @Override public final Suggestion suggest(final N n, final ExclusionManager m) {
    assert prerequisite(n) : dump() + "\n n = " + n + "\n m = " + m + endDump();
    final ASTNode $ = replacement(n, m);
    return $ == null ? null : new Suggestion(description(n), n) {
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
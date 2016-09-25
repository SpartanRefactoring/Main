package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;

/** Replace current node strategy
 * @author Yossi Gil
 * @year 2016 */
public abstract class ReplaceCurrentNode<N extends ASTNode> extends CarefulTipper<N> {
  public abstract ASTNode replacement(N n);

  @Override public final Tip suggest(final N n) {
    assert prerequisite(n) : LoggingManner.dump() + "\n n = " + n + LoggingManner.endDump();
    final ASTNode $ = replacement(n);
    return $ == null ? null : new Tip(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, $, g);
      }
    };
  }
}
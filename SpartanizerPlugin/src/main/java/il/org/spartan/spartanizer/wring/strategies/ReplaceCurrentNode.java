package il.org.spartan.spartanizer.wring.strategies;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

public abstract class ReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
  @Override public final Rewrite make(final N n) {
    return !eligible(n) ? null : new Rewrite(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, replacement(n), g);
      }
    };
  }

  public abstract ASTNode replacement(N n);

  @Override public boolean scopeIncludes(final N n) {
    return replacement(n) != null;
  }
}
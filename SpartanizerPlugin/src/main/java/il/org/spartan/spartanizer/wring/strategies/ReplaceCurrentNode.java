package il.org.spartan.spartanizer.wring.strategies;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

public abstract class ReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
  @Override public boolean wantsToSuggestButPerhapsCant(final N ¢) {
    return replacement(¢) != null;
  }

  public abstract ASTNode replacement(N n);

  @Override public final Rewrite suggest(final N n) {
    return cantSuggest(n) ? null : new Rewrite(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, replacement(n), g);
      }
    };
  }
}
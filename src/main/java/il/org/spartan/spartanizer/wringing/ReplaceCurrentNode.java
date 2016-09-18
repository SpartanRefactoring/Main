package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

public abstract class ReplaceCurrentNode<N extends ASTNode> extends Wring<N> {
  @Override public boolean demandsToSuggestButPerhapsCant(final N ¢) {
    return replacement(¢) != null;
  }

  public abstract ASTNode replacement(N n);

  @Override public final Suggestion suggest(final N n) {
    return cantSuggest(n) ? null : new Suggestion(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, replacement(n), g);
      }
    };
  }
}
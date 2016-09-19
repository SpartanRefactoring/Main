package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;

/** Replace current node strategy
 * @author Yossi Gil
 * @year 2016 */
public abstract class ReplaceCurrentNodeIfPreresuisite<N extends ASTNode> extends Wring<N> {
  @Override public final boolean demandsToSuggestButPerhapsCant(N ¢) {
    return canSuggest(¢);
  }

  @Override public abstract boolean canSuggest(N n);

  @Override public final Suggestion suggest(final N n) {
    return cantSuggest(n) ? null : new Suggestion(description(n), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, replacement(n), g);
      }
    };
  }

  public abstract ASTNode replacement(N n);
}
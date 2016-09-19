package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;

/** A {@link Wring} in which only the suggestion has to be implemented.
 * @author Yossi Gil
 * @year 2016 */
public abstract class EagerWring<N extends ASTNode> extends Wring<N> {
  @Override public String description() {
    return getClass().getSimpleName();
  }

  @Override public final boolean canSuggest(N n) {
    return this.suggest(n) != null;
  }

  @Override public final boolean demandsToSuggestButPerhapsCant(N n) {
    return canSuggest(n);
  }
}

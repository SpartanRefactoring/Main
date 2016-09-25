package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;

/** A {@link Tipper} in which only the suggestion has to be implemented.
 * @author Yossi Gil
 * @year 2016 */
public abstract class EagerWring<N extends ASTNode> extends Tipper<N> {
  @Override public final boolean canSuggest(final N ¢) {
    return this.suggest(¢) != null;
  }

  protected final boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

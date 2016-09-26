package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;

/** A {@link Tipper} in which only the suggestion has to be implemented.
 * @author Yossi Gil
 * @year 2016 */
public abstract class EagerTipper<N extends ASTNode> extends Tipper<N> {
  @Override public final boolean canTip(final N ¢) {
    try {
      return this.tip(¢) != null;
    } catch (TipperException e) {
      return false;
    }
  }

  protected final boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

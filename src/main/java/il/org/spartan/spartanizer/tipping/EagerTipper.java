package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;

/** A {@link Tipper} in which only the tip has to be implemented.
 * @author Yossi Gil
 * @year 2016 */
public abstract class EagerTipper<N extends ASTNode> extends Tipper<N> {
  @Override public final boolean canTip(final N ¢) {
    return this.tip(¢) != null;
  }

  /** @param __ the ASTNode being inspected.
   * @return <code><b>true</b></code> <i>iff</i> the argument holds all the
   *         conditions needed for a tip to be possible. */
  protected final boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

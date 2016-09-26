package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;

/** A {@link Tipper} in which {@link #tip(ASTNode)} is invoked only if
 * {@link #canTip(ASTNode)} returns true. However, in such cases
 * {@link #tip(ASTNode)} may still return null.
 * @author Yossi Gil
 * @year 2016 */
public abstract class CarefulTipper<N extends ASTNode> extends Tipper<N> {
  @Override public final boolean canTip(final N ¢) {
    try {
      return prerequisite(¢) && tip(¢) != null;
    } catch (TipperException e) {
      return false;
    }
  }

  protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

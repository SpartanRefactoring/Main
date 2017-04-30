package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;

/** A {@link Tipper} in which {@link #tip(ASTNode)} is invoked only if
 * {@link #check(ASTNode)} returns true. However, in such cases
 * {@link #tip(ASTNode)} may still return null.
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public abstract class CarefulTipper<N extends ASTNode> extends Tipper<N> {
  private static final long serialVersionUID = -0x10A76363F64E5AB4L;

  @Override public final boolean canTip(final N ¢) {
    return prerequisite(¢) && tip(¢) != null;
  }

  /** @param __ the ASTNode being inspected.
   * @return whether the argument holds all the conditions needed for a tip to
   *         be possible. */
  protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

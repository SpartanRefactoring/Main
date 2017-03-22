package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

/** A {@link Tipper} in which {@link #tip(ASTNode)} is invoked only if
 * {@link #check(ASTNode)} returns true. However, in such cases
 * {@link #tip(ASTNode)} may still return null.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class CarefulTipper<N extends ASTNode> extends Tipper<N> {
  private static final long serialVersionUID = -1200037106702768820L;

  @Override public final boolean canTip(@NotNull final N ¢) {
    assert ¢ != null;
    System.err.println("Can tip " + myClass());
    return prerequisite(¢) && tip(¢) != null;
  }

  /** @param __ the ASTNode being inspected.
   * @return whether the argument satisfies all the conditions needed for a tip
   *         to be possible. */
  protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

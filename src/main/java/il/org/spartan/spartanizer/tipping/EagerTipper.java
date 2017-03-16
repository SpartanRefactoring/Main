package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;

/** A {@link Tipper} in which only the tip has to be implemented.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 26, 2016 */
public abstract class EagerTipper<N extends ASTNode> extends Tipper<N> {
  private static final long serialVersionUID = -767334519852933504L;

  @Override public final boolean canTip(final N ¢) {
    return tip(¢) != null;
  }
}

package il.org.spartan.spartanizer.research;

/** @author Ori Marcovitch
 * @since 2016 */
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

public abstract class UserDefinedTipper<N extends ASTNode> extends Tipper<N> implements TipperCategory.Nanos {
  @Override public final boolean canTip(final N ¢) {
    return prerequisite(¢);
  }
  protected abstract boolean prerequisite(final N ¢);
  /** @param n
   * @param s
   * @return */
  public abstract ASTNode getMatching(ASTNode n, String s);
}
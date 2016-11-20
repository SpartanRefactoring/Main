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

  /** @param ¢ the ASTNode being inspected.
   * @return <code><b>true</b></code> <i>iff</i> the argument holds all the
   *         conditions needed for a tip to be possible. */
  protected abstract boolean prerequisite(final N ¢);

  /** @param n the ASTNode to be inspected.
   * @param s the pattern matching to be found in the ASTNode (for example $X1).
   * @return the ASTNode representing s. */
  public abstract ASTNode getMatching(ASTNode n, String s);
}
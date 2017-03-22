package il.org.spartan.spartanizer.research;

/* TODO Ori Marcovitch please add a description
 *
 * @author Ori Marcovitch
 *
 * @since 2016 */
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class UserDefinedTipper<N extends ASTNode> extends Tipper<N>//
    implements TipperCategory.Nanos {
  private static final long serialVersionUID = -4559537421155078857L;

  @Override public final boolean canTip(@Nullable final N ¢) {
    return ¢ != null && prerequisite(¢);
  }

  /** @param ¢ the ASTNode being inspected.
   * @return whether the argument holds all the conditions needed for a tip to
   *         be possible. */
  protected abstract boolean prerequisite(N ¢);

  /** @param n the ASTNode to be inspected.
   * @param s the pattern matching to be found in the ASTNode (for example $X1).
   * @return the ASTNode representing s. */
  public abstract ASTNode getMatching(ASTNode n, String s);

  public abstract ASTNode getMatching(ASTNode n);

  @NotNull public abstract String pattern();

  @NotNull public abstract String replacement();
}

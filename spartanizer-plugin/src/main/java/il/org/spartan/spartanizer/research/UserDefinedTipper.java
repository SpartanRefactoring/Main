package il.org.spartan.spartanizer.research;

/* TODO Ori Marcovitch please add a description
 *
 * @author Ori Marcovitch
 *
 * @since 2016 */
import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.tipping.categories.Category;

public abstract class UserDefinedTipper<N extends ASTNode> extends Tipper<N>//
    implements Category.Nanos {
  private static final long serialVersionUID = -0x3F46BB20880C92C9L;

  @Override public final boolean canTip(final N ¢) {
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
  public abstract String pattern();
  public abstract String replacement();
}

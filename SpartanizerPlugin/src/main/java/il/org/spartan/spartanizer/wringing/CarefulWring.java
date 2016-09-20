package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;

/** A {@link Wring} in which {@link #suggest(ASTNode)} is invoked only if
 * {@link #canSuggest(ASTNode)} returns true. However, in such cases
 * {@link #suggest(ASTNode)} may still return null.
 * @author Yossi Gil
 * @year 2016 */
public abstract class CarefulWring<N extends ASTNode> extends Wring<N> {
  @Override public final boolean canSuggest(final N ¢) {
    return prerequisite(¢) && suggest(¢) != null;
  }

  protected boolean prerequisite(@SuppressWarnings("unused") final N __) {
    return true;
  }
}

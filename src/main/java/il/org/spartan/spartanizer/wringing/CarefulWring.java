package il.org.spartan.spartanizer.wringing;

import org.eclipse.jdt.core.dom.*;

/** A {@link Wring} in which {@link #suggest(ASTNode)} is invoked only if
 * {@link #canSuggest(ASTNode)} returns true. However, in such cases
 * {@link #suggest(ASTNode)} may still return null.
 * @author Yossi Gil
 * @year 2016 */
public abstract class CarefulWring<N extends ASTNode> extends Wring<N> {
  @Override public final boolean canSuggest(N n) {
    return prerequisite(n) && suggest(n) != null;
  }

  protected abstract boolean prerequisite(N n) ;

  @Override public final boolean demandsToSuggestButPerhapsCant(N n) {
    return canSuggest(n);
  }
}

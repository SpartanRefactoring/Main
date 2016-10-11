package il.org.spartan.spartanizer.tipping;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;

/** A {@link Tipper} in which represents a NanoPattern.
 * @author Ori Marcovitch
 * @year 2016 */
public abstract class NanoPatternTipper<N extends ASTNode> extends Tipper<N> implements TipperCategory.Nanos {
  @Override public final boolean canTip(final N ¢) {
    return prerequisite(¢);
  }

  protected abstract boolean prerequisite(final N ¢);
}

package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @since 2016 */
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

public abstract class UserDefinedTipper<N extends ASTNode> extends Tipper<N> implements TipperCategory.Nanos{
  @Override public final boolean canTip(final N ¢) {
    if (prerequisite(¢))
      Counter.count(this.getClass());
    return prerequisite(¢);
  }

  protected abstract boolean prerequisite(final N ¢);
}
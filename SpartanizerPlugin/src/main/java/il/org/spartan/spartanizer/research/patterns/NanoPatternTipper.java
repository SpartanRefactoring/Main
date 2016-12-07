package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} in which represents a NanoPattern.
 * @author Ori Marcovitch
 * @year 2016 */
public abstract class NanoPatternTipper<N extends ASTNode> extends Tipper<N> implements TipperCategory.Nanos {
  protected static <N extends ASTNode> boolean anyTips(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return ns.stream().anyMatch(t -> t.canTip(n));
  }

  protected static <N extends ASTNode> UserDefinedTipper<N> firstThatTips(final Collection<UserDefinedTipper<N>> ns, final N n) {
    return ns.stream().filter(t -> t.canTip(n)).findFirst().get();
  }

  @Override public abstract Tip tip(final N ¢);
}

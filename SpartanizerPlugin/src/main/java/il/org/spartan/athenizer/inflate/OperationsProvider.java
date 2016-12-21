package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.inflate.SingleFlater.*;
import il.org.spartan.spartanizer.tipping.*;

/** A provider of matching {@link Tipper} for an {@link ASTNode}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-20 */
public abstract class OperationsProvider {
  /** @param n JD
   * @return matching {@link Tipper} */
  public abstract <N extends ASTNode> Tipper<N> getTipper(N n);
  // TODO Raviv: write ***Javadoc*** according to conventions --or
  public abstract Function<List<Operation<?>>, Operation<?>> getFunction();
}

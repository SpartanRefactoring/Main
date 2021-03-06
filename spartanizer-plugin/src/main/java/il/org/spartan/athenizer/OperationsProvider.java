package il.org.spartan.athenizer;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.athenizer.SingleFlater.Operation;
import il.org.spartan.spartanizer.tipping.Tipper;

/** A provider of matching {@link Tipper} for an {@link ASTNode}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2016-12-20 */
public abstract class OperationsProvider {
  /** @param n JD
   * @return matching {@link Tipper} */
  public abstract <N extends ASTNode> Tipper<N> getTipper(N n);
  /** @return a function from list<Op<?> to <Op<?>> which should mean to help us
   *         choose which tipper we would like to use */
  public abstract Function<List<Operation<?>>, List<Operation<?>>> getFunction();
}

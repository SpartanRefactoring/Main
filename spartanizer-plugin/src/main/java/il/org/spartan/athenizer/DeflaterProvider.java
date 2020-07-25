package il.org.spartan.athenizer;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.core.dom.ASTNode;

import fluent.ly.the;
import il.org.spartan.athenizer.SingleFlater.Operation;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.traversal.Toolbox;
import il.org.spartan.spartanizer.traversal.Toolboxes;

/** a wrapper for the original tippers configuration
 * @author Raviv Rachmiel
 * @since 20-12-16 will hold an configuration for the expanders and return
 *        them */
public class DeflaterProvider extends OperationsProvider {
  private Toolbox toolbox;

  public DeflaterProvider() {
    toolbox = Toolbox.full();
    if (toolbox == null)
      toolbox = Toolboxes.allClone();
  }
  public DeflaterProvider(final Toolbox tb) {
    toolbox = tb;
  }
  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }
  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return λ -> Collections.singletonList(the.lastOf(λ));
  }
}

package il.org.spartan.athenizer;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.athenizer.SingleFlater.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

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

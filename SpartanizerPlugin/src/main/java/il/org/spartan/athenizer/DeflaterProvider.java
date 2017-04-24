package il.org.spartan.athenizer;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.SingleFlater.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import nano.ly.*;

/** a wrapper for the original tippers configuration
 * @author Raviv Rachmiel
 * @since 20-12-16 will hold an configuration for the expanders and return
 *        them */
public class DeflaterProvider extends OperationsProvider {
  private Configuration configuration;

  public DeflaterProvider() {
    configuration = Configurations.all();
    if (configuration == null)
      configuration = Configurations.allClone();
  }

  public DeflaterProvider(final Configuration tb) {
    configuration = tb;
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return configuration.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return λ -> Collections.singletonList(the.lastOf(λ));
  }
}

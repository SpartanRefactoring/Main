package il.org.spartan.bloater;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.bloater.SingleFlater.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** a wrapper for the original tippers toolbox
 * @author Raviv Rachmiel
 * @since 20-12-16 will hold an toolbox for the expanders and return them */
public class DeflaterProvider extends OperationsProvider {
  private Configuration configuration;

  public DeflaterProvider() {
    configuration = Configuration.defaultInstance();
    if (configuration == null)
      configuration = Configuration.freshCopyOfAllTippers();
  }

  public DeflaterProvider(final Configuration tb) {
    configuration = tb;
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return configuration.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return λ -> Collections.singletonList(last(λ));
  }
}

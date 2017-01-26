package il.org.spartan.bloater;

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
  private Toolbox toolbox;

  public DeflaterProvider() {
    toolbox = Toolbox.defaultInstance();
    if (toolbox == null)
      toolbox = Toolbox.freshCopyOfAllTippers();
  }

  public DeflaterProvider(final Toolbox tb) {
    toolbox = tb;
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return list -> Collections.singletonList(list.get(list.size() - 1));
  }
}

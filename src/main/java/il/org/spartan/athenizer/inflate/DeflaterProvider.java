package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.inflate.SingleFlatter.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/* TODO Raviv: write ***Javadoc*** according to conventions --or
 * @author Raviv Rachmiel
 * @since 20-12-16
 * will hold an  toolbox for the expanders and return them
 */
public class DeflaterProvider extends OperationsProvider {
  Toolbox toolbox;
  
  public DeflaterProvider() {
    toolbox = Toolbox.defaultInstance();
    if(toolbox == null)
      toolbox = Toolbox.freshCopyOfAllTippers();
  }
  
  public DeflaterProvider(Toolbox tb) {
    this.toolbox = tb;
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(N n) {
    return toolbox.firstTipper(n);
  }

  @Override public Function<List<Operation<?>>, Operation<?>> getFunction() {
    return ((list) -> (list.get(list.size()-1)));
  }
  
}

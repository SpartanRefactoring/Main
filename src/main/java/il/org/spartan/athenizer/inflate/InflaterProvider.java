package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.inflate.SingleFlater.*;
import il.org.spartan.athenizer.inflate.expanders.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/* TODO Raviv: write ***Javadoc*** according to conventions --or
 *
 * @author Raviv Rachmiel
 *
 * @since 20-12-16 will hold the new toolbox for the expanders and return
 * them */
public class InflaterProvider extends OperationsProvider {
  Toolbox toolbox;

  public InflaterProvider() {
    toolbox = Toolbox.defaultInstance();
    if (toolbox == null)
      toolbox = Toolbox.freshCopyOfAllTippers();
  }

  public InflaterProvider(final Toolbox tb) {
    toolbox = tb;
  }

  public static Toolbox freshCopyOfAllExpanders() {
    return new Toolbox()//
        .add(Statement.class, //
            new TernaryExpander())//
        .add(InfixExpression.class, //
            new toStringExpander())//
    ;
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, Operation<?>> getFunction() {
    return (list) -> list.get(0);
  }
}

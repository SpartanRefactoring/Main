package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.inflate.SingleFlatter.*;
import il.org.spartan.spartanizer.tipping.*;

public abstract class OperationsProvider {
  public abstract <N extends ASTNode> Tipper<N> getTipper(N n);
  public abstract Function<List<Operation<?>>, Operation<?>> getFunction();
}

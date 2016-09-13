package il.org.spartan.spartanizer.wring.dispatch;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

public class ExclusionManager {
  final Set<ASTNode> inner = new HashSet<>();

  public void exclude(final ASTNode n) {
    inner.add(n);
  }

  public boolean isExcluded(final ASTNode n) {
    for (final ASTNode ancestor : hop.ancestors(n))
      if (inner.contains(ancestor))
        return true;
    return false;
  }

  void unExclude(final ASTNode n) {
    inner.remove(n);
  }
}
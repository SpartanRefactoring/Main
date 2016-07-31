package il.org.spartan.refactoring.wring;

import java.util.*;
import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;


class ExclusionManager {
  final Set<ASTNode> inner = new HashSet<>();

  boolean isExcluded(final ASTNode n) {
    for (final ASTNode ancestor : ancestors(n))
      if (inner.contains(ancestor))
        return true;
    return false;
  }
  void exclude(final ASTNode n) {
    inner.add(n);
  }
  void unExclude(final ASTNode n) {
    inner.remove(n);
  }
}
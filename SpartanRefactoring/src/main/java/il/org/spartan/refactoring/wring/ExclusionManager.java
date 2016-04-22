package il.org.spartan.refactoring.wring;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.refactoring.utils.Ancestors;

class ExclusionManager {
  final Set<ASTNode> inner = new HashSet<>();

  boolean isExcluded(final ASTNode n) {
    for (final ASTNode ancestor : new Ancestors(n))
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
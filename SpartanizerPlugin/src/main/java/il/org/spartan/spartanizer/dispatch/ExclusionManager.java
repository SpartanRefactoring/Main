package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Hack to stop the trimmer from making more tips. The class should die. It
 * serves the purpose of disabling tips of spartanization in a method, whose
 * parameters are changed. But this disabling does not belong here.
 * @author Yossi Gil
 * @year 2015 */
public final class ExclusionManager {
  final Set<ASTNode> inner = new HashSet<>();

  public void exclude(final ASTNode ¢) {
    inner.add(¢);
  }
  public void excludeAll(final List<? extends ASTNode> ¢) {
    inner.addAll(¢);
  }
  public boolean isExcluded(final ASTNode n) {
    for (final ASTNode ancestor : hop.ancestors(n))
      if (inner.contains(ancestor))
        return true;
    return false;
  }
  void unExclude(final ASTNode ¢) {
    inner.remove(¢);
  }
}
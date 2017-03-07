package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Hack to stop the trimmer from making more tips. The class should die. It
 * serves the purpose of disabling tips of spartanization in a method, whose
 * parameters are changed. But this disabling does not belong here.
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @year 2015 */
public final class ExclusionManager {
  private final Set<ASTNode> inner = new HashSet<>();

  public void exclude(final ASTNode ¢) {
    inner.add(¢);
  }

  public void excludeAll(final Collection<? extends ASTNode> ¢) {
    inner.addAll(¢);
  }

  public boolean isExcluded(final ASTNode ¢) {
    return az.stream(hop.ancestors(¢)).anyMatch(inner::contains);
  }

  void unExclude(final ASTNode ¢) {
    inner.remove(¢);
  }
}
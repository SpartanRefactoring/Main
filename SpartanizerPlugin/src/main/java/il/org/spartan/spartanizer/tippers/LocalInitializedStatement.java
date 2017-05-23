package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalInitializedStatement extends LocalInitialized {
  private static final long serialVersionUID = 1;

  @Override protected ASTNode[] span() {
    return as.array(current, nextStatement);
  }
  protected int waste() {
    return uses().size() * (metrics.size(Inliner.protect(initializer)) - 1);
  }
  private List<SimpleName> uses() {
    return collect.usesOf(name).in(nextStatement);
  }
  protected int saving() {
    return countOf.nodes(!singleFragment() ? current : declaration);
  }
  private boolean singleFragment() {
    return fragments(declaration).size() == 1;
  }
}
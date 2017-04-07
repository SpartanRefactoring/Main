package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalInitializedStatement extends LocalInitialized {
  private static final long serialVersionUID = 1;

  @Override protected ASTNode range() {
    return parent;
  }

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }

  protected int waste() {
    return uses().size() * (metrics.size(initializer) - 1);
  }

  private List<SimpleName> uses() {
   return collect.usesOf(name).in(nextStatement);
  }

  protected int saving() {
    return count.nodes(singleFragment() ? declaration : this.current);
  }

  private boolean singleFragment() {
    return fragments(declaration).size() == 1;
  }
}
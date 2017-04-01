package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalInitializedStatement extends LocalInitialized {
  private static final long serialVersionUID = 1L;

  @Override protected ASTNode range() {
    return parent;
  }

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}
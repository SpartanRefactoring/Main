package il.org.spartan.spartanizer.refactoringMechanism;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalVariableInitializedStatement extends LocalVariableInitialized {
  private static final long serialVersionUID = 0x1B70489B1D1340L;

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}
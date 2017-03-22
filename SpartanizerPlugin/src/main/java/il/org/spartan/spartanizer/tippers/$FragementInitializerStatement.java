package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class $FragementInitializerStatement extends $Fragment {
  private static final long serialVersionUID = 7723281513517888L;

  @Override public abstract String description(VariableDeclarationFragment f);

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}

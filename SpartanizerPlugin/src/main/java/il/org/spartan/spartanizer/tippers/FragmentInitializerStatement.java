package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class FragmentInitializerStatement extends $Fragment {
  @Override public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && initializer() != null;
  }

  private static final long serialVersionUID = 0x1B70489B1D1340L;

  @Override public abstract String description(VariableDeclarationFragment f);

  @Override public final Tip tip(@NotNull final VariableDeclarationFragment f, @Nullable final ExclusionManager exclude) {
    @Nullable final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}

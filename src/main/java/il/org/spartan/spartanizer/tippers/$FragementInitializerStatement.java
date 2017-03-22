package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class $FragementInitializerStatement extends $Fragment {
  private static final long serialVersionUID = 7723281513517888L;

  @Override public abstract String description(VariableDeclarationFragment f);

  @Override public final Tip tip(@NotNull final VariableDeclarationFragment f, @Nullable final ExclusionManager exclude) {
    @Nullable final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}

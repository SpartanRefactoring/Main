package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class FragementInitializerStatement extends FragmentTipper {
  @Override public boolean prerequisite(VariableDeclarationFragment f) {
    return super.prerequisite(f) && initializer() != null;
  }

  private static final long serialVersionUID = 7723281513517888L;

  @Override public abstract String description(VariableDeclarationFragment f);

  @Override public final Fragment tip(@NotNull final VariableDeclarationFragment f, @Nullable final ExclusionManager exclude) {
    @Nullable final Fragment $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}

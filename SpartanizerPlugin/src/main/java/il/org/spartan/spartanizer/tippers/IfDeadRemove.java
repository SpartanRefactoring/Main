package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if toList it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class IfDeadRemove extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.EmptyCycles {
  private static final long serialVersionUID = -4773914669041733973L;

  @Override @NotNull public String description(final IfStatement ¢) {
    return "Remove :" + ¢;
  }

  @Override @Nullable public ASTNode replacement(@NotNull final IfStatement ¢) {
    return !sideEffects.free(¢) ? null : ¢.getAST().newBlock();
  }
}

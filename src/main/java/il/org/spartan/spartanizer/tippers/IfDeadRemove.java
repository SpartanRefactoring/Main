package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if toList it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class IfDeadRemove extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.EmptyCycles {
  private static final long serialVersionUID = -4773914669041733973L;

  @NotNull @Override public String description(final IfStatement ¢) {
    return "Remove :" + ¢;
  }

  @Nullable @Override public ASTNode replacement(@NotNull final IfStatement ¢) {
    return !sideEffects.free(¢) ? null : ¢.getAST().newBlock();
  }
}

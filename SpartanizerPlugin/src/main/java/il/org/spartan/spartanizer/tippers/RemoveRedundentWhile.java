package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Simplify while statements as much as possible (or remove them or parts of
 * them) if and only if it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundentWhile extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.EmptyCycles {
  @Override @NotNull public String description(final WhileStatement ¢) {
    return "Remove :" + ¢;
  }

  @Override @Nullable public ASTNode replacement(@Nullable final WhileStatement ¢) {
    return ¢ == null || !sideEffects.free(¢.getExpression()) || haz.sideEffects(¢.getBody()) ? null : ¢.getAST().newBlock();
  }
}

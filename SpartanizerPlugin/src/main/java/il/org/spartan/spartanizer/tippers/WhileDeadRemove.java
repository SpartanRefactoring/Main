package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;



/** Simplify while statements as much as possible (or remove them or parts of
 * them) if and only if it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundentWhile extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.EmptyCycles {
  @Override  public String description(final WhileStatement ¢) {
    return "Remove :" + ¢;
  }

  @Override  public ASTNode replacement( final WhileStatement ¢) {
    return ¢ == null || !sideEffects.free(¢.getExpression()) || haz.sideEffects(¢.getBody()) ? null : ¢.getAST().newBlock();
  }
}

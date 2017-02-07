package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;



/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if toList
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundantWhile extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.EmptyCycles {
  @Override  public String description(final WhileStatement ¢) {
    return "remove :" + ¢;
  }

  @Override  public ASTNode replacement( final WhileStatement ¢) {
    return sideEffects.free(¢) ? null : ¢.getAST().newBlock();
  }
}

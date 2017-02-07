package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;



/** Simplify for statements as much as possible (or remove them or parts of
 * them) if and only if toList
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class ForDeadRemove extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.EmptyCycles {
  @Override  public String description(final ForStatement ¢) {
    return "remove :" + ¢;
  }

  @Override protected boolean prerequisite(ForStatement ¢) {
    return sideEffects.free(¢); 
  }

  @Override  public ASTNode replacement( final ForStatement ¢) {
    return ¢.getAST().newBlock();
  }
}

package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

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

  @Override  public ASTNode replacement( final ForStatement ¢) {
    return ¢ == null || !sideEffects.free(¢.getExpression()) || !sideEffects.free(initializers(¢)) || !sideEffects.free(updaters(¢))
        || !sideEffects.free(¢.getBody()) ? null : ¢.getAST().newBlock();
  }
}

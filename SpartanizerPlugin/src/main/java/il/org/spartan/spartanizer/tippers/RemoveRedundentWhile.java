package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify while statements as much as possible (or remove them or parts of
 * them) if and only if </br>
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundentWhile extends ReplaceCurrentNode<WhileStatement> implements TipperCategory.Collapse {
  @Override public String description(final WhileStatement ¢) {
    return "remove :" + ¢;
  }

  @Override public ASTNode replacement(final WhileStatement ¢) {
    return ¢ == null || haz.sideEffects(¢.getExpression()) || !RemoveRedundent.checkBlock(¢.getBody()) ? null : ¢.getAST().newBlock();
  }
}

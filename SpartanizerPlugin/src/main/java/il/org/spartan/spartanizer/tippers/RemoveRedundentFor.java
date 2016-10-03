package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify for statements as much as possible (or remove them or parts of
 * them) if and only if </br>
 * it doesn'tipper have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class RemoveRedundentFor extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Collapse {
  @Override public String description(final ForStatement ¢) {
    return "remove :" + ¢;
  }

  @Override public ASTNode replacement(final ForStatement ¢) {
    return ¢ == null || haz.sideEffects(¢.getExpression()) || !RemoveRedundent.checkListOfExpressions(initializers(¢))
        || !RemoveRedundent.checkListOfExpressions(updaters(¢)) || !RemoveRedundent.checkBlock(¢.getBody()) ? null : ¢.getAST().newBlock();
  }
}

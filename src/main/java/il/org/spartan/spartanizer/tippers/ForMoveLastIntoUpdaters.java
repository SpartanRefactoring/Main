package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

/** Move last statement in a for(;;) into updaters list
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class ForMoveLastIntoUpdaters extends LoopReplacer<ForStatement> implements TipperCategory.Unite {
  private static final long serialVersionUID = -0x50B5217BA3948A3EL;

  private static boolean notClaimedByOthers(final ForStatement ¢) {
    return cantTip.declarationInitializerStatementTerminatingScope(¢)//
        && cantTip.forRenameInitializerToIt(¢) //
        && cantTip.declarationRedundantInitializer(¢)//
        && cantTip.removeRedundantIf(¢);
  }

  @Override public String description(final ForStatement ¢) {
    return "Convert loop: 'for(?;" + ¢.getExpression() + ";?)' to something else (buggy)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    fillUp(¢.getBody());
    return validUpdater() && notClaimedByOthers(¢);
  }

  @Override public ForStatement replacement(final ForStatement ¢) {
    final ForStatement $ = copy.of(¢);
    step.updaters($).add(0, copy.of(updater));
    $.setBody(eliminate.lastStatement(copy.of(body)));
    return $;
  }
}

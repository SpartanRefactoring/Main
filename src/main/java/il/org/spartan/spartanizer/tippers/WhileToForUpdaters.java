package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForUpdaters extends LoopReplacer<WhileStatement>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 1L;

  private static boolean notClaimedByOthers(final WhileStatement ¢) {
    return cantTip.declarationInitializerStatementTerminatingScope(¢) && cantTip.declarationRedundantInitializer(¢) //
        && cantTip.remvoeRedundantIf(¢);
  }

  @Override public String description(final WhileStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final WhileStatement ¢) {
    fillUp(¢.getBody());
    return validUpdater() && notClaimedByOthers(¢);
  }

  @Override public ForStatement replacement(final WhileStatement ¢) {
    final ForStatement $ = body.getAST().newForStatement();
    $.setExpression(copy.of(¢.getExpression()));
    updaters($).add(copy.of(updater));
    $.setBody(eliminate.lastStatement(copy.of(body(¢))));
    return $;
  }
}

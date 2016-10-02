package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForUpdaters extends ReplaceCurrentNode<WhileStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("unchecked") private static ForStatement buildForWhithoutLastStatement(final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setExpression(dupWhileExpression(¢));
    $.updaters().add(updaterFromBody(¢));
    $.setBody(minus.lastStatement(dupWhileBody(¢)));
    return $;
  }

  private static Statement dupWhileBody(final WhileStatement ¢) {
    return duplicate.of(step.body(¢));
  }

  private static Expression dupWhileExpression(final WhileStatement ¢) {
    return duplicate.of(¢.getExpression());
  }

  private static boolean fitting(final WhileStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(step.body(¢)) && hasFittingUpdater(¢)
        && cantTip.declarationInitializerStatementTerminatingScope(¢) && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  private static boolean hasFittingUpdater(final WhileStatement ¢) {
    final Block bodyBlock = az.block(step.body(¢));
    return iz.incrementOrDecrement(lastStatement(¢)) && bodyBlock != null && step.statements(az.block(step.body(¢))).size() >= 2;
  }

  private static ASTNode lastStatement(final WhileStatement ¢) {
    return hop.lastStatement(step.body(¢));
  }

  private static Expression updaterFromBody(final WhileStatement ¢) {
    return duplicate.of(az.expressionStatement(hop.lastStatement(step.body(¢))).getExpression());
  }

  @Override public String description(final WhileStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final WhileStatement ¢) {
    return ¢ != null && fitting(¢);
  }

  @Override public ASTNode replacement(final WhileStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutLastStatement(¢);
  }
}

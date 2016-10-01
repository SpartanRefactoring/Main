package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForUpdaters extends ReplaceCurrentNode<WhileStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("unchecked") private static ForStatement buildForWhithoutLastStatement(final ForStatement $, final WhileStatement s) {
    $.setExpression(dupWhileExpression(s));
    $.updaters().add(dupWhileLastStatement(s));
    $.setBody(minus.firstLastStatement(dupWhileBody(s)));
    return $;
  }

  private static Statement dupWhileBody(final WhileStatement ¢) {
    return duplicate.of(step.body(¢));
  }

  private static Expression dupWhileExpression(final WhileStatement ¢) {
    return duplicate.of(¢.getExpression());
  }

  private static Expression dupWhileLastStatement(final WhileStatement ¢) {
    return duplicate.of(az.expressionStatement(firstLastStatement(¢)).getExpression());
  }

  private static ASTNode firstLastStatement(final WhileStatement ¢) {
    return hop.firstLastStatement(step.body(¢));
  }

  private static boolean fitting(final WhileStatement ¢) {
    return ¢ != null && statementFitts(lastStatement(¢)) && !haz.ContinueStatement(step.body(¢))
        && cantTip.declarationInitializerStatementTerminatingScope(¢);
  }

  private static boolean statementFitts(final Statement ¢) {
    return iz.incrementOrDecrement(¢) || haz.sideEffects(¢);
  }
  
  private static Statement lastStatement(final WhileStatement ¢) {
    return az.asStatement(hop.lastStatement(step.body(¢)));
  }

  @Override public String description(final WhileStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final WhileStatement ¢) {
    return ¢ != null && fitting(¢);
  }

  @Override public ASTNode replacement(final WhileStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutLastStatement(¢.getAST().newForStatement(), ¢);
  }
}

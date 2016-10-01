package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
public class ForToForUpdaters extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("unchecked") private static ForStatement buildForWhithoutFirstLastStatement(final ForStatement $) {
    $.updaters().add(dupWhileStatement($));
    $.setBody(minus.firstLastStatement(dupForBody($)));
    return $;
  }

  private static Statement dupForBody(final ForStatement ¢) {
    return duplicate.of(step.body(¢));
  }

  private static Expression dupWhileStatement(final ForStatement ¢) {
    return duplicate.of(az.expressionStatement(firstLastStatement(¢)).getExpression());
  }

  private static ASTNode firstLastStatement(final ForStatement ¢) {
    return findFirst.statementCanBePushedToForUpdaters(step.body(¢));
  }
  
  private static boolean fitting(final ForStatement ¢) {
    return cantTip.declarationInitializerStatementTerminatingScope(¢)
        && cantTip.forRenameInitializerToCent(¢) && ¢ != null
        && cantTip.declarationRedundantInitializer(¢)
        && cantTip.remvoeRedundantIf(¢)
        && fittingUpdater(¢)
        && !iz.containsContinueStatement(step.body(¢));
  }
  
  private static boolean fittingUpdater(final ForStatement ¢) {
    final Statement updater = az.asStatement(findFirst.statementCanBePushedToForUpdaters(step.body(¢)));
    return updater != null && updatesOnlyForInitializers(¢, updater);
  }
  
  private static boolean updatesOnlyForInitializers(final ForStatement ¢, final Statement updater) {
    return true;
    // TODO: Alex and Dan, implement after uses, declares and defines.
  }
  
  @Override public String description(final ForStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(step.body(¢));
  }

  @Override public ASTNode replacement(final ForStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutFirstLastStatement(duplicate.of(¢));
  }
}

package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForUpdaters extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.Unite {
  @NotNull private static ForStatement buildForWhithoutLastStatement(@NotNull final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setExpression(dupWhileExpression(¢));
    step.updaters($).add(dupUpdaterFromBody(¢));
    $.setBody(minus.lastStatement(dupWhileBody(¢)));
    return $;
  }

  @Nullable private static Expression dupUpdaterFromBody(final WhileStatement ¢) {
    return copy.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  @Nullable private static Statement dupWhileBody(final WhileStatement ¢) {
    return copy.of(step.body(¢));
  }

  @Nullable private static Expression dupWhileExpression(@NotNull final WhileStatement ¢) {
    return copy.of(¢.getExpression());
  }

  private static boolean fitting(@Nullable final WhileStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(step.body(¢)) && hasFittingUpdater(¢)
        && cantTip.declarationInitializerStatementTerminatingScope(¢) && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  private static boolean hasFittingUpdater(final WhileStatement ¢) {
    return az.block(step.body(¢)) != null && iz.incrementOrDecrement(lastStatement(¢)) && step.statements(az.block(step.body(¢))).size() >= 2
        && !ForToForUpdaters.bodyDeclaresElementsOf(lastStatement(¢));
  }

  private static ASTNode lastStatement(final WhileStatement ¢) {
    return hop.lastStatement(step.body(¢));
  }

  @Override @NotNull public String description(@NotNull final WhileStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(@Nullable final WhileStatement ¢) {
    return ¢ != null && fitting(¢);
  }

  @Override @Nullable public ASTNode replacement(@NotNull final WhileStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutLastStatement(¢);
  }
}

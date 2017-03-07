package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForUpdaters extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -382828224028857273L;

  private static ForStatement buildForWhithoutLastStatement(final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setExpression(copy.of(¢.getExpression()));
    updaters($).add(copy.of(az.expressionStatement(lastStatement(¢)).getExpression()));
    $.setBody(minus.lastStatement(copy.of(body(¢))));
    return $;
  }

  private static boolean fitting(final WhileStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(body(¢)) && hasFittingUpdater(¢) && cantTip.declarationInitializerStatementTerminatingScope(¢)
        && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  private static boolean hasFittingUpdater(final WhileStatement ¢) {
    return az.block(body(¢)) != null && iz.incrementOrDecrement(lastStatement(¢)) && statements(az.block(body(¢))).size() >= 2
        && !ForToForUpdaters.bodyDeclaresElementsOf(lastStatement(¢));
  }

  private static ASTNode lastStatement(final WhileStatement ¢) {
    return hop.lastStatement(body(¢));
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

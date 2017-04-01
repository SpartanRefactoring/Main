package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** A duplicates of {@link WhileToForUpdaters}?
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForWithUpdater extends ReplaceCurrentNode<WhileStatement>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -382828224028857273L;

  private static ForStatement buildForWhithoutLastStatement(final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setExpression(copy.of(¢.getExpression()));
    updaters($).add(copy.of(az.expressionStatement(hop.lastStatement(body(¢))).getExpression()));
    $.setBody(minus.lastStatement(copy.of(body(¢))));
    return $;
  }

  private static boolean fitting(final WhileStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(body(¢)) && hasFittingUpdater(¢) && cantTip.declarationInitializerStatementTerminatingScope(¢)
        && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  private static boolean hasFittingUpdater(final WhileStatement ¢) {
    return az.block(body(¢)) != null && iz.updating(hop.lastStatement(body(¢))) && statements(az.block(body(¢))).size() >= 2
        && !ForToForUpdaters.bodyDeclaresElementsOf(hop.lastStatement(body(¢)));
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

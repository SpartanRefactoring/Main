package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.minus;
import il.org.spartan.spartanizer.ast.navigate.cantTip;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** A duplicates of {@link WhileToForUpdaters}?
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class WhileToForWithUpdater extends ReplaceCurrentNode<WhileStatement>//
    implements Category.Loops {
  private static final long serialVersionUID = -0x550143DCF3BD3B9L;

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

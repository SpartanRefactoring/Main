package il.org.spartan.spartanizer.tippers;

import java.util.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class ForToForUpdaters extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.Unite {
  private static ForStatement buildForWhithoutFirstLastStatement(final ForStatement $) {
    setUpdaters($);
    $.setBody(minus.lastStatement(dupForBody($)));
    return $;
  }

  private static Statement dupForBody(final ForStatement ¢) {
    return copy.of(step.body(¢));
  }

  private static boolean fitting(final ForStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(step.body(¢)) && hasFittingUpdater(¢)
        && cantTip.declarationInitializerStatementTerminatingScope(¢) && cantTip.forRenameInitializerToCent(¢)
        && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  private static boolean hasFittingUpdater(final ForStatement ¢) {
    final Block bodyBlock = az.block(step.body(¢));
    if (!iz.incrementOrDecrement(lastStatement(¢)) || bodyBlock == null || step.statements(bodyBlock).size() < 2
        || bodyDeclaresElementsOf(lastStatement(¢)))
      return false;
    final ExpressionStatement updater = az.expressionStatement(lastStatement(¢));
    assert updater != null : "updater is not expressionStatement";
    final Expression e = updater.getExpression();
    final PrefixExpression $ = az.prefixExpression(e);
    final PostfixExpression post = az.postfixExpression(e);
    final Assignment a = az.assignment(e);
    return updaterDeclaredInFor(¢,
        $ != null ? az.simpleName($.getOperand()) : post != null ? az.simpleName(post.getOperand()) : a != null ? az.simpleName(step.left(a)) : null);
  }

  public static boolean bodyDeclaresElementsOf(final ASTNode n) {
    final Block $ = az.block(n.getParent());
    return $ != null && extract.fragments($).stream().anyMatch(λ -> !collect.usesOf(λ.getName()).in(n).isEmpty());
  }

  private static ASTNode lastStatement(final ForStatement ¢) {
    return hop.lastStatement(step.body(¢));
  }

  private static void setUpdaters(final ForStatement $) {
    final List<Expression> oldUpdaters = new ArrayList<>(step.updaters($));
    step.updaters($).clear();
    step.updaters($).add(updaterFromBody($));
    step.updaters($).addAll(oldUpdaters);
  }

  private static boolean updaterDeclaredInFor(final ForStatement s, final SimpleName n) {
    return step.fragments(az.variableDeclarationExpression(findFirst.elementOf(step.initializers(s)))).stream()
        .anyMatch(λ -> (λ.getName() + "").equals(n + ""));
  }

  private static Expression updaterFromBody(final ForStatement ¢) {
    return copy.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  @Override public String description(final ForStatement ¢) {
    return "Convert loop: 'for(?;" + ¢.getExpression() + ";?)' to something else (buggy)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && fitting(¢);
  }

  @Override public ASTNode replacement(final ForStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutFirstLastStatement(copy.of(¢));
  }
}

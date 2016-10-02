package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
public class ForToForUpdaters extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Collapse {
  private static ForStatement buildForWhithoutFirstLastStatement(final ForStatement $) {
    setUpdaters($);
    // TODO Alex, minus should return void, and then no need to duplicate.
    $.setBody(minus.lastStatement(dupForBody($)));
    return $;
  }

  private static Statement dupForBody(final ForStatement ¢) {
    return duplicate.of(step.body(¢));
  }

  private static boolean fitting(final ForStatement ¢) {
    return ¢ != null && !iz.containsContinueStatement(step.body(¢)) && hasFittingUpdater(¢)
        && cantTip.declarationInitializerStatementTerminatingScope(¢) && cantTip.forRenameInitializerToCent(¢)
        && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  private static boolean hasFittingUpdater(final ForStatement ¢) {
    final Block bodyBlock = az.block(step.body(¢));
    if (!iz.incrementOrDecrement(lastStatement(¢)) || bodyBlock == null || step.statements(bodyBlock).size() < 2)
      return false;
    final ExpressionStatement updater = az.expressionStatement(lastStatement(¢));
    assert updater != null : "updater is not expressionStatement";
    // if (updater == null)
    // return false;
    final Expression e = updater.getExpression();
    final PrefixExpression pre = az.prefixExpression(e);
    final PostfixExpression post = az.postfixExpression(e);
    final SimpleName n = pre == null ? az.simpleName(post.getOperand()) : az.simpleName(pre.getOperand());
    return updaterDeclaredInFor(¢, n);
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
    final VariableDeclarationExpression vde = az.variableDeclarationExpression(findFirst.elementOf(step.initializers(s)));
    for (final VariableDeclarationFragment f : step.fragments(vde))
      if (f.getName().toString().equals(n.toString()))
        return true;
    return false;
  }

  private static Expression updaterFromBody(final ForStatement ¢) {
    return duplicate.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  @Override public String description(final ForStatement ¢) {
    return "Convert the while about '(" + ¢.getExpression() + ")' to a traditional for(;;)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && fitting(¢);
  }

  @Override public ASTNode replacement(final ForStatement ¢) {
    return !fitting(¢) ? null : buildForWhithoutFirstLastStatement(duplicate.of(¢));
  }
}

package il.org.spartan.spartanizer.tippers;
import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class ForMoveLastIntoUpdaters extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -5815591308727978558L;
  private ExpressionStatement updater;
  private boolean hasFittingUpdater(final ForStatement ¢) {
    final Block b = az.block(body(¢));
    if (b == null || statements(b).size() < 2)
      return false;
    Statement candidate = last(statements(b));
    if (candidate == null || !iz.updater(candidate) || bodyDeclaresElementsOf(candidate))
      return false;
    updater = az.expressionStatement(candidate);
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

  private static boolean updaterDeclaredInFor(final ForStatement s, final SimpleName n) {
    return fragments(az.variableDeclarationExpression(first(step.initializers(s)))).stream().anyMatch(λ -> (λ.getName() + "").equals(n + ""));
  }

  private static Expression updaterFromBody(final ForStatement ¢) {
    return copy.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  @Override public String description(final ForStatement ¢) {
    return "Convert loop: 'for(?;" + ¢.getExpression() + ";?)' to something else (buggy)";
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    return !haz.containsContinueStatement(step.body(¢)) && hasFittingUpdater(¢)
    && cantTip.declarationInitializerStatementTerminatingScope(¢) && cantTip.forRenameInitializerToCent(¢)
    && cantTip.declarationRedundantInitializer(¢) && cantTip.remvoeRedundantIf(¢);
  }

  @Override public ForStatement replacement(final ForStatement ¢) {
    final ForStatement $ = copy.of(¢);
    step.updaters($).add(0, updaterFromBody($));
    Statement removeLastStatement = eliminate.lastStatement(body($));
    $.setBody(removeLastStatement);
    return $;
  }
}

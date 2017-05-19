package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** See {@link #description()}
 * @author Alex Kopzon
 * @since 2016-09-23 */
public class ForToForUpdaters extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.Loops {
  private static final long serialVersionUID = -0x50B5217BA3948A3EL;

  private static ForStatement buildForWithoutLastStatement(final ForStatement $) {
    setUpdaters($);
    $.setBody(minus.lastStatement(copy.of(body($))));
    return $;
  }
  @Override public String description() {
    return "Move last statement of a for(;;) loop into the list of updaters";
  }
  private static boolean fitting(final ForStatement ¢) {
    return ¢ != null//
        && !iz.containsContinueStatement(step.body(¢))//
        && hasFittingUpdater(¢)//
        && cantTip.declarationInitializerStatementTerminatingScope(¢)//
        && cantTip.forRenameInitializerToIt(¢)//
        && cantTip.declarationRedundantInitializer(¢)//
        && cantTip.removeRedundantIf(¢);
  }
  private static boolean hasFittingUpdater(final ForStatement ¢) {
    final Block bodyBlock = az.block(step.body(¢));
    if (!iz.updating(lastStatement(¢)) || bodyBlock == null || step.statements(bodyBlock).size() < 2 || bodyDeclaresElementsOf(lastStatement(¢)))
      return false;
    final ExpressionStatement updater = az.expressionStatement(lastStatement(¢));
    assert updater != null : "updater is not expressionStatement";
    final Expression e = expression(updater);
    final PrefixExpression $ = az.prefixExpression(e);
    final PostfixExpression post = az.postfixExpression(e);
    final Assignment a = az.assignment(e);
    return updaterDeclaredInFor(¢,
        $ != null ? az.simpleName(operand($)) : post != null ? az.simpleName(operand(post)) : a != null ? az.simpleName(left(a)) : null);
  }
  public static boolean bodyDeclaresElementsOf(final ASTNode n) {
    final Block $ = az.block(n.getParent());
    return $ != null && extract.fragments($).stream().anyMatch(λ -> !collect.usesOf(λ.getName()).in(n).isEmpty());
  }
  private static ASTNode lastStatement(final ForStatement ¢) {
    return hop.lastStatement(body(¢));
  }
  private static void setUpdaters(final ForStatement $) {
    final Collection<Expression> oldUpdaters = as.list(step.updaters($));
    updaters($).clear();
    updaters($).add(updaterFromBody($));
    updaters($).addAll(oldUpdaters);
  }
  private static boolean updaterDeclaredInFor(final ForStatement s, final SimpleName n) {
    return fragments(az.variableDeclarationExpression(the.firstOf(initializers(s)))).stream().anyMatch(λ -> (name(λ) + "").equals(n + ""));
  }
  private static Expression updaterFromBody(final ForStatement ¢) {
    return copy.of(expression(az.expressionStatement(lastStatement(¢))));
  }
  @Override public String description(final ForStatement ¢) {
    return "Convert loop: 'for(?;" + expression(¢) + ";?)' to something else (buggy)";
  }
  @Override public boolean prerequisite(final ForStatement ¢) {
    return ¢ != null && fitting(¢);
  }
  @Override public ASTNode replacement(final ForStatement ¢) {
    return !fitting(¢) ? null : buildForWithoutLastStatement(copy.of(¢));
  }
}

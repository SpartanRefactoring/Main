package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.extendedOperands;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Statement;

import fluent.ly.the;
import il.org.spartan.athenizer.zoom.zoomers.Issue0976;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.plugin.Wizard;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Test case is {@link Issue0976} Issue #976 Convert: {@code if (a == b && c ==
 * d) { a = 5; } } to: {@code if (a == b) { if (c == d) { a = 5; } } }
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-09 */
public class LongIfBloater extends ReplaceCurrentNode<IfStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = -0x1470E408344718CBL;

  @Override public ASTNode replacement(final IfStatement ¢) {
    if (!shouldTip(¢))
      return null;
    final InfixExpression $ = az.infixExpression(¢.getExpression());
    final IfStatement newThen = subject.pair(then(¢), null).toIf(!$.hasExtendedOperands() ? $.getRightOperand() : getReducedIEFromIEWithExtOp($));
    final Statement oldElse = ¢.getElseStatement();
    if (oldElse == null)
      return subject.pair(newThen, null).toIf($.getLeftOperand());
    newThen.setElseStatement(copy.of(oldElse));
    return subject.pair(newThen, copy.of(oldElse)).toIf($.getLeftOperand());
  }
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace an if statement that contains && with two ifs";
  }
  private static Expression getReducedIEFromIEWithExtOp(final InfixExpression ¢) {
    final InfixExpression $ = subject.pair(¢.getRightOperand(), the.firstOf(extendedOperands(¢))).to(¢.getOperator());
    Wizard.append($, step.extendedOperands(¢));
    if (!$.extendedOperands().isEmpty())
      $.extendedOperands().remove(0);
    return $;
  }
  private static boolean shouldTip(final IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd(az.infixExpression(¢.getExpression())) && !tooComplicated(¢);
  }
  private static boolean tooComplicated(final IfStatement ¢) {
    return step.elze(¢) != null && step.extendedOperands(az.infixExpression(¢.getExpression())) != null
        && !step.extendedOperands(az.infixExpression(¢.getExpression())).isEmpty();
  }
}

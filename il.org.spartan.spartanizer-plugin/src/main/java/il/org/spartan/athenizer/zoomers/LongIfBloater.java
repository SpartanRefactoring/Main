package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.athenizer.zoom.zoomers.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

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
    final InfixExpression ret = az.infixExpression(¢.getExpression());
    final IfStatement newThen = subject.pair(then(¢), null).toIf(!ret.hasExtendedOperands() ? ret.getRightOperand() : getReducedIEFromIEWithExtOp(ret));
    final Statement oldElse = ¢.getElseStatement();
    if (oldElse == null)
      return subject.pair(newThen, null).toIf(ret.getLeftOperand());
    newThen.setElseStatement(copy.of(oldElse));
    return subject.pair(newThen, copy.of(oldElse)).toIf(ret.getLeftOperand());
  }
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace an if statement that contains && with two ifs";
  }
  private static Expression getReducedIEFromIEWithExtOp(final InfixExpression ¢) {
    final InfixExpression ret = subject.pair(¢.getRightOperand(), the.firstOf(extendedOperands(¢))).to(¢.getOperator());
    subject.append(ret, step.extendedOperands(¢));
    if (!ret.extendedOperands().isEmpty())
      ret.extendedOperands().remove(0);
    return ret;
  }
  private static boolean shouldTip(final IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd(az.infixExpression(¢.getExpression())) && !tooComplicated(¢);
  }
  private static boolean tooComplicated(final IfStatement ¢) {
    return step.elze(¢) != null && step.extendedOperands(az.infixExpression(¢.getExpression())) != null
        && !step.extendedOperands(az.infixExpression(¢.getExpression())).isEmpty();
  }
}

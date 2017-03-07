package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0976} Issue #976 Convert: {@code if (a == b && c ==
 * d) { a = 5; } } to: {@code if (a == b) { if (c == d) { a = 5; } } }
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-09 */
public class LongIfBloater extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -1472927802038098123L;

  @Override public ASTNode replacement(final IfStatement ¢) {
    if (!shouldTip(¢))
      return null;
    final InfixExpression ie = az.infixExpression(¢.getExpression());
    final IfStatement newThen = subject.pair(then(¢), null)
        .toIf(!ie.hasExtendedOperands() ? ie.getRightOperand() : az.expression(getReducedIEFromIEWithExtOp(ie))),
        $ = subject.pair(newThen, null).toIf(az.infixExpression(¢.getExpression()).getLeftOperand());
    final Statement oldElse = ¢.getElseStatement();
    if (oldElse != null) {
      newThen.setElseStatement(oldElse);
      $.setThenStatement(newThen);
      $.setElseStatement(oldElse);
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace an if statement that contains && with two ifs";
  }

  private static InfixExpression getReducedIEFromIEWithExtOp(final InfixExpression ¢) {
    final InfixExpression $ = subject.pair(¢.getRightOperand(), first(extendedOperands(¢))).to(¢.getOperator());
    subject.append($, step.extendedOperands(¢)).extendedOperands().remove(0);
    return $;
  }

  private static boolean shouldTip(final IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd((InfixExpression) ¢.getExpression());
  }
}

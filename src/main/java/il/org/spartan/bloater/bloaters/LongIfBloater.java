package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0976} Issue #976 Convert:
 *
 * <pre>
 * if (a == b && c == d) {
 *   a = 5;
 * }
 * </pre>
 *
 * to:
 *
 * <pre>
 * if (a == b) {
 *   if (c == d) {
 *     a = 5;
 *   }
 * }
 * </pre>
 *
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-09 */
public class LongIfBloater extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final IfStatement ¢) {
    if (!shouldTip(¢))
      return null;
    final InfixExpression ie = az.infixExpression(¢.getExpression());
    final IfStatement newThen = subject.pair(copy.of(¢.getThenStatement()), null)
        .toIf(!ie.hasExtendedOperands() ? copy.of(ie.getRightOperand()) : az.expression(getReducedIEFromIEWithExtOp(¢, ie))),
        $ = subject.pair(newThen, null).toIf(copy.of(az.infixExpression(¢.getExpression()).getLeftOperand()));
    if (¢.getElseStatement() != null) {
      newThen.setElseStatement(copy.of(¢.getElseStatement()));
      $.setThenStatement(newThen);
      $.setElseStatement(copy.of(¢.getElseStatement()));
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace an if statement that contains && with two ifs";
  }

  private static InfixExpression getReducedIEFromIEWithExtOp(final IfStatement ¢, final InfixExpression x) {
    // TODO: Tomer Dragucki use class subject --yg
    final InfixExpression $ = ¢.getAST().newInfixExpression();
    $.setOperator(x.getOperator());
    $.setLeftOperand(copy.of(x.getRightOperand()));
    $.setRightOperand(copy.of((Expression) x.extendedOperands().get(0)));
    step.extendedOperands($).addAll(copy.of(step.extendedOperands(x)));
    $.extendedOperands().remove(0);
    return $;
  }

  private static boolean shouldTip(final IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd((InfixExpression) ¢.getExpression());
  }
}

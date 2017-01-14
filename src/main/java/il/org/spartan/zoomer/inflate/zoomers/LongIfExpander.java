package il.org.spartan.zoomer.inflate.zoomers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
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
public class LongIfExpander extends ReplaceCurrentNode<IfStatement> implements TipperCategory.Expander {
  @Override public ASTNode replacement(final IfStatement ¢) {
    if (!shouldTip(¢))
      return null;
    final IfStatement $ = ¢.getAST().newIfStatement();
    // TODO: Tomer Dragucki please use class 'az' --yg
    final InfixExpression ie = (InfixExpression) ¢.getExpression();
    // TODO: Tomer Dragucki please use left --yg
    $.setExpression(copy.of(ie.getLeftOperand()));
    // TODO: Tomer Dragucki use subject.pair().toIf() --yg
    final IfStatement newThen = ¢.getAST().newIfStatement();
    final Expression ne = !ie.hasExtendedOperands() ? copy.of(ie.getRightOperand()) : az.expression(getReducedIEFromIEWithExtOp(¢, ie));
    newThen.setExpression(ne);
    newThen.setThenStatement(copy.of(¢.getThenStatement()));
    $.setThenStatement(newThen);
    if (¢.getElseStatement() != null) {
      newThen.setElseStatement(copy.of(¢.getElseStatement()));
      $.setElseStatement(copy.of(¢.getElseStatement()));
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace an if statement that contains && with two ifs";
  }

  // TODO: Tomer Dragucki remove and use class step --yg
  @SuppressWarnings("unchecked") private static InfixExpression getReducedIEFromIEWithExtOp(final IfStatement ¢, final InfixExpression x) {
    // TODO: Tomer Dragucki use class subject --yg
    final InfixExpression $ = ¢.getAST().newInfixExpression();
    $.setOperator(x.getOperator());
    $.setLeftOperand(copy.of(x.getRightOperand()));
    $.setRightOperand(copy.of((Expression) x.extendedOperands().get(0)));
    $.extendedOperands().addAll(copy.of(x.extendedOperands()));
    $.extendedOperands().remove(0);
    return $;
  }

  private static boolean shouldTip(final IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd((InfixExpression) ¢.getExpression());
  }
}

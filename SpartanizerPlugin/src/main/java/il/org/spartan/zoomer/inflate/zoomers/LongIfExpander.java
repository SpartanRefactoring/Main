package il.org.spartan.zoomer.inflate.zoomers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.idiomatics.*;
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
  @Override public ASTNode replacement(IfStatement ¢) {
    if (!shouldTip(¢))
      return null;
    IfStatement $ = ¢.getAST().newIfStatement();
    InfixExpression ie = (InfixExpression) ¢.getExpression();
    $.setExpression(copy.of(ie.getLeftOperand()));
    IfStatement newThen = ¢.getAST().newIfStatement();
    Expression ne = !ie.hasExtendedOperands() ? copy.of(ie.getRightOperand()) : az.expression(getReducedIEFromIEWithExtOp(¢, ie));
    newThen.setExpression(ne);
    newThen.setThenStatement(copy.of(¢.getThenStatement()));
    $.setThenStatement(newThen);
    if (¢.getElseStatement() != null) {
      newThen.setElseStatement(copy.of(¢.getElseStatement()));
      $.setElseStatement(copy.of(¢.getElseStatement()));
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") IfStatement __) {
    // boolean a = true, b = true, c = true;
    // if (a && b && c) {
    // a = false;
    // }
    // if (a && b) {
    // a=false;
    // }
    //
    // if (a) {
    // if (b) {
    // a = false;
    // }
    // }
    return "Replace an if statement that contains && with two ifs";
  }

  @SuppressWarnings("unchecked") private static InfixExpression getReducedIEFromIEWithExtOp(IfStatement ¢, InfixExpression ie) {
    InfixExpression $ = ¢.getAST().newInfixExpression();
    $.setOperator(ie.getOperator());
    $.setLeftOperand(copy.of(ie.getRightOperand()));
    $.setRightOperand(copy.of((Expression) ie.extendedOperands().get(0)));
    $.extendedOperands().addAll(copy.of(ie.extendedOperands()));
    $.extendedOperands().remove(0);
    return $;
  }

  private static boolean shouldTip(IfStatement ¢) {
    return iz.infixExpression(¢.getExpression()) && iz.conditionalAnd((InfixExpression) ¢.getExpression());
  }
}

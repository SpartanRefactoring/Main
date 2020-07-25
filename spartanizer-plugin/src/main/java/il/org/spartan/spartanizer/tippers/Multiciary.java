package il.org.spartan.spartanizer.tippers;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import fluent.ly.list;
import il.org.spartan.spartanizer.ast.navigate.step;

/** @author Yossi Gil
 * @since 2017-04-25 */
public abstract class Multiciary extends NodeMatcher<InfixExpression> {
  private static final long serialVersionUID = 1;
  protected int arity;
  protected Expression left;
  protected List<Expression> operands;
  protected Operator operator;
  protected List<Expression> rest;
  protected Expression right;

  public Multiciary() {
    property("Left", () -> left = current.getLeftOperand());
    property("Right", () -> right = current.getRightOperand());
    property("Operator", () -> operator = current.getOperator());
    property("Rest", () -> rest = step.extendedOperands(current));
    property("Operands", () -> operands = list.prepend(left).to().prepend(right).to(rest));
    property("Arity", () -> arity = operands.size());
  }
}

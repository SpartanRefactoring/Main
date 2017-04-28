package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** @author Yossi Gil
 * @since 2017-04-25 */
public abstract class InfixExpressionPattern extends NodePattern<InfixExpression> {
  private static final long serialVersionUID = 1;

  protected int arity;
  protected Expression left;
  protected List<Expression> operands;
  protected Operator operator;
  protected List<Expression> rest;
  protected Expression right;

  public InfixExpressionPattern() {
    property("Left", () -> left = current.getLeftOperand());
    property("Right", () -> right = current.getRightOperand());
    property("Operator", () -> operator = current.getOperator());
    property("Rest", () -> rest = step.extendedOperands(current));
    property("Operands", () -> operands = list.prepend(left).to().prepend(right).to(rest));
    property("Arity", () -> arity = operands.size());
  }
}

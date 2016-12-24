package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class AssignmentOperatorExpansion extends CarefulTipper<Assignment> implements TipperCategory.InVain {
  @Override public String description(@SuppressWarnings("unused") Assignment __) {
    return "use regualr assignment wth operator";
  }

  @Override protected boolean prerequisite(Assignment ¢) {
    return convertToInfix(¢.getOperator()) != null;
  }

  @Override public Tip tip(Assignment ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        InfixExpression e = ¢.getAST().newInfixExpression();
        e.setLeftOperand(duplicate.of(¢.getLeftHandSide()));
        e.setRightOperand(make.plant(duplicate.of(¢.getRightHandSide())).into(e));
        e.setOperator(convertToInfix(¢.getOperator()));
        Assignment a = ¢.getAST().newAssignment();
        a.setLeftHandSide(duplicate.of(¢.getLeftHandSide()));
        a.setRightHandSide(e);
        a.setOperator(Operator.ASSIGN);
        r.replace(¢, a, g);
      }
    };
  }

  static InfixExpression.Operator convertToInfix(Operator ¢) {
    return ¢ == Operator.BIT_AND_ASSIGN ? InfixExpression.Operator.AND
        : ¢ == Operator.BIT_OR_ASSIGN ? InfixExpression.Operator.OR
            : ¢ == Operator.BIT_XOR_ASSIGN ? InfixExpression.Operator.XOR
                : ¢ == Operator.DIVIDE_ASSIGN ? InfixExpression.Operator.DIVIDE
                    : ¢ == Operator.LEFT_SHIFT_ASSIGN ? InfixExpression.Operator.LEFT_SHIFT
                        : ¢ == Operator.MINUS_ASSIGN ? InfixExpression.Operator.MINUS
                            : ¢ == Operator.PLUS_ASSIGN ? InfixExpression.Operator.PLUS
                                : ¢ == Operator.REMAINDER_ASSIGN ? InfixExpression.Operator.REMAINDER
                                    : ¢ == Operator.RIGHT_SHIFT_SIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_SIGNED
                                        : ¢ == Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED : null;
  }
}

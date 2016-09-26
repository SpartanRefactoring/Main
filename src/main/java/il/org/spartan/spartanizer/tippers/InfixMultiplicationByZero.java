package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert a multiplication of expression\statement by zero to zero <br/>
 * where there is no any side effect
 * @author Dor Ma'ayan
 * @since 2016-09-25
 * @see {@link sideEffects} */
public class InfixMultiplicationByZero extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  @Override public String description(final InfixExpression ¢) {
    return "Convert" + ¢ + " to 0";
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    if (¢.getOperator() != TIMES || !isContainsZero(¢) || isContainsSideEffect(¢))
      return null;
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("0");
    return $;
  }

  private boolean isContainsSideEffect(final InfixExpression ¢) {
    final List<Expression> operands = extract.allOperands(¢);
    for (final Expression e : operands)
      if (!sideEffects.free(e))
        return true;
    return false;
  }

  private boolean isContainsZero(final InfixExpression ¢) {
    final List<Expression> operands = extract.allOperands(¢);
    for (final Expression e : operands)
      if (iz.numberLiteral(e) && az.numberLiteral(e).getToken().equals("0"))
        return true;
    return false;
  }
}

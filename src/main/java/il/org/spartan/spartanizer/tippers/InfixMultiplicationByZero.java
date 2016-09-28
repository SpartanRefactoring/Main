package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

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
public class InfixMultiplicationByZero extends ReplaceCurrentNode<InfixExpression> implements Category.InVain {
  private static boolean isContainsSideEffect(final InfixExpression x) {
    for (final Expression ¢ : extract.allOperands(x))
      if (haz.sideEffects(¢))
        return true;
    return false;
  }

  @Override public String description(final InfixExpression ¢) {
    return "Convert" + ¢ + " to 0";
  }

  private boolean isContainsZero(final InfixExpression x) {
    for (final Expression ¢ : extract.allOperands(x))
      if (iz.numberLiteral(¢) && "0".equals(az.numberLiteral(¢).getToken()))
        return true;
    return false;
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    if (¢.getOperator() != TIMES || !isContainsZero(¢) || isContainsSideEffect(¢))
      return null;
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("0");
    return $;
  }
}

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
public class InfixMultiplicationByZero extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  // TODO: Yossi make an issue about this bug: check for usage also in the
  // generator expressiona
  // TOOD: Yossi and another issue, allow inlining into generator expression
  private static boolean isContainsSideEffect(final InfixExpression ¢) {
    for (final Expression e : extract.allOperands(¢))
      if (!sideEffects.free(e))
        return true;
    return false;
  }

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

  private boolean isContainsZero(final InfixExpression ¢) {
    for (final Expression e : extract.allOperands(¢))
      if (iz.numberLiteral(e) && "0".equals(az.numberLiteral(e).getToken()))
        return true;
    return false;
  }
}

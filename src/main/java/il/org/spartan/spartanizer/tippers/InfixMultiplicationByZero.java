package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert a multiplication of expression\statement by zero to zero where there
 * is no any side effect
 * @author Dor Ma'ayan
 * @since 2016-09-25
 * @see {@link sideEffects} */
public class InfixMultiplicationByZero extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  private static final long serialVersionUID = 0x5511953EBF0A7FD5L;

  private static boolean containsZero(@NotNull final InfixExpression x) {
    return extract.allOperands(x).stream().anyMatch(λ -> iz.numberLiteral(λ) && "0".equals(az.numberLiteral(λ).getToken()));
  }

  private static boolean isContainsSideEffect(@NotNull final InfixExpression x) {
    return extract.allOperands(x).stream().anyMatch(λ -> !sideEffects.free(λ));
  }

  @Override @NotNull public String description(final InfixExpression ¢) {
    return "Convert" + ¢ + " to 0";
  }

  @Override @Nullable public ASTNode replacement(@NotNull final InfixExpression ¢) {
    if (¢.getOperator() != TIMES || !containsZero(¢) || isContainsSideEffect(¢))
      return null;
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("0");
    return $;
  }
}

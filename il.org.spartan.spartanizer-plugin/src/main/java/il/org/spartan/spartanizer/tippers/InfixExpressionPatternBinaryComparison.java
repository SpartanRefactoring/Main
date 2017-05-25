package il.org.spartan.spartanizer.tippers;

import il.org.spartan.spartanizer.ast.navigate.*;

/** matches {@code X1 <= X2}, {@code X1 < X2}, {@code X1 >= X2},
 * {@code X1 > X2}, {@code X1 == X2}, and {@code X1 != X2},
 * @author Yossi Gil
 * @since 2017-04-25 */
public abstract class InfixExpressionPatternBinaryComparison extends Binary {
  private static final long serialVersionUID = 1;

  public InfixExpressionPatternBinaryComparison() {
    andAlso("Operator is comparison", () -> op.isComparison(operator));
  }
}

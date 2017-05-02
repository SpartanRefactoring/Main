package il.org.spartan.spartanizer.tippers;

/** An {@link Multiciary} which has precisely two arguments, as is in
 * {@link InfixExpressionPatternBinaryComparison}
 * @author Yossi Gil
 * @since 2017-04-28 */
public abstract class Binary extends Multiciary {
  private static final long serialVersionUID = 1;

  public Binary() {
    andAlso("No extended arguments", () -> arity == 2);
  }
}

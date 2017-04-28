package il.org.spartan.spartanizer.tippers;

/** An {@link InfixExpressionPattern} which has precisely two arguments, as is in {@link InfixExpressionPatternBinaryComparison} 
 * 
 * @author Yossi Gil
 * @since 2017-04-28 */
public abstract class InfixExpressionPatternBinary extends InfixExpressionPattern {
  private static final long serialVersionUID = 1;

  public InfixExpressionPatternBinary() {
   andAlso("No extended arguments" , ()-> arity == 2);
  }

}

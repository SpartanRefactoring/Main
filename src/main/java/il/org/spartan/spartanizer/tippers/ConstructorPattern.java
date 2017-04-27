package il.org.spartan.spartanizer.tippers;

/** Constructor
 * @author Yossi Gil
 * @since 2017-04-22 */
public abstract class ConstructorPattern extends CallablePattern {
  public ConstructorPattern() {
    andAlso("Must be constructor ", () -> current.isConstructor());
  }

  private static final long serialVersionUID = 1;
}

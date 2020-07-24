package il.org.spartan.graph;

/** @author Yossi Gil
 * @since Apr 19, 2012 */
public abstract class AbstractSmallIntegersGraph {
  protected static short makeShort(final int ¢) {
    if (¢ < 0 || ¢ > Short.MAX_VALUE)
      throw new IllegalArgumentException();
    return (short) ¢;
  }

  public abstract short component(int i);

  public abstract short components();

  public final boolean connected(final int n1, final int n2) {
    return has(n1) && has(n2) && component((short) n1) == component((short) n2);
  }

  public abstract boolean has(int i);
}

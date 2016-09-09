/**
 *
 */
package il.org.spartan.graph;

/** @author Yossi Gil
 * @since Apr 19, 2012 */
public abstract class AbstractSmallIntegersGraph {
  protected static short makeShort(final int n) {
    if (n < 0 || n > Short.MAX_VALUE)
      throw new IllegalArgumentException();
    return (short) n;
  }

  public abstract short component(final int n);

  public abstract short components();

  public final boolean connected(final int n1, final int n2) {
    return has(n1) && has(n2) && component((short) n1) == component((short) n2);
  }

  public abstract boolean has(final int n);
}

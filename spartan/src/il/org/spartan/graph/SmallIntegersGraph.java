/**
 *
 */
package il.org.spartan.graph;

import il.org.spartan.collections.*;

/** @author Yossi Gil
 * @since Apr 19, 2012 */
public abstract class SmallIntegersGraph {
  public final int arcsCount;
  final short[] nodes;
  final short[] component;

  protected SmallIntegersGraph(final int arcsCount, final short[] nodes, final short[] component) {
    this.arcsCount = arcsCount;
    this.nodes = nodes;
    this.component = component;
  }

  public final short component(final int n) {
    return !has(n) ? -1 : component((short) n);
  }

  public final short components() {
    final Integers s = new Integers();
    for (final short n : nodes)
      s.add(component(n));
    return (short) s.size();
  }

  public final boolean connected(final int n1, final int n2) {
    return has(n1) && has(n2) && component((short) n1) == component((short) n2);
  }

  public abstract boolean has(final int n);

  public abstract boolean has(final int n1, final int n2);

  public final short nodesCount() {
    return (short) nodes.length;
  }

  protected final short component(final short n) {
    return component[n] < 0 ? n : (component[n] = component(component[n]));
  }
}

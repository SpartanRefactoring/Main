package il.org.spartan.graph;

import static java.util.Arrays.*;

import java.util.*;

/** @author Yossi Gil
 * @since Apr 19, 2012 */
public class UnionFindSmallIntegersGraph extends AbstractSmallIntegersGraph {
  private final BitSet nodes = new BitSet();
  private short component[] = new short[0];

  @Override public final short component(final int ¢) {
    return !has(¢) ? -1 : component((short) ¢);
  }
  @Override public final short components() {
    final BitSet $ = new BitSet();
    for (int ¢ = nodes.nextSetBit(0); ¢ >= 0; ¢ = nodes.nextSetBit(¢ + 1))
      $.set(¢);
    return (short) $.cardinality();
  }
  public void connect(final int i, final int j) {
    connect(makeShort(i), makeShort(j));
  }
  public void connect(final short i, final short j) {
    add(i);
    add(j);
    union(i, j);
  }
  @Override public boolean has(final int ¢) {
    add(¢);
    return true;
  }
  private void add(final int ¢) {
    add(makeShort(¢));
  }
  private void add(final short n) {
    final int m = component.length;
    if (n >= m) {
      component = copyOf(component, n + 1);
      Arrays.fill(component, m, component.length, (short) -1);
    }
    nodes.set(n);
  }
  private short find(final short n) {
    return component[n] < 0 ? n : (component[n] = find(component[n]));
  }
  private void union(final short n1, final short n2) {
    if (find(n1) != find(n2))
      component[find(n1)] = find(n2);
  }
}

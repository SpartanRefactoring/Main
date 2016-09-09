/**
 *
 */
package il.org.spartan.graph;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** An immutable undirected graph whose nodes are short integers in the range
 * 0..N where N is small, with the compact representation of an array of size N
 * of arrays of shorts.
 * @author Yossi Gil
 * @since Apr 18, 2012 */
public class BitSetSmallIntegersGraph extends SmallIntegersGraph {
  private final BitSet[] neighbors;

  private BitSetSmallIntegersGraph(final BitSet[] neighbors, final int arcsCount, final short[] component, final short[] nodes) {
    super(arcsCount, nodes, component);
    this.neighbors = neighbors;
  }

  @Override public boolean has(final int n) {
    return n >= 0 && n < neighbors.length && neighbors[n] != null;
  }

  @Override public boolean has(final int n1, final int n2) {
    return has(n1) && has(n2) && neighbors[n1].get(n2);
  }

  public BitSet neighbors(final int n) {
    return n < 0 || n >= neighbors.length ? null : (BitSet) neighbors[n].clone();
  }

  public short[] nodes() {
    return nodes.clone();
  }

  public static class Builder {
    private static short[] append(final short[] as, final short a) {
      if (Arrays.binarySearch(as, a) >= 0)
        return as;
      final short[] $ = copyOf(as, as.length + 1);
      $[$.length - 1] = a;
      sort($);
      return $;
    }

    private static short makeShort(final int n) {
      if (n < 0 || n > Short.MAX_VALUE)
        throw new IllegalArgumentException();
      return (short) n;
    }

    private BitSet[] neighbors = new BitSet[0];
    private short[] component = new short[0];
    private short[] nodes = new short[0];

    public Builder add(final int n) {
      return add(makeShort(n));
    }

    public Builder add(final short n) {
      final int m = neighbors.length;
      if (n >= m) {
        neighbors = copyOf(neighbors, n + 1);
        component = copyOf(component, n + 1);
        Arrays.fill(component, m, component.length, (short) -1);
      }
      nodes = append(nodes, n);
      neighbors[n] = Defaults.to(neighbors[n], new BitSet());
      return this;
    }

    public Builder connect(final int i, final int j) {
      return connect(makeShort(i), makeShort(j));
    }

    public Builder connect(final short i, final short j) {
      add(i).add(j);
      neighbors[i].set(j);
      neighbors[j].set(i);
      return union(i, j);
    }

    @SuppressWarnings("synthetic-access") //
    public BitSetSmallIntegersGraph go() {
      return new BitSetSmallIntegersGraph(neighbors, countArcs(), component, nodes);
    }

    private int countArcs() {
      int $ = 0;
      for (final BitSet ss : neighbors)
        if (ss != null)
          $ += ss.cardinality();
      return $ / 2;
    }

    private short find(final short n) {
      return component[n] < 0 ? n : (component[n] = find(component[n]));
    }

    private Builder union(final short n1, final short n2) {
      if (find(n1) != find(n2))
        component[find(n1)] = find(n2);
      return this;
    }
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    private static final int PSEUDO_ZERO = 2 * Short.MAX_VALUE + 2;

    @Test public void arsInsertedInNoOrder() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertEquals(3, g.arcsCount);
    }

    @Test public void connectedCheckDisconnectedNodes() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).add(1).go();
      assertFalse(g.connected(0, 1));
    }

    @Test public void connectedCheckRemoteNodes() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).connect(1, 2).go();
      assertTrue(g.connected(0, 2));
    }

    @Test public void connectedExists() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assertFalse(g.connected(2, 3));
    }

    @Test public void connectedOfPresentNodes() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assertTrue(g.connected(0, 1));
    }

    @Test public void connectedTwoChains() {
      final BitSetSmallIntegersGraph g = new Builder()//
          .connect(0, 1).connect(1, 2).connect(2, 3)//
          .connect(4, 5).connect(5, 6).connect(6, 7)//
          .go();
      assertTrue(g.connected(0, 2));
      assertTrue(g.connected(0, 1));
      assertTrue(g.connected(2, 1));
      assertTrue(g.connected(0, 3));
      assertTrue(g.connected(4, 7));
      assertTrue(g.connected(5, 7));
      assertTrue(g.connected(6, 7));
      assertTrue(g.connected(4, 7));
    }

    @Test public void containsArcFalse() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertFalse(g.has(13, 0));
    }

    @Test public void containsArcFirstLargeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(Short.MAX_VALUE + 1, 0));
    }

    @Test public void containsArcFirstNegativeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(-1, 0));
    }

    @Test public void containsArcSecondLargeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(0, Short.MAX_VALUE + 1));
    }

    @Test public void containsArcSecondLargeValueAfterInsertion1() {
      final BitSetSmallIntegersGraph g = new Builder().connect(1, 0).go();
      assertFalse(g.has(1, PSEUDO_ZERO));
    }

    @Test public void containsArcSecondLargeValueAfterInsertion2() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assertFalse(g.has(PSEUDO_ZERO, 0));
    }

    @Test public void containsArcSecondNegativeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(0, -1));
    }

    @Test public void containsArcTrue() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertTrue(g.has(13, 14));
    }

    @Test public void containsLargeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(Short.MAX_VALUE + 1));
    }

    @Test public void containsNegativeValue() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(-1));
    }

    @Test public void containsPseudoZero() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).go();
      assertFalse(g.has(PSEUDO_ZERO));
    }

    @Test public void disconnectedComponentsBuilder() {
      final Builder b = new Builder().add(5).add(6).add(7).add(8).add(9);
      assertEquals(-1, b.component[5]);
      assertEquals(-1, b.component[6]);
      assertEquals(-1, b.component[7]);
      assertEquals(-1, b.component[0]);
      assertEquals(-1, b.component[8]);
    }

    @Test public void disconnectedComponentsBuilderFreeNodes() {
      final Builder b = new Builder().add(5).add(6).add(7).add(8).add(9);
      assertEquals(-1, b.component[0]);
      assertEquals(-1, b.component[8]);
    }

    @Test public void disconnectedComponentsCount() {
      final Builder b = new Builder();
      b.add(5);
      b.add(6);
      b.add(7);
      b.add(9);
      assertEquals(4, b.go().components());
    }

    @Test public void disconnectedComponentsGraph() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(-1, g.component[5]);
      assertEquals(-1, g.component[6]);
      assertEquals(-1, g.component[7]);
      assertEquals(-1, g.component[0]);
      assertEquals(-1, g.component[8]);
    }

    @Test public void disconnectedComponentsGraphFreeNodes() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(-1, g.component[0]);
      assertEquals(-1, g.component[8]);
    }

    @Test public void disconnectedComponentsGraphFreeNodesFunction() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(0, g.component((short) 0));
      assertEquals(8, g.component((short) 8));
    }

    @Test public void disconnectedComponentsGraphFunction() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(5, g.component((short) 5));
      assertEquals(6, g.component((short) 6));
      assertEquals(7, g.component((short) 7));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesPublicFunctionCall() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(9).add(100).add(9).go();
      assertEquals(-1, g.component(8));
      assertEquals(-1, g.component(0));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesShortPrivateFunctionCall() {
      final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(8, g.component((short) 8));
      assertEquals(0, g.component((short) 0));
    }

    @Test public void edgeFind() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      assertEquals(0, b.find((short) 0));
      assertEquals(1, b.find((short) 1));
      b.connect(0, 1);
      assertEquals(1, b.find((short) 0));
      assertEquals(1, b.find((short) 1));
    }

    @Test public void emptyCreationArcsCount() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.arcsCount);
    }

    @Test public void emptyCreationNodesCount() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.nodesCount());
    }

    @Test public void emptyDoesNotContain() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(13));
    }

    @Test public void emptyDoesNotContainNegative() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(-1));
    }

    @Test public void fourEdgeFindIsTrimming() {
      final Builder b = new Builder();
      b.connect(0, 1);
      b.connect(1, 2);
      b.connect(2, 3);
      b.connect(3, 4);
      assertEquals(1, b.component[0]);
      assertEquals(2, b.component[1]);
      assertEquals(3, b.component[2]);
      assertEquals(4, b.component[3]);
      assertEquals(-1, b.component[4]);
      b.find((short) 0);
      assertEquals(4, b.component[0]);
      assertEquals(4, b.component[1]);
      assertEquals(4, b.component[2]);
      assertEquals(4, b.component[3]);
      assertEquals(-1, b.component[4]);
    }

    @Test public void hasThreeNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertEquals(3, g.neighbors(13).cardinality());
    }

    @Test public void illegalNodeNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      assertNull(g.neighbors(15));
    }

    @Test public void nastyReconnection() {
      final BitSetSmallIntegersGraph g = new Builder()//
          .connect(1, 2).connect(2, 3).connect(3, 4).connect(4, 5) //
          .connect(1, 5) // find(1) shall return 5 and will try to reconnect
          .go();
      assertEquals(5, g.component(5));
      assertEquals(5, g.component(4));
      assertEquals(5, g.component(3));
      assertEquals(5, g.component(2));
      assertEquals(5, g.component(1));
    }

    @Test(timeout = 20) public void neighborsIsNotNull() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertNotNull(g.neighbors(13));
    }

    @Test public void newArcReturnsThis() {
      final Builder b = new Builder();
      assertSame(b, b.connect(13, 14));
    }

    public void newNodeIdempotent() {
      assertEquals(1, new Builder().add(5).add(5).add(5).go().nodesCount());
    }

    @Test(expected = IllegalArgumentException.class) public void newNodeLargeNumber() {
      new Builder().add(Short.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class) public void newNodeNegative() {
      new Builder().add(-1);
    }

    @Test public void newNodeReturnsThis() {
      final Builder b = new Builder();
      assertSame(b, b.add(13));
    }

    @Test public void noComponents() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.components());
    }

    @Test public void nodesEmpty() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.nodes().length);
    }

    @Test public void nodesExists() {
      final BitSetSmallIntegersGraph g = new Builder().go();
      assertNotNull(g.nodes());
    }

    @Test public void nodesResistChange() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      g.nodes()[0] = 5;
      assertEquals(1, g.nodes()[0]);
      assertEquals(2, g.nodes()[1]);
      assertEquals(3, g.nodes()[2]);
      assertEquals(4, g.nodes()[3]);
    }

    @Test public void nodesSingle() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      assertEquals(1, g.nodes().length);
    }

    @Test public void nodesSingleCorrect() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      assertEquals(1, g.nodes()[0]);
    }

    @Test public void nodesTwoCorrect() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).add(2).go();
      assertEquals(1, g.nodes()[0]);
      assertEquals(2, g.nodes()[1]);
    }

    @Test public void nodesUnsortedCorrect() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      assertEquals(1, g.nodes()[0]);
      assertEquals(2, g.nodes()[1]);
      assertEquals(3, g.nodes()[2]);
      assertEquals(4, g.nodes()[3]);
    }

    @Test public void noNullCreation() {
      assertNotNull(new Builder().go());
    }

    @Test public void oneComponentOfOneEdge() {
      final BitSetSmallIntegersGraph g = new Builder().connect(1, 2).go();
      assertEquals(1, g.components());
    }

    @Test public void oneComponents() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      assertEquals(1, g.components());
    }

    @Test public void safeModifictation() {
      final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      g.neighbors(0).clear(1);
      assertTrue(g.has(0, 1));
    }

    @Test public void selfEdgeComponent() {
      final Builder b = new Builder();
      b.connect(5, 5);
      assertEquals(-1, b.component[5]);
    }

    @Test public void selfEdgeFind() {
      final Builder b = new Builder();
      b.connect(5, 5);
      assertEquals(5, b.find((short) 5));
    }

    @Test public void simpleFind() {
      final Builder b = new Builder();
      b.add(0);
      assertEquals(0, b.find((short) 0));
    }

    @Test public void simpleNode0ContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).go();
      assertTrue(g.has(0));
    }

    @Test public void simpleNode1ContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(1).go();
      assertTrue(g.has(1));
    }

    @Test public void simpleTwoNodeContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(0).add(1).go();
      assertTrue(g.has(0));
      assertTrue(g.has(1));
    }

    @Test public void singleArcCountArcs() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(1, g.arcsCount);
    }

    @Test public void singleArcCountNodes() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(2, g.nodesCount());
    }

    @Test public void singleArcHasOneNeighbor() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(1, g.neighbors(13).cardinality());
    }

    @Test public void singleArcInsertedTwiceCountArcs() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 14).go();
      assertEquals(1, g.arcsCount);
    }

    @Test public void singleArcInverseHasOneNeighbor() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(1, g.neighbors(14).cardinality());
    }

    @Test public void singleEdgeCheckComponent() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      b.connect(0, 1);
      final BitSetSmallIntegersGraph g = b.go();
      assertEquals(1, g.component((short) 0));
      assertEquals(1, g.component((short) 1));
    }

    @Test public void singleNodeContainsFalse() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assertFalse(g.has(14));
    }

    @Test public void singleNodeContainsFalseBelow() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assertFalse(g.has(11));
    }

    @Test public void singleNodeContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assertTrue(g.has(13));
    }

    @Test public void singleNodeCountArcs() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assertEquals(0, g.arcsCount);
    }

    @Test public void singleNodeCountNodes() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assertEquals(1, g.nodesCount());
    }

    @Test public void singleNodeHasNoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).go();
      assertEquals(0, g.neighbors(13).cardinality());
    }

    @Test public void triangleAndPath() {
      final BitSetSmallIntegersGraph g = new Builder()//
          .connect(1, 2).connect(2, 3).connect(3, 4) //
          .connect(7, 8).connect(5, 6).connect(6, 7) //
          .go();
      assertEquals(2, g.components());
    }

    @Test public void triangleHasTwoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      assertEquals(2, g.neighbors(14).cardinality());
      assertEquals(2, g.neighbors(13).cardinality());
      assertEquals(2, g.neighbors(5).cardinality());
    }

    @Test public void twoArcsHasTwoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      assertEquals(2, g.neighbors(13).cardinality());
    }

    @Test public void twoArcsInverseHasTwoNeighbors() {
      final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      assertEquals(1, g.neighbors(14).cardinality());
      assertEquals(1, g.neighbors(15).cardinality());
    }

    @Test public void twoEdgeComponent() {
      final Builder b = new Builder();
      b.connect(0, 1);
      b.connect(1, 2);
      assertEquals(1, b.component[0]);
      assertEquals(2, b.component[1]);
      assertEquals(2, b.component[1]);
    }

    @Test public void twoEdgeFind() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      assertEquals(0, b.find((short) 0));
      assertEquals(1, b.find((short) 1));
      b.connect(0, 1);
      assertEquals(1, b.find((short) 0));
      assertEquals(1, b.find((short) 1));
      b.connect(1, 2);
      assertEquals(2, b.find((short) 0));
      assertEquals(2, b.find((short) 1));
      assertEquals(2, b.find((short) 2));
    }

    @Test public void twoNodeContainsTrue() {
      final BitSetSmallIntegersGraph g = new Builder().add(13).add(14).go();
      assertTrue(g.has(13));
      assertTrue(g.has(14));
    }
  }
}
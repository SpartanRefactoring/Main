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
public class MatrixSmallIntegersGraph extends SmallIntegersGraph {
  private final short[][] neighbors;

  private MatrixSmallIntegersGraph(final short[][] neighbors, final int arcsCount, final short[] component, final short[] nodes) {
    super(arcsCount, nodes, component);
    this.neighbors = neighbors;
  }

  @Override public boolean has(final int n) {
    return n >= 0 && n < neighbors.length && neighbors[n] != null;
  }

  @Override public boolean has(final int n1, final int n2) {
    return has(n1) && has(n2) && binarySearch(neighbors[n1], (short) n2) >= 0;
  }

  public short[] neighbors(final int n) {
    return n < 0 || n >= neighbors.length ? null : neighbors[n].clone();
  }

  public short[] nodes() {
    return nodes.clone();
  }

  public static class Builder {
    private static final short[] noNeighbors = new short[0];

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

    private short[][] neighbors = new short[0][];
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
      neighbors[n] = Defaults.to(neighbors[n], noNeighbors);
      return this;
    }

    public Builder connect(final int i, final int j) {
      return connect(makeShort(i), makeShort(j));
    }

    public Builder connect(final short i, final short j) {
      return add(i).add(j).append(i, j).append(j, i).union(i, j);
    }

    @SuppressWarnings("synthetic-access") //
    public MatrixSmallIntegersGraph go() {
      return new MatrixSmallIntegersGraph(neighbors, countArcs(), component, nodes);
    }

    private Builder append(final short i, final short j) {
      neighbors[i] = append(neighbors[i], j);
      return this;
    }

    private int countArcs() {
      int $ = 0;
      for (final short[] ss : neighbors)
        if (ss != null)
          $ += ss.length;
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
      final SmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertEquals(3, g.arcsCount);
    }

    @Test public void connectedCheckDisconnectedNodes() {
      final SmallIntegersGraph g = new Builder().add(0).add(1).go();
      assertFalse(g.connected(0, 1));
    }

    @Test public void connectedCheckRemoteNodes() {
      final SmallIntegersGraph g = new Builder().connect(0, 1).connect(1, 2).go();
      assertTrue(g.connected(0, 2));
    }

    @Test public void connectedExists() {
      final SmallIntegersGraph g = new Builder().connect(0, 1).go();
      assertFalse(g.connected(2, 3));
    }

    @Test public void connectedOfPresentNodes() {
      final SmallIntegersGraph g = new Builder().connect(0, 1).go();
      assertTrue(g.connected(0, 1));
    }

    @Test public void connectedTwoChains() {
      final SmallIntegersGraph g = new Builder()//
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
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertFalse(g.has(13, 0));
    }

    @Test public void containsArcFirstLargeValue() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(Short.MAX_VALUE + 1, 0));
    }

    @Test public void containsArcFirstNegativeValue() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(-1, 0));
    }

    @Test public void containsArcSecondLargeValue() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(0, Short.MAX_VALUE + 1));
    }

    @Test public void containsArcSecondLargeValueAfterInsertion1() {
      final MatrixSmallIntegersGraph g = new Builder().connect(1, 0).go();
      assertFalse(g.has(1, PSEUDO_ZERO));
    }

    @Test public void containsArcSecondLargeValueAfterInsertion2() {
      final MatrixSmallIntegersGraph g = new Builder().connect(0, 1).go();
      assertFalse(g.has(PSEUDO_ZERO, 0));
    }

    @Test public void containsArcSecondNegativeValue() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(0, -1));
    }

    @Test public void containsArcTrue() {
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertTrue(g.has(13, 14));
    }

    @Test public void containsLargeValue() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(Short.MAX_VALUE + 1));
    }

    @Test public void containsNegativeValue() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(-1));
    }

    @Test public void containsPseudoZero() {
      final MatrixSmallIntegersGraph g = new Builder().add(0).go();
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
      final MatrixSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(-1, g.component[5]);
      assertEquals(-1, g.component[6]);
      assertEquals(-1, g.component[7]);
      assertEquals(-1, g.component[0]);
      assertEquals(-1, g.component[8]);
    }

    @Test public void disconnectedComponentsGraphFreeNodes() {
      final MatrixSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(-1, g.component[0]);
      assertEquals(-1, g.component[8]);
    }

    @Test public void disconnectedComponentsGraphFreeNodesFunction() {
      final SmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(0, g.component((short) 0));
      assertEquals(8, g.component((short) 8));
    }

    @Test public void disconnectedComponentsGraphFunction() {
      final SmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      assertEquals(5, g.component((short) 5));
      assertEquals(6, g.component((short) 6));
      assertEquals(7, g.component((short) 7));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesPublicFunctionCall() {
      final SmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(9).add(100).add(9).go();
      assertEquals(-1, g.component(8));
      assertEquals(-1, g.component(0));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesShortPrivateFunctionCall() {
      final SmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
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
      final SmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.arcsCount);
    }

    @Test public void emptyCreationNodesCount() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.nodesCount());
    }

    @Test public void emptyDoesNotContain() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertFalse(g.has(13));
    }

    @Test public void emptyDoesNotContainNegative() {
      final MatrixSmallIntegersGraph g = new Builder().go();
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
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
      assertEquals(3, g.neighbors(13).length);
    }

    @Test public void illegalNodeNeighbors() {
      final MatrixSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      assertNull(g.neighbors(15));
    }

    @Test public void nastyReconnection() {
      final SmallIntegersGraph g = new Builder()//
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
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go();
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
      final SmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.components());
    }

    @Test public void nodesEmpty() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertEquals(0, g.nodes().length);
    }

    @Test public void nodesExists() {
      final MatrixSmallIntegersGraph g = new Builder().go();
      assertNotNull(g.nodes());
    }

    @Test public void nodesResistChange() {
      final MatrixSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      g.nodes()[0] = 5;
      assertEquals(1, g.nodes()[0]);
      assertEquals(2, g.nodes()[1]);
      assertEquals(3, g.nodes()[2]);
      assertEquals(4, g.nodes()[3]);
    }

    @Test public void nodesSingle() {
      final MatrixSmallIntegersGraph g = new Builder().add(1).go();
      assertEquals(1, g.nodes().length);
    }

    @Test public void nodesSingleCorrect() {
      final MatrixSmallIntegersGraph g = new Builder().add(1).go();
      assertEquals(1, g.nodes()[0]);
    }

    @Test public void nodesTwoCorrect() {
      final MatrixSmallIntegersGraph g = new Builder().add(1).add(2).go();
      assertEquals(1, g.nodes()[0]);
      assertEquals(2, g.nodes()[1]);
    }

    @Test public void nodesUnsortedCorrect() {
      final MatrixSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      assertEquals(1, g.nodes()[0]);
      assertEquals(2, g.nodes()[1]);
      assertEquals(3, g.nodes()[2]);
      assertEquals(4, g.nodes()[3]);
    }

    @Test public void noNullCreation() {
      assertNotNull(new Builder().go());
    }

    @Test public void oneComponentOfOneEdge() {
      final SmallIntegersGraph g = new Builder().connect(1, 2).go();
      assertEquals(1, g.components());
    }

    @Test public void oneComponents() {
      final SmallIntegersGraph g = new Builder().add(1).go();
      assertEquals(1, g.components());
    }

    @Test public void safeModifictation() {
      final MatrixSmallIntegersGraph g = new Builder().connect(0, 1).go();
      g.neighbors(0)[0] = 5;
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
      final MatrixSmallIntegersGraph g = new Builder().add(0).go();
      assertTrue(g.has(0));
    }

    @Test public void simpleNode1ContainsTrue() {
      final MatrixSmallIntegersGraph g = new Builder().add(1).go();
      assertTrue(g.has(1));
    }

    @Test public void simpleTwoNodeContainsTrue() {
      final MatrixSmallIntegersGraph g = new Builder().add(0).add(1).go();
      assertTrue(g.has(0));
      assertTrue(g.has(1));
    }

    @Test public void singleArcCountArcs() {
      final SmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(1, g.arcsCount);
    }

    @Test public void singleArcCountNodes() {
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(2, g.nodesCount());
    }

    @Test public void singleArcHasOneNeighbor() {
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(1, g.neighbors(13).length);
    }

    @Test public void singleArcInsertedTwiceCountArcs() {
      final SmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 14).go();
      assertEquals(1, g.arcsCount);
    }

    @Test public void singleArcInverseHasOneNeighbor() {
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).go();
      assertEquals(1, g.neighbors(14).length);
    }

    @Test public void singleEdgeCheckComponent() {
      final Builder b = new Builder();
      b.add(0);
      b.add(1);
      b.connect(0, 1);
      final SmallIntegersGraph g = b.go();
      assertEquals(1, g.component((short) 0));
      assertEquals(1, g.component((short) 1));
    }

    @Test public void singleNodeContainsFalse() {
      final MatrixSmallIntegersGraph g = new Builder().add(13).go();
      assertFalse(g.has(14));
    }

    @Test public void singleNodeContainsFalseBelow() {
      final MatrixSmallIntegersGraph g = new Builder().add(13).go();
      assertFalse(g.has(11));
    }

    @Test public void singleNodeContainsTrue() {
      final MatrixSmallIntegersGraph g = new Builder().add(13).go();
      assertTrue(g.has(13));
    }

    @Test public void singleNodeCountArcs() {
      final SmallIntegersGraph g = new Builder().add(13).go();
      assertEquals(0, g.arcsCount);
    }

    @Test public void singleNodeCountNodes() {
      final MatrixSmallIntegersGraph g = new Builder().add(13).go();
      assertEquals(1, g.nodesCount());
    }

    @Test public void singleNodeHasNoNeighbors() {
      final MatrixSmallIntegersGraph g = new Builder().add(13).go();
      assertEquals(0, g.neighbors(13).length);
    }

    @Test public void triangleAndPath() {
      final SmallIntegersGraph g = new Builder()//
          .connect(1, 2).connect(2, 3).connect(3, 4) //
          .connect(7, 8).connect(5, 6).connect(6, 7) //
          .go();
      assertEquals(2, g.components());
    }

    @Test public void triangleHasTwoNeighbors() {
      final MatrixSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      assertEquals(2, g.neighbors(14).length);
      assertEquals(2, g.neighbors(13).length);
      assertEquals(2, g.neighbors(5).length);
    }

    @Test public void twoArcsHasTwoNeighbors() {
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      assertEquals(2, g.neighbors(13).length);
    }

    @Test public void twoArcsInverseHasTwoNeighbors() {
      final MatrixSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      assertEquals(1, g.neighbors(14).length);
      assertEquals(1, g.neighbors(15).length);
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
      final MatrixSmallIntegersGraph g = new Builder().add(13).add(14).go();
      assertTrue(g.has(13));
      assertTrue(g.has(14));
    }
  }
}
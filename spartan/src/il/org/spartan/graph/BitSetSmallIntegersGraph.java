package il.org.spartan.graph;

import static fluent.ly.azzert.*;
import static java.util.Arrays.*;
import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;

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

  @Override public boolean has(final int ¢) {
    return ¢ >= 0 && ¢ < neighbors.length && neighbors[¢] != null;
  }

  @Override public boolean has(final int n1, final int n2) {
    return has(n1) && has(n2) && neighbors[n1].get(n2);
  }

  @Nullable public BitSet neighbors(final int ¢) {
    return ¢ < 0 || ¢ >= neighbors.length ? null : (BitSet) neighbors[¢].clone();
  }

  public short[] nodes() {
    return nodes.clone();
  }

  public static class Builder {
    @NotNull private static short[] append(@NotNull final short[] as, final short a) {
      if (Arrays.binarySearch(as, a) >= 0)
        return as;
      @NotNull final short[] $ = copyOf(as, as.length + 1);
      $[$.length - 1] = a;
      sort($);
      return $;
    }

    private static short makeShort(final int ¢) {
      if (¢ < 0 || ¢ > Short.MAX_VALUE)
        throw new IllegalArgumentException();
      return (short) ¢;
    }

    @NotNull private BitSet[] neighbors = new BitSet[0];
    @NotNull private short[] component = new short[0];
    @NotNull private short[] nodes = new short[0];

    @NotNull public Builder add(final int ¢) {
      return add(makeShort(¢));
    }

    @NotNull public Builder add(final short n) {
      final int m = neighbors.length;
      if (n >= m) {
        neighbors = copyOf(neighbors, n + 1);
        component = copyOf(component, n + 1);
        Arrays.fill(component, m, component.length, (short) -1);
      }
      nodes = append(nodes, n);
      neighbors[n] = defaults.to(neighbors[n], new BitSet());
      return this;
    }

    @NotNull public Builder connect(final int i, final int j) {
      return connect(makeShort(i), makeShort(j));
    }

    @NotNull public Builder connect(final short i, final short j) {
      add(i).add(j);
      neighbors[i].set(j);
      neighbors[j].set(i);
      return union(i, j);
    }

    @NotNull @SuppressWarnings("synthetic-access") //
    public BitSetSmallIntegersGraph go() {
      return new BitSetSmallIntegersGraph(neighbors, countArcs(), component, nodes);
    }

    private int countArcs() {
      int $ = 0;
      for (@Nullable final BitSet ¢ : neighbors)
        if (¢ != null)
          $ += ¢.cardinality();
      return $ / 2;
    }

    private short find(final short n) {
      return component[n] < 0 ? n : (component[n] = find(component[n]));
    }

    @NotNull private Builder union(final short n1, final short n2) {
      if (find(n1) != find(n2))
        component[find(n1)] = find(n2);
      return this;
    }
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    private static final int PSEUDO_ZERO = 2 * Short.MAX_VALUE + 2;

    @Test public void arsInsertedInNoOrder() {
      azzert.that(new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go().arcsCount, is(3));
    }

    @Test public void connectedCheckDisconnectedNodes() {
      assert !new Builder().add(0).add(1).go().connected(0, 1);
    }

    @Test public void connectedCheckRemoteNodes() {
      assert new Builder().connect(0, 1).connect(1, 2).go().connected(0, 2);
    }

    @Test public void connectedExists() {
      assert !new Builder().connect(0, 1).go().connected(2, 3);
    }

    @Test public void connectedOfPresentNodes() {
      assert new Builder().connect(0, 1).go().connected(0, 1);
    }

    @Test public void connectedTwoChains() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder()//
          .connect(0, 1).connect(1, 2).connect(2, 3)//
          .connect(4, 5).connect(5, 6).connect(6, 7)//
          .go();
      assert g.connected(0, 2);
      assert g.connected(0, 1);
      assert g.connected(2, 1);
      assert g.connected(0, 3);
      assert g.connected(4, 7);
      assert g.connected(5, 7);
      assert g.connected(6, 7);
      assert g.connected(4, 7);
    }

    @Test public void containsArcFalse() {
      assert !new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go().has(13, 0);
    }

    @Test public void containsArcFirstLargeValue() {
      assert !new Builder().go().has(Short.MAX_VALUE + 1, 0);
    }

    @Test public void containsArcFirstNegativeValue() {
      assert !new Builder().go().has(-1, 0);
    }

    @Test public void containsArcSecondLargeValue() {
      assert !new Builder().go().has(0, Short.MAX_VALUE + 1);
    }

    @Test public void containsArcSecondLargeValueAfterInsertion1() {
      assert !new Builder().connect(1, 0).go().has(1, PSEUDO_ZERO);
    }

    @Test public void containsArcSecondLargeValueAfterInsertion2() {
      assert !new Builder().connect(0, 1).go().has(PSEUDO_ZERO, 0);
    }

    @Test public void containsArcSecondNegativeValue() {
      assert !new Builder().go().has(0, -1);
    }

    @Test public void containsArcTrue() {
      assert new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go().has(13, 14);
    }

    @Test public void containsLargeValue() {
      assert !new Builder().go().has(Short.MAX_VALUE + 1);
    }

    @Test public void containsNegativeValue() {
      assert !new Builder().go().has(-1);
    }

    @Test public void containsPseudoZero() {
      assert !new Builder().add(0).go().has(PSEUDO_ZERO);
    }

    @Test public void disconnectedComponentsBuilder() {
      @NotNull final Builder b = new Builder().add(5).add(6).add(7).add(8).add(9);
      azzert.that(b.component[5], is(-1));
      azzert.that(b.component[6], is(-1));
      azzert.that(b.component[7], is(-1));
      azzert.that(b.component[0], is(-1));
      azzert.that(b.component[8], is(-1));
    }

    @Test public void disconnectedComponentsBuilderFreeNodes() {
      @NotNull final Builder b = new Builder().add(5).add(6).add(7).add(8).add(9);
      azzert.that(b.component[0], is(-1));
      azzert.that(b.component[8], is(-1));
    }

    @Test public void disconnectedComponentsCount() {
      @NotNull final Builder b = new Builder();
      b.add(5);
      b.add(6);
      b.add(7);
      b.add(9);
      azzert.that(b.go().components(), is(4));
    }

    @Test public void disconnectedComponentsGraph() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component[5], is(-1));
      azzert.that(g.component[6], is(-1));
      azzert.that(g.component[7], is(-1));
      azzert.that(g.component[0], is(-1));
      azzert.that(g.component[8], is(-1));
    }

    @Test public void disconnectedComponentsGraphFreeNodes() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component[0], is(-1));
      azzert.that(g.component[8], is(-1));
    }

    @Test public void disconnectedComponentsGraphFreeNodesFunction() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component((short) 0), is(0));
      azzert.that(g.component((short) 8), is(8));
    }

    @Test public void disconnectedComponentsGraphFunction() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component((short) 5), is(5));
      azzert.that(g.component((short) 6), is(6));
      azzert.that(g.component((short) 7), is(7));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesPublicFunctionCall() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(9).add(100).add(9).go();
      azzert.that(g.component(8), is(-1));
      azzert.that(g.component(0), is(-1));
    }

    @Test public void disconnectedComponentsGraphFunctionNotFoundNodesShortPrivateFunctionCall() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(5).add(6).add(7).add(8).add(9).go();
      azzert.that(g.component((short) 8), is(8));
      azzert.that(g.component((short) 0), is(0));
    }

    @Test public void edgeFind() {
      @NotNull final Builder b = new Builder();
      b.add(0);
      b.add(1);
      azzert.that(b.find((short) 0), is(0));
      azzert.that(b.find((short) 1), is(1));
      b.connect(0, 1);
      azzert.that(b.find((short) 0), is(1));
      azzert.that(b.find((short) 1), is(1));
    }

    @Test public void emptyCreationArcsCount() {
      azzert.that(new Builder().go().arcsCount, is(0));
    }

    @Test public void emptyCreationNodesCount() {
      azzert.that(new Builder().go().nodesCount(), is(0));
    }

    @Test public void emptyDoesNotContain() {
      assert !new Builder().go().has(13);
    }

    @Test public void emptyDoesNotContainNegative() {
      assert !new Builder().go().has(-1);
    }

    @Test public void fourEdgeFindIsTrimming() {
      @NotNull final Builder b = new Builder();
      b.connect(0, 1);
      b.connect(1, 2);
      b.connect(2, 3);
      b.connect(3, 4);
      azzert.that(b.component[0], is(1));
      azzert.that(b.component[1], is(2));
      azzert.that(b.component[2], is(3));
      azzert.that(b.component[3], is(4));
      azzert.that(b.component[4], is(-1));
      b.find((short) 0);
      azzert.that(b.component[0], is(4));
      azzert.that(b.component[1], is(4));
      azzert.that(b.component[2], is(4));
      azzert.that(b.component[3], is(4));
      azzert.that(b.component[4], is(-1));
    }

    @Test public void hasThreeNeighbors() {
      azzert.that(new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go().neighbors(13).cardinality(), is(3));
    }

    @Test public void illegalNodeNeighbors() {
      azzert.isNull(new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go().neighbors(15));
    }

    @Test public void nastyReconnection() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder()//
          .connect(1, 2).connect(2, 3).connect(3, 4).connect(4, 5) //
          .connect(1, 5) // find(1) shall return 5 and will try to reconnect
          .go();
      azzert.that(g.component(5), is(5));
      azzert.that(g.component(4), is(5));
      azzert.that(g.component(3), is(5));
      azzert.that(g.component(2), is(5));
      azzert.that(g.component(1), is(5));
    }

    @Test(timeout = 20) public void neighborsIsNotNull() {
      assert new Builder().connect(13, 14).connect(13, 15).connect(13, 12).go().neighbors(13) != null;
    }

    @Test public void newArcReturnsThis() {
      @NotNull final Builder b = new Builder();
      assertSame(b, b.connect(13, 14));
    }

    public void newNodeIdempotent() {
      azzert.that(new Builder().add(5).add(5).add(5).go().nodesCount(), is(1));
    }

    @Test(expected = IllegalArgumentException.class) public void newNodeLargeNumber() {
      new Builder().add(Short.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class) public void newNodeNegative() {
      new Builder().add(-1);
    }

    @Test public void newNodeReturnsThis() {
      @NotNull final Builder b = new Builder();
      assertSame(b, b.add(13));
    }

    @Test public void noComponents() {
      azzert.that(new Builder().go().components(), is(0));
    }

    @Test public void nodesEmpty() {
      azzert.that(new Builder().go().nodes().length, is(0));
    }

    @Test public void nodesExists() {
      assert new Builder().go().nodes() != null;
    }

    @Test public void nodesResistChange() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      g.nodes()[0] = 5;
      azzert.that(g.nodes()[0], is(1));
      azzert.that(g.nodes()[1], is(2));
      azzert.that(g.nodes()[2], is(3));
      azzert.that(g.nodes()[3], is(4));
    }

    @Test public void nodesSingle() {
      azzert.that(new Builder().add(1).go().nodes().length, is(1));
    }

    @Test public void nodesSingleCorrect() {
      azzert.that(new Builder().add(1).go().nodes()[0], is(1));
    }

    @Test public void nodesTwoCorrect() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(1).add(2).go();
      azzert.that(g.nodes()[0], is(1));
      azzert.that(g.nodes()[1], is(2));
    }

    @Test public void nodesUnsortedCorrect() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(1).add(3).add(4).add(2).go();
      azzert.that(g.nodes()[0], is(1));
      azzert.that(g.nodes()[1], is(2));
      azzert.that(g.nodes()[2], is(3));
      azzert.that(g.nodes()[3], is(4));
    }

    @Test public void noNullCreation() {
      assert new Builder().go() != null;
    }

    @Test public void oneComponentOfOneEdge() {
      azzert.that(new Builder().connect(1, 2).go().components(), is(1));
    }

    @Test public void oneComponents() {
      azzert.that(new Builder().add(1).go().components(), is(1));
    }

    @Test public void safeModifictation() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().connect(0, 1).go();
      g.neighbors(0).clear(1);
      assert g.has(0, 1);
    }

    @Test public void selfEdgeComponent() {
      @NotNull final Builder b = new Builder();
      b.connect(5, 5);
      azzert.that(b.component[5], is(-1));
    }

    @Test public void selfEdgeFind() {
      @NotNull final Builder b = new Builder();
      b.connect(5, 5);
      azzert.that(b.find((short) 5), is(5));
    }

    @Test public void simpleFind() {
      @NotNull final Builder b = new Builder();
      b.add(0);
      azzert.that(b.find((short) 0), is(0));
    }

    @Test public void simpleNode0ContainsTrue() {
      assert new Builder().add(0).go().has(0);
    }

    @Test public void simpleNode1ContainsTrue() {
      assert new Builder().add(1).go().has(1);
    }

    @Test public void simpleTwoNodeContainsTrue() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(0).add(1).go();
      assert g.has(0);
      assert g.has(1);
    }

    @Test public void singleArcCountArcs() {
      azzert.that(new Builder().connect(13, 14).go().arcsCount, is(1));
    }

    @Test public void singleArcCountNodes() {
      azzert.that(new Builder().connect(13, 14).go().nodesCount(), is(2));
    }

    @Test public void singleArcHasOneNeighbor() {
      azzert.that(new Builder().connect(13, 14).go().neighbors(13).cardinality(), is(1));
    }

    @Test public void singleArcInsertedTwiceCountArcs() {
      azzert.that(new Builder().connect(13, 14).connect(13, 14).go().arcsCount, is(1));
    }

    @Test public void singleArcInverseHasOneNeighbor() {
      azzert.that(new Builder().connect(13, 14).go().neighbors(14).cardinality(), is(1));
    }

    @Test public void singleEdgeCheckComponent() {
      @NotNull final Builder b = new Builder();
      b.add(0);
      b.add(1);
      b.connect(0, 1);
      @NotNull final BitSetSmallIntegersGraph g = b.go();
      azzert.that(g.component((short) 0), is(1));
      azzert.that(g.component((short) 1), is(1));
    }

    @Test public void singleNodeContainsFalse() {
      assert !new Builder().add(13).go().has(14);
    }

    @Test public void singleNodeContainsFalseBelow() {
      assert !new Builder().add(13).go().has(11);
    }

    @Test public void singleNodeContainsTrue() {
      assert new Builder().add(13).go().has(13);
    }

    @Test public void singleNodeCountArcs() {
      azzert.that(new Builder().add(13).go().arcsCount, is(0));
    }

    @Test public void singleNodeCountNodes() {
      azzert.that(new Builder().add(13).go().nodesCount(), is(1));
    }

    @Test public void singleNodeHasNoNeighbors() {
      azzert.that(new Builder().add(13).go().neighbors(13).cardinality(), is(0));
    }

    @Test public void triangleAndPath() {
      azzert.that(new Builder().connect(1, 2).connect(2, 3).connect(3, 4).connect(7, 8).connect(5, 6).connect(6, 7).go().components(), is(2));
    }

    @Test public void triangleHasTwoNeighbors() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().connect(5, 14).connect(5, 13).connect(13, 14).go();
      azzert.that(g.neighbors(14).cardinality(), is(2));
      azzert.that(g.neighbors(13).cardinality(), is(2));
      azzert.that(g.neighbors(5).cardinality(), is(2));
    }

    @Test public void twoArcsHasTwoNeighbors() {
      azzert.that(new Builder().connect(13, 14).connect(13, 15).go().neighbors(13).cardinality(), is(2));
    }

    @Test public void twoArcsInverseHasTwoNeighbors() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().connect(13, 14).connect(13, 15).go();
      azzert.that(g.neighbors(14).cardinality(), is(1));
      azzert.that(g.neighbors(15).cardinality(), is(1));
    }

    @Test public void twoEdgeComponent() {
      @NotNull final Builder b = new Builder();
      b.connect(0, 1);
      b.connect(1, 2);
      azzert.that(b.component[0], is(1));
      azzert.that(b.component[1], is(2));
      azzert.that(b.component[1], is(2));
    }

    @Test public void twoEdgeFind() {
      @NotNull final Builder b = new Builder();
      b.add(0);
      b.add(1);
      azzert.that(b.find((short) 0), is(0));
      azzert.that(b.find((short) 1), is(1));
      b.connect(0, 1);
      azzert.that(b.find((short) 0), is(1));
      azzert.that(b.find((short) 1), is(1));
      b.connect(1, 2);
      azzert.that(b.find((short) 0), is(2));
      azzert.that(b.find((short) 1), is(2));
      azzert.that(b.find((short) 2), is(2));
    }

    @Test public void twoNodeContainsTrue() {
      @NotNull final BitSetSmallIntegersGraph g = new Builder().add(13).add(14).go();
      assert g.has(13);
      assert g.has(14);
    }
  }
}
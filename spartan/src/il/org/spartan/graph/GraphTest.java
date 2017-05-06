package il.org.spartan.graph;

import static fluent.ly.azzert.*;
import static il.org.spartan.graph.GraphsSamplesGenerator.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spatan.iteration.*;

@SuppressWarnings("static-method")
public class GraphTest {
  static void verifyEdge( final Graph<String> s, final int from, final int to) {
    assert s.vertices().get(from) != null;
    assert s.vertices().get(to) != null;
    assert s.vertices().get(from).outgoing() != null;
    assert s.vertices().get(to).incoming() != null;
    verifyFound(s.vertices().get(from).outgoing(), s.vertices().get(to));
    verifyFound(s.vertices().get(to).incoming(), s.vertices().get(from));
  }
  static void verifyEdge( final Graph<String> s, final String from, final String to) {
    verifyEdge(s, index(s, from), index(s, to));
  }
  static void verifyGraph( final Graph<String> ¢) {
    verifyVertices(¢);
    verifySources(¢);
    verifySinks(¢);
    verifyPreorder(¢);
  }
  static void verifyGraphsEquivlanet( final Graph<String> g1,  final Graph<String> g2) {
    assertEquals(g1.size(), g2.size());
    if (!g1.isEmpty())
      for ( final Vertex<String> sv : g1.vertices()) {
        final Vertex<String> v = g2.vertex(sv.e());
        assertEquals(sv.e(), v.e());
        assertEquals(sv.incoming().size(), v.incoming().size());
        for ( final Vertex<String> incomingIndex : sv.incoming())
          verifyEdge(g2, incomingIndex.e(), sv.e());
        assertEquals(sv.outgoing().size(), v.outgoing().size());
        for ( final Vertex<String> outgoingIndex : sv.outgoing())
          verifyEdge(g2, v.e(), outgoingIndex.e());
      }
  }
  static void verifyPreorder( final Graph<String> s) {
    verifyCollection(s, s.preOrder(), s.vertices().size(), λ -> verifyVertex(s, λ.e()));
  }
  static void verifySink( final Graph<String> s, final String sink) {
    verifySink(s, s.vertex(sink));
  }
  static void verifySink( final Graph<String> s,  final Vertex<String> v) {
    assertEquals(0, s.outgoing(v).size());
  }
  static void verifySinks( final Graph<String> s) {
    verifyCollection(s, s.sinks(), s.sinksCount(), λ -> verifySink(s, λ));
  }
  static void verifySource( final Graph<String> s, final String source) {
    verifySource(s, s.vertex(source));
  }
  static void verifySource( final Graph<String> s,  final Vertex<String> v) {
    assert s.isSource(v);
    assertEquals(0, v.incoming().size());
  }
  static void verifySources( final Graph<String> s) {
    verifyCollection(s, s.sources(), s.sourcesCount(), λ -> verifySource(s, λ));
  }
  static void verifyVertex( final Graph<String> s,  final String... vertices) {
    for ( final String vertex : vertices)
      assert vertex != null : s.vertex(vertex);
    for (final String vertex : vertices)
      verifyVertex(s, s.vertex(vertex));
  }
  static void verifyVertex( final Graph<String> s,  final Vertex<String> v) {
    assert v != null;
    assert index(s, v) >= 0;
    assert index(s, v) < s.size();
    assertEquals(v, s.vertices().get(index(s, v)));
  }
  static void verifyVertices( final Graph<String> s) {
    verifyCollection(s, s.vertices(), s.size(), λ -> verifyVertex(s, λ));
  }
  private static boolean among( final String what,  final String... where) {
    for (final String ¢ : where)
      if (what.equals(¢))
        return true;
    return false;
  }
  private static int index( final Graph<String> s, final String v) {
    for (int $ = 0; $ < s.vertices().size(); ++$)
      if (s.vertices().get($).e().equals(v))
        return $;
    return -1;
  }
  private static int index( final Graph<String> s, final Vertex<String> v) {
    for (int $ = 0; $ < s.vertices().size(); ++$)
      if (s.vertices().get($) == v)
        return $;
    return -1;
  }
  private static void verifyCollection( final Graph<String> s,  final ImmutableArrayList<Vertex<String>> vs, final int size,
       final Query q) {
    assert vs != null;
    for (final Vertex<String> ¢ : vs)
      q.test(¢);
    assertEquals(size, vs.size());
     final boolean[] seen = new boolean[s.size()];
    for (final Vertex<String> ¢ : vs) {
      assert !seen[index(s, ¢)];
      seen[index(s, ¢)] = true;
    }
  }
  private static void verifyCollection( final Graph<String> s,  final Iterable<Vertex<String>> ss, final int length,
       final Query q) {
    assert ss != null;
    for (final Vertex<String> ¢ : ss)
      q.test(¢);
    assertEquals(length, Iterables.count(ss));
     final boolean[] seen = new boolean[s.size()];
    for (final Vertex<String> ¢ : ss) {
      assert !seen[index(s, ¢)];
      seen[index(s, ¢)] = true;
    }
  }
  private static void verifyFound(final ImmutableArrayList<Vertex<String>> s, final Vertex<String> u) {
    azzert.that(s, hasItem(u));
  }
  @Test public void builderAddGraph() {
     final Graph<String> src = new Graph.Builder<String>() //
        .newEdge("A", "C").newEdge("B", "C").newEdge("C", "D")//
        .newEdge("C", "E").newEdge("F", "G").newEdge("H", "I")//
        .newEdge("I", "H").newVertex("J")//
        .build();
    verifyGraph(src);
    verifyVertex(src, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    assertEquals(10, src.size());
     final Graph<String> g = new Graph.Builder<String>().addGraph(src).build();
    verifyVertex(g, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    assertEquals(10, g.size());
    verifyGraphsEquivlanet(src, g);
    verifyGraph(g);
  }
  @Test public void emptyGraph() {
    final Graph<String> g = new Graph.Builder<String>().build();
    assertEquals(g.size(), 0);
    assertEquals(g.vertices().size(), 0);
    assertEquals(g.sourcesCount(), 0);
    assertEquals(g.sinksCount(), 0);
    verifyGraph(g);
  }
  @Test public void emptyNamedGraph() {
    assertEquals("empty", new Graph.Builder<String>("empty").build().name());
  }
  @Test public void emptyNamedInvertedGraph() {
    assertEquals("empty" + NamedEntity.INVERTED, new Graph.Builder<String>("empty").build().invert().name());
  }
  @Test public void flowGraph() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("START", "END", "a") //
        .outgoing("a", "b", "c") //
        .outgoing("b", "c")//
        .outgoing("c", "d", "e") //
        .outgoing("d", "f")//
        .outgoing("e", "f") //
        .outgoing("f", "b", "g") //
        .outgoing("g", "END") //
        .build();
    assertEquals(g.vertex("START"), g.source(g.vertex("START")));
    assertEquals(g.vertex("START"), g.source(g.vertex("a")));
    assertEquals(g.vertex("START"), g.source(g.vertex("b")));
    assertEquals(g.vertex("START"), g.source(g.vertex("c")));
    assertEquals(g.vertex("START"), g.source(g.vertex("d")));
    assertEquals(g.vertex("START"), g.source(g.vertex("e")));
    assertEquals(g.vertex("START"), g.source(g.vertex("f")));
    assertEquals(g.vertex("START"), g.source(g.vertex("g")));
    assertEquals(g.vertex("START"), g.source(g.vertex("END")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("START"), g.source(¢));
  }
  @Test public void invertedTree() {
     final Graph<String> g = makeInvertedTree();
    assert among(g.source(g.vertex("A")).e(), "A");
    assert among(g.source(g.vertex("B")).e(), "B");
    assert among(g.source(g.vertex("C")).e(), "C");
    assert among(g.source(g.vertex("D")).e(), "D");
    assert among(g.source(g.vertex("E")).e(), "A", "B");
    assert among(g.source(g.vertex("F")).e(), "C", "D");
    assert among(g.source(g.vertex("G")).e(), "A", "B", "C", "D");
  }
  @Test public void invertedTreeLoops() {
     final Graph<String> g = makeInvertedTreeWithLoops();
    assert among(g.source(g.vertex("A")).e(), "A");
    assert among(g.source(g.vertex("B")).e(), "B");
    assert among(g.source(g.vertex("C")).e(), "C");
    assert among(g.source(g.vertex("D")).e(), "D");
    assert among(g.source(g.vertex("E")).e(), "A", "B");
    assert among(g.source(g.vertex("F")).e(), "C", "D");
    assert among(g.source(g.vertex("G")).e(), "A", "B", "C", "D");
  }
  @Test public void namedTriagle() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("root", "side", "tail") //
        .outgoing("side", "tail") //
        .build();
    assertEquals(g.vertex("root"), g.source(g.vertex("root")));
    assertEquals(g.vertex("root"), g.source(g.vertex("side")));
    assertEquals(g.vertex("root"), g.source(g.vertex("tail")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("root"), g.source(¢));
  }
  @Test public void numbersTriagleExample() {
     final Graph<String> g = makeOneTwoThreeTrianble();
    assertEquals(g.vertex("one"), g.source(g.vertex("one")));
    assertEquals(g.vertex("one"), g.source(g.vertex("two")));
    assertEquals(g.vertex("one"), g.source(g.vertex("three")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("one"), g.source(¢));
  }
  @Test public void singleEdgeGraph() {
     final Graph<String> g = makeSingleEdge();
    assertEquals(2, g.size());
    assertEquals(1, g.sourcesCount());
    assertEquals(1, g.sinksCount());
    assertEquals(2, g.vertices().size());
    assertEquals(1, g.outDegree(g.vertex("A")));
    assertEquals(0, g.inDegree(g.vertex("A")));
    assertEquals(0, g.outDegree(g.vertex("B")));
    assertEquals(1, g.inDegree(g.vertex("B")));
    verifyEdge(g, "A", "B");
    verifySource(g, "A");
    verifySink(g, "B");
    assertEquals(0, g.vertices().get(index(g, "A")).incoming().size());
    assertEquals(0, g.vertices().get(index(g, "B")).outgoing().size());
    assertEquals(1, g.vertices().get(index(g, "A")).outgoing().size());
    assertEquals(1, g.vertices().get(index(g, "B")).incoming().size());
    assertEquals(1, index(g, "A") + index(g, "B"));
    verifyGraph(g);
  }
  @Test public void singleEdgeGraphPreOrder() {
     final Graph<String> g = makeSingleEdge();
     final Iterator<? extends Vertex<String>> i = g.preOrder().iterator();
    assert i.hasNext();
    assertEquals(i.next(), g.vertex("A"));
    assert i.hasNext();
    assertEquals(i.next(), g.vertex("B"));
    assert !i.hasNext();
    azzert.isNull(i.next());
  }
  @Test public void singleLoopGraph() {
     final Graph<String> g = make2Clique();
    assertEquals(1, g.outDegree(g.vertex("A")));
    assertEquals(1, g.inDegree(g.vertex("A")));
    assertEquals(1, g.outDegree(g.vertex("A")));
    assertEquals(1, g.inDegree(g.vertex("B")));
    assertEquals(2, g.size());
    assertEquals(0, g.sourcesCount());
    assertEquals(0, g.sinksCount());
    assertEquals(2, g.vertices().size());
    verifyGraph(g);
  }
  @Test public void singleLoopMultipleInsertionsGraph() {
     final Graph.Builder<String> b = new Graph.Builder<>();
    b.newVertex("A");
    b.newVertex("B");
    b.newEdge("A", "B");
    b.newEdge("B", "A");
    b.newVertex("A");
    b.newVertex("B");
    b.newEdge("A", "B");
    b.newEdge("B", "A");
    b.newVertex("A");
    b.newVertex("B");
    b.newEdge("A", "B");
    b.newEdge("B", "A");
     final Graph<String> g = b.build();
    assertEquals(2, g.size());
    assertEquals(0, g.sourcesCount());
    assertEquals(0, g.sinksCount());
    assertEquals(2, g.vertices().size());
    verifyGraph(g);
  }
  @Test public void singletonGraph() {
     final Graph<String> g = make1Clique();
    assertEquals(1, g.size());
    assertEquals(1, g.vertices().size());
    assertEquals(1, g.sourcesCount());
    assertEquals(1, g.sinksCount(), 1);
    assertEquals(0, index(g, "A"));
    assertEquals(0, g.vertices().get(0).incoming().size());
    assertEquals(0, g.vertices().get(0).outgoing().size());
    verifyGraph(g);
  }
  @Test public void testAll() {
    for ( final Graph<String> ¢ : makeAll())
      assertEquals(¢.size(), Iterables.count(¢.preOrder()));
  }
  @Test public void testCFGExample() {
     final Graph<String> g = makeCFGExample();
    assertEquals(g.vertex("START"), g.source(g.vertex("START")));
    assertEquals(g.vertex("START"), g.source(g.vertex("a")));
    assertEquals(g.vertex("START"), g.source(g.vertex("b")));
    assertEquals(g.vertex("START"), g.source(g.vertex("c")));
    assertEquals(g.vertex("START"), g.source(g.vertex("d")));
    assertEquals(g.vertex("START"), g.source(g.vertex("e")));
    assertEquals(g.vertex("START"), g.source(g.vertex("END")));
    assertEquals(g.vertex("START"), g.source(g.vertex("f")));
    assertEquals(g.vertex("START"), g.source(g.vertex("g")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("START"), g.source(¢));
  }
  @Test public void testChain() {
     final Graph<String> g = makeChainABCDEF();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("B")));
    assertEquals(g.vertex("A"), g.source(g.vertex("C")));
    assertEquals(g.vertex("A"), g.source(g.vertex("D")));
    assertEquals(g.vertex("A"), g.source(g.vertex("E")));
    assertEquals(g.vertex("A"), g.source(g.vertex("F")));
  }
  @Test public void testDiamond() {
     final Graph<String> g = makeDiamond();
    assertEquals(g.vertex("D"), g.source(g.vertex("V")));
    assertEquals(g.vertex("D"), g.source(g.vertex("B1")));
    assertEquals(g.vertex("D"), g.source(g.vertex("B2")));
    assertEquals(g.vertex("D"), g.source(g.vertex("D")));
  }
  @Test public void testDiamondBasic() {
     final Graph<String> g = makeDiamond();
    assertEquals(4, g.size());
    assertEquals(4, g.vertices().size());
    assertEquals(1, g.sourcesCount());
    assertEquals(1, g.sinksCount());
    assert g.vertex("D") != null;
    assert g.vertex("B1") != null;
    assert g.vertex("B2") != null;
    assert g.vertex("V") != null;
    assertEquals(6, index(g, "D") + index(g, "B1") + index(g, "B2") + index(g, "V"));
    verifySink(g, "V");
    verifySource(g, "D");
    verifyEdge(g, "B1", "V");
    verifyEdge(g, "B2", "V");
    verifyEdge(g, "D", "B1");
    verifyEdge(g, "D", "B2");
    verifyGraph(g);
  }
  @Test public void testInvertedTree() {
     final Graph<String> g = makeInvertedTree();
    assertEquals(7, Iterables.count(g.preOrder()));
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assert Iterables.contains(g.preOrder(), g.vertex("E"));
    assert Iterables.contains(g.preOrder(), g.vertex("F"));
    assert Iterables.contains(g.preOrder(), g.vertex("G"));
  }
  @Test public void testInvertedTreeLoops() {
     final Graph<String> g = makeInvertedTreeWithLoops();
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assert Iterables.contains(g.preOrder(), g.vertex("E"));
    assert Iterables.contains(g.preOrder(), g.vertex("F"));
    assert Iterables.contains(g.preOrder(), g.vertex("G"));
  }
  @Test public void testLowerCaseTriagleExample() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("a", "b", "c") //
        .outgoing("b", "c") //
        .build();
    assertEquals(g.vertex("a"), g.source(g.vertex("a")));
    assertEquals(g.vertex("a"), g.source(g.vertex("b")));
    assertEquals(g.vertex("a"), g.source(g.vertex("c")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("a"), g.source(¢));
  }
  @Test public void testPreOrderInnerCycle() {
     final Graph<String> g = new Graph.Builder<String>() //
        .newEdge("A", "C").newEdge("B", "C").newEdge("C", "D")//
        .newEdge("C", "E").newEdge("F", "G")//
        .newEdge("H", "I").newEdge("I", "H")//
        .newVertex("J")//
        .build();
    assertEquals(10, g.size());
    assertEquals(10, g.vertices().size());
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assert Iterables.contains(g.preOrder(), g.vertex("E"));
    assert Iterables.contains(g.preOrder(), g.vertex("F"));
    assert Iterables.contains(g.preOrder(), g.vertex("G"));
    assert Iterables.contains(g.preOrder(), g.vertex("J"));
    assert Iterables.contains(g.preOrder(), g.vertex("I"));
    assert Iterables.contains(g.preOrder(), g.vertex("H"));
    assertEquals(g.size(), Iterables.count(g.preOrder()));
  }
  @Test public void testPreOrderSmallIsolatedInnerCycle() {
     final Graph<String> g = makeAloofNodeAndAloofCycle();
    assertEquals(3, g.vertices().size());
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assertEquals(3, Iterables.count(g.preOrder()));
  }
  @Test public void testSimple() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("A", "a") //
        .outgoing("a", "C", "D") //
        .outgoing("C", "D")//
        .build();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("a")));
    assertEquals(g.vertex("A"), g.source(g.vertex("C")));
    assertEquals(g.vertex("A"), g.source(g.vertex("D")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("A"), g.source(¢));
  }
  @Test public void testSimpleTree() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("A", "E", "B") //
        .outgoing("B", "C", "D") //
        .outgoing("C", "D")//
        .build();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("B")));
    assertEquals(g.vertex("A"), g.source(g.vertex("C")));
    assertEquals(g.vertex("A"), g.source(g.vertex("D")));
    assertEquals(g.vertex("A"), g.source(g.vertex("E")));
    for ( final Vertex<String> ¢ : g.vertices())
      azzert.that("Node " + ¢.e(), g.source(¢), is(g.vertex("A")));
  }
  @Test public void testSingleEdgeSource() {
     final Graph<String> g = makeSingleEdge();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("B")));
  }
  @Test public void testSingleLoop() {
     final Graph<String> g = make2Clique();
    assertEquals(g.vertex("B"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("B")));
  }
  @Test public void testSingletonGraphSource() {
     final Graph<String> g = make1Clique();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
  }
  @Test public void testSingletonLoopGraph() {
     final Graph<String> g = makeSingletonLoop();
    assertEquals(1, g.size());
    assertEquals(1, g.vertices().size());
    assertEquals(0, g.sourcesCount());
    assertEquals(0, g.sinksCount());
    assertEquals(0, index(g, "A"));
    assertEquals(g.vertices().get(0), g.vertices().get(0).incoming().get(0));
    assertEquals(g.vertices().get(0), g.vertices().get(0).outgoing().get(0));
    verifyGraph(g);
  }
  @Test public void testThreeByThree() {
     final Graph<String> g = make3By3();
    assertEquals(g.vertex("A1"), g.source(g.vertex("A1")));
    assertEquals(g.vertex("A2"), g.source(g.vertex("A2")));
    assertEquals(g.vertex("A3"), g.source(g.vertex("A3")));
    assert among(g.source(g.vertex("B1")).e(), "A1", "A2", "A3");
    assert among(g.source(g.vertex("B2")).e(), "A1", "A2", "A3");
    assert among(g.source(g.vertex("B3")).e(), "A1", "A2", "A3");
  }
  @Test public void testThreeByThreeGraph() {
     final Graph<String> g = make3By3();
    assertEquals(6, g.size());
    assertEquals(6, g.vertices().size());
    assertEquals(3, g.sourcesCount());
    assertEquals(3, g.sinksCount());
    verifyVertex(g, "A1");
    verifyVertex(g, "A2");
    verifyVertex(g, "A3");
    verifyVertex(g, "B1");
    verifyVertex(g, "B2");
    verifyVertex(g, "B3");
    verifyEdge(g, "A1", "B1");
    verifyEdge(g, "A1", "B2");
    verifyEdge(g, "A1", "B3");
    verifyEdge(g, "A2", "B1");
    verifyEdge(g, "A2", "B2");
    verifyEdge(g, "A2", "B3");
    verifyEdge(g, "A3", "B1");
    verifyEdge(g, "A3", "B2");
    verifyEdge(g, "A3", "B3");
    assertEquals(3, g.outDegree(g.vertex("A1")));
    assertEquals(0, g.inDegree(g.vertex("A1")));
    assertEquals(3, g.outDegree(g.vertex("A2")));
    assertEquals(0, g.inDegree(g.vertex("A2")));
    assertEquals(3, g.outDegree(g.vertex("A3")));
    assertEquals(0, g.inDegree(g.vertex("A3")));
    assertEquals(3, g.inDegree(g.vertex("B1")));
    assertEquals(0, g.outDegree(g.vertex("B1")));
    assertEquals(3, g.inDegree(g.vertex("B2")));
    assertEquals(0, g.outDegree(g.vertex("B2")));
    assertEquals(3, g.inDegree(g.vertex("B3")));
    assertEquals(0, g.outDegree(g.vertex("B3")));
    verifyGraph(g);
  }
  @Test public void testTree() {
     final Graph<String> g = makeTree();
    assertEquals(g.size(), Iterables.count(g.preOrder()));
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assert Iterables.contains(g.preOrder(), g.vertex("E"));
    assert Iterables.contains(g.preOrder(), g.vertex("F"));
    assert Iterables.contains(g.preOrder(), g.vertex("G"));
    assert Iterables.before(g.preOrder(), g.vertex("G"), g.vertex("F"));
    assert Iterables.before(g.preOrder(), g.vertex("G"), g.vertex("E"));
    assert Iterables.before(g.preOrder(), g.vertex("E"), g.vertex("A"));
    assert Iterables.before(g.preOrder(), g.vertex("E"), g.vertex("B"));
    assert Iterables.before(g.preOrder(), g.vertex("F"), g.vertex("C"));
    assert Iterables.before(g.preOrder(), g.vertex("F"), g.vertex("D"));
  }
  @Test public void testTreeWithLoops() {
     final Graph<String> g = makeTreeWithLoops();
    assertEquals(7, Iterables.count(g.preOrder()));
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assert Iterables.contains(g.preOrder(), g.vertex("E"));
    assert Iterables.contains(g.preOrder(), g.vertex("F"));
    assert Iterables.contains(g.preOrder(), g.vertex("G"));
    assert Iterables.before(g.preOrder(), g.vertex("G"), g.vertex("F"));
    assert Iterables.before(g.preOrder(), g.vertex("G"), g.vertex("E"));
    assert Iterables.before(g.preOrder(), g.vertex("E"), g.vertex("A"));
    assert Iterables.before(g.preOrder(), g.vertex("E"), g.vertex("B"));
    assert Iterables.before(g.preOrder(), g.vertex("F"), g.vertex("C"));
    assert Iterables.before(g.preOrder(), g.vertex("F"), g.vertex("D"));
  }
  @Test public void testTwoAloofNodes() {
     final Graph<String> g = makeTwoAloofNodes();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("B"), g.source(g.vertex("B")));
  }
  @Test public void testTwoAloofNodesGraph() {
     final Graph<String> g = makeTwoAloofNodes();
    assertEquals(2, g.size());
    assertEquals(2, g.sourcesCount());
    assertEquals(2, g.sinksCount());
    assertEquals(2, g.vertices().size());
    verifyGraph(g);
  }
  @Test public void testWikiPageRanksExample() {
     final Graph<String> g = makeWikiExample();
    assertEquals(g.vertex("P1"), g.source(g.vertex("P1")));
    assertEquals(g.vertex("P2"), g.source(g.vertex("P2")));
    assertEquals(g.vertex("P3"), g.source(g.vertex("P3")));
    assertEquals(g.vertex("P4"), g.source(g.vertex("P4")));
    assertEquals(g.vertex("P5"), g.source(g.vertex("P5")));
    assert among(g.source(g.vertex("A")).e(), "P1", "P2", "P3", "P4", "P5");
    assert among(g.source(g.vertex("B")).e(), "P1", "P2", "P3", "P4", "P5");
    assert among(g.source(g.vertex("C")).e(), "P1", "P2", "P3", "P4", "P5");
    assert among(g.source(g.vertex("D")).e(), "P1", "P2", "P3", "P4", "P5");
    assert among(g.source(g.vertex("E")).e(), "P1", "P2", "P3", "P4", "P5");
    assert among(g.source(g.vertex("F")).e(), "P1", "P2", "P3", "P4", "P5");
  }
  @Test public void testWikiPageRanksExampleContains() {
     final Graph<String> g = makeWikiExample();
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assert Iterables.contains(g.preOrder(), g.vertex("E"));
    assert Iterables.contains(g.preOrder(), g.vertex("F"));
    assert Iterables.contains(g.preOrder(), g.vertex("P1"));
    assert Iterables.contains(g.preOrder(), g.vertex("P2"));
    assert Iterables.contains(g.preOrder(), g.vertex("P3"));
    assert Iterables.contains(g.preOrder(), g.vertex("P4"));
    assert Iterables.contains(g.preOrder(), g.vertex("P5"));
  }
  @Test public void testWikiPageRanksExampleCount() {
     final Graph<String> g = makeWikiExample();
    assertEquals(g.size(), Iterables.count(g.preOrder()));
  }
  @Test public void toStringTest() {
     final Graph<String> g = makeSingleEdge();
    assertEquals("A", g.vertex("A").e() + "");
    assertEquals("B", g.vertex("B").e() + "");
  }
  @Test public void tree() {
     final Graph<String> g = makeTree();
    assertEquals(g.vertex("G"), g.source(g.vertex("G")));
    assertEquals(g.vertex("G"), g.source(g.vertex("F")));
    assertEquals(g.vertex("G"), g.source(g.vertex("E")));
    assertEquals(g.vertex("G"), g.source(g.vertex("A")));
    assertEquals(g.vertex("G"), g.source(g.vertex("B")));
    assertEquals(g.vertex("G"), g.source(g.vertex("C")));
    assertEquals(g.vertex("G"), g.source(g.vertex("D")));
  }
  @Test public void treeWithLoops() {
     final Graph<String> g = makeTreeWithLoops();
    assertEquals(g.vertex("G"), g.source(g.vertex("G")));
    assertEquals(g.vertex("G"), g.source(g.vertex("F")));
    assertEquals(g.vertex("G"), g.source(g.vertex("E")));
    assertEquals(g.vertex("G"), g.source(g.vertex("A")));
    assertEquals(g.vertex("G"), g.source(g.vertex("B")));
    assertEquals(g.vertex("G"), g.source(g.vertex("C")));
    assertEquals(g.vertex("G"), g.source(g.vertex("D")));
  }
  @Test public void treeWithLoopsAndForwardEdges() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("A", "B", "C") //
        .outgoing("B", "D", "E") //
        .outgoing("C", "F", "G") //
        .newSelfLoops("A", "B", "C", "D", "E", "F", "G") //
        .outgoing("A", "B", "C", "D", "E", "F", "G") //
        .build();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("B")));
    assertEquals(g.vertex("A"), g.source(g.vertex("C")));
    assertEquals(g.vertex("A"), g.source(g.vertex("D")));
    assertEquals(g.vertex("A"), g.source(g.vertex("E")));
    assertEquals(g.vertex("A"), g.source(g.vertex("F")));
    assertEquals(g.vertex("A"), g.source(g.vertex("G")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("A"), g.source(¢));
  }
  @Test public void triangle() {
     final Graph<String> g = new Graph.Builder<String>()//
        .outgoing("A", "B", "C") //
        .outgoing("B", "C") //
        .build();
    assertEquals(g.vertex("A"), g.source(g.vertex("A")));
    assertEquals(g.vertex("A"), g.source(g.vertex("B")));
    assertEquals(g.vertex("A"), g.source(g.vertex("C")));
    for (final Vertex<String> ¢ : g.vertices())
      assertEquals(g.vertex("A"), g.source(¢));
  }
  @Test public void twoConnectedPairs() {
     final Graph<String> g = makeTwoConnectedPairs();
    assertEquals(4, g.vertices().size());
    assert Iterables.contains(g.preOrder(), g.vertex("A"));
    assert Iterables.contains(g.preOrder(), g.vertex("B"));
    assert Iterables.contains(g.preOrder(), g.vertex("C"));
    assert Iterables.contains(g.preOrder(), g.vertex("D"));
    assertEquals(4, Iterables.count(g.preOrder()));
    assert Iterables.before(g.preOrder(), g.vertex("A"), g.vertex("C"));
    assert Iterables.before(g.preOrder(), g.vertex("A"), g.vertex("D"));
    assert Iterables.before(g.preOrder(), g.vertex("B"), g.vertex("C"));
    assert Iterables.before(g.preOrder(), g.vertex("B"), g.vertex("D"));
  }
  @Test public void wikiPageRanksExample() {
     final Graph<String> g = makeWikiExample();
    assertEquals(11, g.size());
    assertEquals(5, g.sourcesCount());
    assertEquals(1, g.sinksCount());
    assertEquals(1, g.inDegree(g.vertex("A")));
    assertEquals(0, g.outDegree(g.vertex("A")));
    assertEquals(6, g.inDegree(g.vertex("E")));
    assertEquals(3, g.outDegree(g.vertex("E")));
    assertEquals(11, Iterables.count(g.preOrder()));
    verifyPreorder(g);
    verifyGraph(g);
  }

  interface Query {
    void test(Vertex<String> s);
  }
}

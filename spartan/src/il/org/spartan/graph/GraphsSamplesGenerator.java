package il.org.spartan.graph;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

import il.org.spartan.graph.Graph.*;

public class GraphsSamplesGenerator {
  public static Graph<String> make1By2Clique() {
    return makeCliqueBuilder(2).newEdge("START", "A").newEdge("START", "B").build();
  }

  public static Graph<String> make1By3Clique() {
    return makeCliqueBuilder(3).newEdge("START", "A").newEdge("START", "B").newEdge("START", "C").build();
  }

  public static Graph<String> make1Clique() {
    return new Graph.Builder<String>().newVertex("A").build();
  }

  public static Graph<String> make2And1() {
    return makeCliqueBuilder(2).newEdge("A", "END").build();
  }

  public static Graph<String> make2By2() {
    final Graph.Builder<String> b = new Graph.Builder<>();
    b.newEdge("A1", "B1").newEdge("A1", "B2");
    b.newEdge("A2", "B1").newEdge("A2", "B2");
    return b.build();
  }

  public static Graph<String> make2Clique() {
    return makeClique(2);
  }

  public static Graph<String> make2CliqueBy1() {
    return makeCliqueBuilder(2).newEdge("A", "END").newEdge("B", "END").build();
  }

  public static Graph<String> make3By3() {
    final Graph.Builder<String> b = new Graph.Builder<>();
    b.newEdge("A1", "B1").newEdge("A1", "B2").newEdge("A1", "B3");
    b.newEdge("A2", "B1").newEdge("A2", "B2").newEdge("A2", "B3");
    b.newEdge("A3", "B1").newEdge("A3", "B2").newEdge("A3", "B3");
    return b.build();
  }

  public static Graph<String> make3By4() {
    final Graph.Builder<String> b = new Graph.Builder<>();
    b.newEdge("A1", "B1").newEdge("A1", "B2").newEdge("A1", "B3").newEdge("A1", "B4");
    b.newEdge("A2", "B1").newEdge("A2", "B2").newEdge("A2", "B3").newEdge("A2", "B4");
    b.newEdge("A3", "B1").newEdge("A3", "B2").newEdge("A3", "B3").newEdge("A3", "B4");
    return b.build();
  }

  public static Graph<String> make3CliqueBy1() {
    return makeCliqueBuilder(3).newEdge("A", "END").newEdge("B", "END").newEdge("C", "END").build();
  }

  public static Iterable<Graph<String>> makeAll() {
    final List<Graph<String>> $ = new ArrayList<>();
    final Class<GraphsSamplesGenerator> c = GraphsSamplesGenerator.class;
    for (final Method m : c.getDeclaredMethods())
      if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0 && m.getReturnType() == Graph.class)
        try {
          @SuppressWarnings("unchecked") final Graph<String> g = (Graph<String>) m.invoke(null);
          $.add(g);
        } catch (final IllegalArgumentException e) {
          e.printStackTrace();
        } catch (final IllegalAccessException e) {
          e.printStackTrace();
        } catch (final InvocationTargetException e) {
          e.printStackTrace();
        }
    assertEquals(28, $.size());
    for (int i = 0; i < 6; i++) {
      $.add(makeClique(i));
      $.add(makeCLIQUE(i));
      $.add(makeChain(i));
    }
    return $;
  }

  public static Graph<String> makeAloofNodeAndAloofCycle() {
    return new Graph.Builder<String>() //
        .newVertex("A")//
        .newEdge("B", "C").newEdge("C", "B")//
        .build();
  }

  public static Graph<String> makeCFGExample() {
    final Graph.Builder<String> b = new Graph.Builder<>();
    b.newEdge("START", "END").newEdge("START", "a");
    b.newEdge("a", "b").newEdge("a", "c");
    b.newEdge("b", "c");
    b.newEdge("c", "d").newEdge("c", "e");
    b.newEdge("d", "f").newEdge("e", "f");
    b.newEdge("f", "b").newEdge("f", "g");
    b.newEdge("g", "END");
    return b.build();
  }

  public static Graph<String> makeChain(final int n) {
    final Graph.Builder<String> $ = new Graph.Builder<>("Chain " + n);
    for (char from = 'A'; from < 'A' + n - 1; from++)
      $.newEdge("" + from, "" + (char) (from + 1));
    return $.build();
  }

  public static Graph<String> makeChainABC() {
    return new Graph.Builder<String>("ABC").newEdge("A", "B").newEdge("B", "C").build();
  }

  public static Graph<String> makeChainABCDEF() {
    final Graph.Builder<String> b = new Graph.Builder<>();
    b.newEdge("A", "B");
    b.newEdge("B", "C");
    b.newEdge("C", "D");
    b.newEdge("D", "E");
    b.newEdge("E", "F");
    return b.build();
  }

  public static Graph<String> makeClique(final int n) {
    return makeCliqueBuilder(n).build();
  }

  public static Graph<String> makeCLIQUE(final int n) {
    final Graph.Builder<String> $ = makeCliqueBuilder(n);
    for (char c = 'A'; c < 'A' + n; c++)
      $.newEdge("" + c, "" + c);
    return $.build();
  }

  public static Builder<String> makeCliqueBuilder(final int n) {
    final Graph.Builder<String> $ = new Graph.Builder<>("Clique " + n);
    for (char from = 'A'; from < 'A' + n; from++) {
      $.newVertex("" + from);
      for (char to = 'A'; to < 'A' + n; to++)
        if (from != to)
          $.newEdge("" + from, "" + to);
    }
    return $;
  }

  public static Graph<String> makeDiamond() {
    return new Graph.Builder<String>("diamond") //
        .newEdge("B1", "V").newEdge("B2", "V") //
        .newEdge("D", "B1").newEdge("D", "B2") //
        .build();
  }

  public static Graph<String> makeHujiLectureGraph() {
    return new Graph.Builder<String>() //
        .incoming("b", "a", "c", "d", "e") //
        .incoming("c", "c", "d") //
        .incoming("d", "g") //
        .incoming("e", "b", "d", "f") //
        .incoming("f", "h", "d") //
        .incoming("g", "f") //
        .incoming("h", "f") //
        .build();
  }

  public static Graph<String> makeInvertedTree() {
    return new Graph.Builder<String>("Inverted tree") //
        .newVertices("A", "B", "C", "D")//
        .newVertices("E", "F")//
        .newVertices("G")//
        .incoming("G", "E", "F")//
        .incoming("E", "A", "B")//
        .incoming("F", "C", "D")//
        .build();
  }

  public static Graph<String> makeInvertedTreeWithLoops() {
    return new Graph.Builder<String>("Inverted tree with loops") //
        .newVertices("A", "B", "C", "D")//
        .newVertices("E", "F")//
        .newVertices("G")//
        .incoming("G", "E", "F")//
        .incoming("E", "A", "B")//
        .incoming("F", "C", "D")//
        .newSelfLoops("A", "B", "C", "D", "E", "F", "G") //
        .build();
  }

  public static Graph<String> makeOneTwoThreeTrianble() {
    return new Graph.Builder<String>()//
        .outgoing("one", "two", "three") //
        .outgoing("two", "three") //
        .build();
  }

  public static Graph<String> makeSingleEdge() {
    final Graph.Builder<String> b = new Graph.Builder<>();
    b.newEdge("A", "B");
    return b.build();
  }

  public static Graph<String> makeSingletonLoop() {
    return new Graph.Builder<String>("Singleton Loop").newEdge("A", "A").build();
  }

  public static Graph<String> makeSingletonNode() {
    return new Graph.Builder<String>("Singleton").newVertex("A").build();
  }

  public static Graph<String> makeTree() {
    return new Graph.Builder<String>() //
        .newVertices("A", "B", "C", "D")//
        .newVertices("E", "F")//
        .newVertices("G")//
        .outgoing("G", "E", "F")//
        .outgoing("E", "A", "B")//
        .outgoing("F", "C", "D")//
        .build();
  }

  public static Graph<String> makeTreeWithLoops() {
    return new Graph.Builder<String>("Tree with Loops") //
        .newVertices("A", "B", "C", "D")//
        .newVertices("E", "F")//
        .newVertices("G")//
        .outgoing("G", "E", "F")//
        .outgoing("E", "A", "B")//
        .outgoing("F", "C", "D")//
        .newSelfLoops("A", "B", "C", "D", "E", "F", "G") //
        .build();
  }

  public static Graph<String> makeTwoAloofNodes() {
    return new Graph.Builder<String>().newVertex("A").newVertex("B").build();
  }

  public static Graph<String> makeTwoConnectedPairs() {
    return new Graph.Builder<String>("Two Connected Pairs") //
        .newEdge("A", "B").newEdge("B", "A")//
        .newEdge("B", "C")//
        .newEdge("C", "D").newEdge("D", "C")//
        .build();
  }

  public static Graph<String> makeTwoConnectedTriples() {
    return new Graph.Builder<String>("Two connected Triples") //
        .newEdge("A", "B").newEdge("B", "C").newEdge("C", "A")//
        .newEdge("D", "E").newEdge("E", "F").newEdge("F", "D")//
        .newEdge("C", "D").build();
  }

  public static Graph<String> makeWikiExample() {
    final Graph.Builder<String> b = new Graph.Builder<>("PageRank wiki example");
    b.incoming("A", "D");
    b.incoming("B", "C", "D", "E", "P1", "P2", "P3");
    b.incoming("C", "B");
    b.incoming("D", "E");
    b.incoming("E", "F", "P1", "P2", "P3", "P4", "P5");
    b.incoming("F", "E");
    return b.build();
  }
}

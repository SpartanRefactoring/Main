package il.org.spartan.graph;

import static fluent.ly.azzert.*;

import java.lang.reflect.*;
import java.util.*;

import org.jetbrains.annotations.*;

import fluent.ly.*;
import il.org.spartan.graph.Graph.*;

public class GraphsSamplesGenerator {
  @NotNull public static Graph<String> make1By2Clique() {
    return makeCliqueBuilder(2).newEdge("START", "A").newEdge("START", "B").build();
  }
  @NotNull public static Graph<String> make1By3Clique() {
    return makeCliqueBuilder(3).newEdge("START", "A").newEdge("START", "B").newEdge("START", "C").build();
  }
  @NotNull public static Graph<String> make1Clique() {
    return new Graph.Builder<String>().newVertex("A").build();
  }
  @NotNull public static Graph<String> make2And1() {
    return makeCliqueBuilder(2).newEdge("A", "END").build();
  }
  @NotNull public static Graph<String> make2By2() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>();
    $.newEdge("A1", "B1").newEdge("A1", "B2");
    $.newEdge("A2", "B1").newEdge("A2", "B2");
    return $.build();
  }
  @NotNull public static Graph<String> make2Clique() {
    return makeClique(2);
  }
  @NotNull public static Graph<String> make2CliqueBy1() {
    return makeCliqueBuilder(2).newEdge("A", "END").newEdge("B", "END").build();
  }
  @NotNull public static Graph<String> make3By3() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>();
    $.newEdge("A1", "B1").newEdge("A1", "B2").newEdge("A1", "B3");
    $.newEdge("A2", "B1").newEdge("A2", "B2").newEdge("A2", "B3");
    $.newEdge("A3", "B1").newEdge("A3", "B2").newEdge("A3", "B3");
    return $.build();
  }
  @NotNull public static Graph<String> make3By4() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>();
    $.newEdge("A1", "B1").newEdge("A1", "B2").newEdge("A1", "B3").newEdge("A1", "B4");
    $.newEdge("A2", "B1").newEdge("A2", "B2").newEdge("A2", "B3").newEdge("A2", "B4");
    $.newEdge("A3", "B1").newEdge("A3", "B2").newEdge("A3", "B3").newEdge("A3", "B4");
    return $.build();
  }
  @NotNull public static Graph<String> make3CliqueBy1() {
    return makeCliqueBuilder(3).newEdge("A", "END").newEdge("B", "END").newEdge("C", "END").build();
  }
  @NotNull public static Iterable<Graph<String>> makeAll() {
    @NotNull final List<Graph<String>> $ = new ArrayList<>();
    @NotNull final Class<GraphsSamplesGenerator> c = GraphsSamplesGenerator.class;
    for (@NotNull final Method m : c.getDeclaredMethods())
      if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0 && m.getReturnType() == Graph.class)
        try {
          $.add((Graph<String>) m.invoke(null));
        } catch (@NotNull final InvocationTargetException | IllegalAccessException | IllegalArgumentException ¢) {
          ¢.printStackTrace();
        }
    azzert.that($.size(), is(28));
    for (int ¢ = 0; ¢ < 6; ++¢) {
      $.add(makeClique(¢));
      $.add(makeCLIQUE(¢));
      $.add(makeChain(¢));
    }
    return $;
  }
  @NotNull public static Graph<String> makeAloofNodeAndAloofCycle() {
    return new Graph.Builder<String>() //
        .newVertex("A")//
        .newEdge("B", "C").newEdge("C", "B")//
        .build();
  }
  @NotNull public static Graph<String> makeCFGExample() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>();
    $.newEdge("START", "END").newEdge("START", "a");
    $.newEdge("a", "b").newEdge("a", "c");
    $.newEdge("b", "c");
    $.newEdge("c", "d").newEdge("c", "e");
    $.newEdge("d", "f").newEdge("e", "f");
    $.newEdge("f", "b").newEdge("f", "g");
    $.newEdge("g", "END");
    return $.build();
  }
  @NotNull public static Graph<String> makeChain(final int i) {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>("Chain " + i);
    for (char from = 'A'; from < i + 'A' - 1; ++from)
      $.newEdge(from + "", (char) (from + 1) + "");
    return $.build();
  }
  @NotNull public static Graph<String> makeChainABC() {
    return new Graph.Builder<String>("ABC").newEdge("A", "B").newEdge("B", "C").build();
  }
  @NotNull public static Graph<String> makeChainABCDEF() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>();
    $.newEdge("A", "B");
    $.newEdge("B", "C");
    $.newEdge("C", "D");
    $.newEdge("D", "E");
    $.newEdge("E", "F");
    return $.build();
  }
  @NotNull public static Graph<String> makeClique(final int ¢) {
    return makeCliqueBuilder(¢).build();
  }
  @NotNull public static Graph<String> makeCLIQUE(final int i) {
    @NotNull final Graph.Builder<String> $ = makeCliqueBuilder(i);
    for (char ¢ = 'A'; ¢ < i + 'A'; ++¢)
      $.newEdge(¢ + "", ¢ + "");
    return $.build();
  }
  @NotNull public static Builder<String> makeCliqueBuilder(final int i) {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>("Clique " + i);
    for (char from = 'A'; from < i + 'A'; ++from) {
      $.newVertex(from + "");
      for (char to = 'A'; to < i + 'A'; ++to)
        if (from != to)
          $.newEdge(from + "", to + "");
    }
    return $;
  }
  @NotNull public static Graph<String> makeDiamond() {
    return new Graph.Builder<String>("diamond") //
        .newEdge("B1", "V").newEdge("B2", "V") //
        .newEdge("D", "B1").newEdge("D", "B2") //
        .build();
  }
  @NotNull public static Graph<String> makeHujiLectureGraph() {
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
  @NotNull public static Graph<String> makeInvertedTree() {
    return new Graph.Builder<String>("Inverted tree") //
        .newVertices("A", "B", "C", "D")//
        .newVertices("E", "F")//
        .newVertices("G")//
        .incoming("G", "E", "F")//
        .incoming("E", "A", "B")//
        .incoming("F", "C", "D")//
        .build();
  }
  @NotNull public static Graph<String> makeInvertedTreeWithLoops() {
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
  @NotNull public static Graph<String> makeOneTwoThreeTrianble() {
    return new Graph.Builder<String>()//
        .outgoing("one", "two", "three") //
        .outgoing("two", "three") //
        .build();
  }
  @NotNull public static Graph<String> makeSingleEdge() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>();
    $.newEdge("A", "B");
    return $.build();
  }
  @NotNull public static Graph<String> makeSingletonLoop() {
    return new Graph.Builder<String>("Singleton Loop").newEdge("A", "A").build();
  }
  @NotNull public static Graph<String> makeSingletonNode() {
    return new Graph.Builder<String>("Singleton").newVertex("A").build();
  }
  @NotNull public static Graph<String> makeTree() {
    return new Graph.Builder<String>() //
        .newVertices("A", "B", "C", "D")//
        .newVertices("E", "F")//
        .newVertices("G")//
        .outgoing("G", "E", "F")//
        .outgoing("E", "A", "B")//
        .outgoing("F", "C", "D")//
        .build();
  }
  @NotNull public static Graph<String> makeTreeWithLoops() {
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
  @NotNull public static Graph<String> makeTwoAloofNodes() {
    return new Graph.Builder<String>().newVertex("A").newVertex("B").build();
  }
  @NotNull public static Graph<String> makeTwoConnectedPairs() {
    return new Graph.Builder<String>("Two Connected Pairs") //
        .newEdge("A", "B").newEdge("B", "A")//
        .newEdge("B", "C")//
        .newEdge("C", "D").newEdge("D", "C")//
        .build();
  }
  @NotNull public static Graph<String> makeTwoConnectedTriples() {
    return new Graph.Builder<String>("Two connected Triples") //
        .newEdge("A", "B").newEdge("B", "C").newEdge("C", "A")//
        .newEdge("D", "E").newEdge("E", "F").newEdge("F", "D")//
        .newEdge("C", "D").build();
  }
  @NotNull public static Graph<String> makeWikiExample() {
    @NotNull final Graph.Builder<String> $ = new Graph.Builder<>("PageRank wiki example");
    $.incoming("A", "D");
    $.incoming("B", "C", "D", "E", "P1", "P2", "P3");
    $.incoming("C", "B");
    $.incoming("D", "E");
    $.incoming("E", "F", "P1", "P2", "P3", "P4", "P5");
    $.incoming("F", "E");
    return $.build();
  }
}

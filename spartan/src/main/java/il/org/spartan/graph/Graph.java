package il.org.spartan.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import il.org.spartan.collections.ImmutableArrayList;
import il.org.spartan.streotypes.Immutable;

/** A simple reference compact implementation of a {@link AbstractGraph} of
 * arbitrary objects, using a {@Codex} and Java arrays.
 *
 * @param <E> type of elements stored in this graph
 * @author Yossi Gil
 * @since 2011-11-11 */
public class Graph<@Nullable E> extends AbstractGraph<@Nullable E> {
  private static <E> ImmutableArrayList<Vertex<E>> makeSinks(final ImmutableArrayList<Vertex<E>> v) {
    final ArrayList<Vertex<E>> $ = new ArrayList<>();
    for (final Vertex<E> ¢ : v)
      if (¢.outgoing().isEmpty())
        $.add(¢);
    return ImmutableArrayList.make($);
  }

  private static <E> ImmutableArrayList<Vertex<E>> makeSources(final ImmutableArrayList<Vertex<E>> v) {
    final ArrayList<Vertex<E>> $ = new ArrayList<>();
    for (final Vertex<E> ¢ : v)
      if (¢.incoming().isEmpty())
        $.add(¢);
    return new ImmutableArrayList<>($);
  }

  private final ImmutableArrayList<Vertex<E>> vertices;
  private final ImmutableArrayList<Vertex<E>> sources;
  private final ImmutableArrayList<Vertex<E>> sinks;
  private final Map<E, Vertex<E>> map;
  private final String description;
  private final String name;

  public Graph(final Graph<E> other) {
    this(other.name(), other.description(), other.map);
  }

  /** This <code><b>private</b></code> constructor is used internally by the
   * factory class {@link Builder}, which provides the sole means for
   * Instantiating this class.
   *
   * @param name
   * @param map  An array of vertices */
  private Graph(final String name, final String description, final Map<E, Vertex<E>> map) {
    this.name = name;
    this.description = description;
    this.map = map;
    vertices = ImmutableArrayList.make(map.values());
    sources = makeSources(vertices);
    sinks = makeSinks(vertices);
  }

  /*
   * (non-Javadoc)
   *
   * @see il.org.spartan.metrics.AbstractGraph#contains(il.ac.technion.cs
   * .ssdl.metrics.Vertex)
   */
  @Override public boolean contains(final E ¢) {
    return map.containsKey(¢);
  }

  @Override public String description() {
    return description;
  }

  public boolean hasEdge(final Vertex<E> from, final Vertex<E> to) {
    for (final Vertex<E> ¢ : from.outgoing())
      if (¢ == to)
        return true;
    return false;
  }

  @Override public String name() {
    return name;
  }

  @Override public ImmutableArrayList<Vertex<E>> sinks() {
    return sinks;
  }

  @Override public int sinksCount() {
    return sinks.size();
  }

  @Override public ImmutableArrayList<Vertex<E>> sources() {
    return sources;
  }

  @Override public int sourcesCount() {
    return sources.size();
  }

  @Override public Vertex<E> vertex(final E ¢) {
    return map.get(¢);
  }

  @Override public ImmutableArrayList<Vertex<E>> vertices() {
    return vertices;
  }

  /** @author Yossi Gil
   * @since Dec 7, 2011
   * @param <E> type of elements stored in this graph */
  public static class Builder<E> {
    private static <E> HashSet<E> emptySet() {
      return new HashSet<>();
    }

    private static <E> void fill(final Map<E, Vertex<E>> m, final Vertex<E>[] vs, final HashSet<E> es) {
      int i = 0;
      for (final E ¢ : es)
        vs[i++] = m.get(¢);
    }

    @SuppressWarnings("unchecked") //
    private static <E> Vertex<E>[] newVertexArray(final int size) {
      return new Vertex[size];
    }

    private final String name;
    private final String description;
    private final HashSet<BuildingEdge<E>> edges = new HashSet<>();
    private final LinkedHashMap<E, HashSet<E>> outgoing = new LinkedHashMap<>();
    private final LinkedHashMap<E, HashSet<E>> incoming = new LinkedHashMap<>();

    /** Instantiate this class */
    public Builder() {
      this("");
    }

    public Builder(final Graph<E> g) {
      this(g.name(), g.description());
      addGraph(g);
    }

    public Builder(final Graph<E> g, final String name, final String description) {
      this(name, description);
      addGraph(g);
    }

    public Builder(final String name) {
      this(name, "");
    }

    /** Instantiate this class, while supplying a name for the graph to be built
     *
     * @param name        the name of the graph to be built
     * @param description the description of the graph to be built */
    public Builder(final String name, final String description) {
      this.name = name;
      this.description = description;
    }

    /** Merges into the currently built graph all edges and vertices in the supplied
     * graph.
     *
     * @param g an arbitrary graph
     * @return <code><b>this</b></code> */
    public Builder<E> addGraph(final Graph<E> g) {
      for (final Vertex<E> v : g.vertices())
        if (g.outDegree(v) == 0)
          newVertex(v.e());
        else
          for (final Vertex<E> u : g.outgoing(v))
            newEdge(v.e(), u.e());
      return this;
    }

    /** The actual function to create the graph, defined by all associations
     * recorded so far.
     *
     * @return the Graph object defined by the associations. */
    public Graph<E> build() {
      return new Graph<>(name, description, makeVerticesMap());
    }

    public int countEdges() {
      return edges.size();
    }

    public boolean hasEdge(final E from, final E to) {
      return edges.contains(new BuildingEdge<>(from, to));
    }

    /** Records multiple unidirectional associations between a set of a "from" data
     * elements, and a single, "to" data element. If any of the data elements is not
     * present in the graph, it is added as well.
     *
     * @param to    the target of the associations.
     * @param froms the sources of the associations
     * @return <code><b>this</b></code> */
    @SafeVarargs public final Builder<E> incoming(final E to, final E... froms) {
      for (final E from : froms)
        newEdge(from, to);
      return this;
    }

    /** Records a unidirectional association between two data elements- later to be
     * shown as an edge of the graph. This function also records the existence of
     * the these two data elements.
     *
     * @param from association starts here
     * @param to   association ends here
     * @return <code><b>this</b></code> */
    @NonNull public Builder<E> newEdge(final E from, final E to) {
      newVertex(from).newVertex(to);
      edges.add(new BuildingEdge<>(from, to));
      return this;
    }

    /** Records the presence of multiple data elements. To be used mainly for cases
     * in which these data element may not participate in any association.
     *
     * @param es arbitrary data elements
     * @return <code><b>this</b></code> */
    @SafeVarargs public final Builder<E> newSelfLoops(final E... es) {
      for (final E ¢ : es)
        newEdge(¢, ¢);
      return this;
    }

    /** Records the presence of a new data element, to be used for cases in which
     * the data element may not participate in any association.
     *
     * @param ¢ a data element.
     * @return <code><b>this</b></code> */
    public Builder<E> newVertex(final E ¢) {
      outgoing.put(¢, Builder.emptySet());
      incoming.put(¢, Builder.emptySet());
      return this;
    }

    /** Records the presence of multiple data elements. To be used mainly for cases
     * in which these data elements may not participate in any association.
     *
     * @param es arbitrary data elements
     * @return <code><b>this</b></code> */
    @SafeVarargs public final Builder<E> newVertices(final E... es) {
      for (final E ¢ : es)
        newVertex(¢);
      return this;
    }

    /** Records multiple unidirectional associations between a "from" data element,
     * and a set of "to" data elements. If any of the data elements is not present
     * in the graph, it is added as well.
     *
     * @param from the source of the associations
     * @param tos  the targets of the associations.
     * @return <code><b>this</b></code> */
    @SafeVarargs public final Builder<E> outgoing(final E from, final E... tos) {
      for (final E to : tos)
        newEdge(from, to);
      return this;
    }

    private Map<E, Vertex<E>> makeVerticesMap() {
      // Phase I: Determine vertex set, and record mappings.
      for (final BuildingEdge<E> ¢ : edges) {
        outgoing.get(¢.from).add(¢.to);
        incoming.get(¢.to).add(¢.from);
      }
      final Map<E, Vertex<E>[]> incomingArrays = new HashMap<>(), outgoingArrays = new HashMap<>();
      for (final E e : outgoing.keySet()) {
        final Vertex<E>[] out = newVertexArray(outgoing.get(e).size()), in = newVertexArray(incoming.get(e).size());
        outgoingArrays.put(e, out);
        incomingArrays.put(e, in);
      }
      // Phase III: create the vertices map.
      final Map<E, Vertex<E>> $ = new HashMap<>();
      for (final E ¢ : outgoing.keySet())
        $.put(¢, new Vertex<>(¢, outgoingArrays.get(¢), incomingArrays.get(¢)));
      for (final E v : $.keySet()) {
        fill($, outgoingArrays.get(v), outgoing.get(v));
        fill($, incomingArrays.get(v), incoming.get(v));
      }
      return $;
    }

    @Immutable private static class BuildingEdge<E> {
      public E from;
      public E to;

      public BuildingEdge(final E from, final E to) {
        this.from = from;
        this.to = to;
      }

      @Override public boolean equals(final Object ¢) {
        @SuppressWarnings("unchecked") final BuildingEdge<E> $ = (BuildingEdge<E>) ¢;
        return from.equals($.from) && to.equals($.to);
      }

      @Override public int hashCode() {
        return to.hashCode() ^ from.hashCode() >>> 1;
      }

      @Override public String toString() {
        return "<" + from + "," + to + ">";
      }
    }
  }

  public boolean isEmpty() {
    return vertices().isEmpty();
  }
}

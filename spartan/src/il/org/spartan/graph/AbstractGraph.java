package il.org.spartan.graph;

import static fluent.ly.___.*;

import java.util.*;
import java.util.concurrent.*;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.collections.*;
import il.org.spatan.iteration.Iterables.*;

/** An abstract representation of an immutable directed graph
 * @param <E> type of elements stored in this graph
 * @author Yossi Gil
 * @since 2011-11-11 */
public abstract class AbstractGraph<E> {
  public abstract boolean contains(E d);
  public int countEdges() {
    int $ = 0;
    for ( final Vertex<E> ¢ : vertices())
      $ += outDegree(¢);
    return $;
  }
  /** Which description is associated with this graph?
   * @return the description, if known, of this graph, or the empty string. */
  public abstract String description();
  /** Which edges are incident on this Vertex<E>?
   * @param ¢ some Vertex<E>
   * @return if v has no incoming edges then <code><b>null</b></code>,
   *         otherwise, all vertices from which there an edge leading to v. */
  public ImmutableArrayList<Vertex<E>> incoming( final Vertex<E> ¢) {
    return ¢.incoming();
  }
  public int inDegree( final Vertex<E> ¢) {
    return incoming(¢).size();
  }
  /** Generates the inverse graph, in which all the direction of all edges is
   * inverted.
   * @return the graph obtained by inverting the direction of all edges in this
   *         graph */
   public AbstractGraph<E> invert() {
    return InvertedGraph.make(this);
  }
  /** Determine whether a given Vertex<E> is a source
   * @param ¢ some Vertex<E>
   * @return <code><b>true</b></code> if and only if this Vertex<E> is a
   *         source */
  public final boolean isSource( final Vertex<E> ¢) {
    return inDegree(¢) == 0;
  }
  /** What is this graph's name?
   * @return the name, if known, of this graph, or the empty string. */
  public abstract String name();
  public int outDegree( final Vertex<E> ¢) {
    return ¢.outgoing().size();
  }
  /** Which edges emanate from a given Vertex<E>?
   * @param ¢ some Vertex<E>
   * @return if v has no outgoing edges then <code><b>null</b></code>,
   *         otherwise, all vertices u such that there is an edge emanating from
   *         v and leading to u */
  public ImmutableArrayList<Vertex<E>> outgoing( final Vertex<E> ¢) {
    return ¢.outgoing();
  }
  /** A DFS pre-order iteration over the graph.
   * @return the vertices of the graph, in a pre-order, dfs scan. */
   public Iterable<Vertex<E>> preOrder() {
    return () -> new ReadonlyIterator<Vertex<E>>() {
      final Set<Vertex<E>> visited = new HashSet<>();
      final Set<Vertex<E>> unvisited = new HashSet<>();
      final Stack<Vertex<E>> stack = new Stack<>();
      {
        for (final Vertex<E> ¢ : vertices())
          unvisited.add(¢);
        for (final Vertex<E> ¢ : sources())
          stack.push(¢);
      }
       Vertex<E> pending = findNext();

      @Override public boolean hasNext() {
        return pending != null;
      }
      @Override  public Vertex<E> next() {
         final Vertex<E> $ = pending;
        pending = findNext();
        return $;
      }
      Vertex<E> findNext() {
        while (!stack.empty() || !unvisited.isEmpty()) {
          if (stack.empty())
            for (final Vertex<E> ¢ : unvisited) {
              stack.push(source(¢));
              break;
            }
          final Vertex<E> $ = stack.pop();
          if (outgoing($) != null)
            for (final Vertex<E> ¢ : outgoing($))
              if (fresh(¢))
                stack.push(¢);
          if (fresh($))
            return visit($);
        }
        ensure(unvisited.isEmpty());
        ensure(visited.size() == size());
        ensure(stack.empty());
        return null;
      }
      boolean fresh(final Vertex<E> ¢) {
        return !visited.contains(¢);
      }
      Vertex<E> visit(final Vertex<E> ¢) {
        visited.add(¢);
        unvisited.remove(¢);
        return ¢;
      }
    };
  }
  /** Provides means for iteration over those vertices which are sinks for the
   * graph.
   * <p>
   * This class provides a basic implementation, which iterates over an array of
   * the sinks, where this array is created by a full iteration over all
   * vertices in the first call to this function.
   * @return vertices at which this graph "ends", i.e., precisely those vertices
   *         with no emanating edges. The returned value is never
   *         <code><b>null</b></code>. */
  public abstract ImmutableArrayList<Vertex<E>> sinks();
  /** Provides means for counting the number of sinks in the graph.
   * @return a non-negative integer representing the number of sinks. */
  public abstract int sinksCount();
  /** @return a non-negative integer, representing the number of vertices in the
   *         graph */
  public final int size() {
    return vertices().size();
  }
  /** Return one of the sources of a given Vertex<E>. A Vertex<E> <em>r</em> is
   * a source of a Vertex<E> <i>v</i> if <i>v</i>can be reached from <i>r</i>,
   * and if there is another Vertex<E> <i>r'</i> from which <i>r</i> can be
   * reached, then there is also a path from <i>r</i> to <i>r'</i>.<i>r</i>
   * @param v an arbitrary Vertex<E> of this graph
   * @return a source of the parameter */
  public final Vertex<E> source(final Vertex<E> v) {
     final Queue<Vertex<E>> q = new ArrayBlockingQueue<>(size() + 1);
    q.add(v);
     final Set<Vertex<E>> seen = new HashSet<>();
    seen.add(v);
    for (;;) {
      final Vertex<E> $ = q.poll();
      if (inDegree($) == 0)
        return $;
      for (final Vertex<E> u : incoming($)) {
        if (u == $ && inDegree($) == 1)
          return $;
        if (!seen.contains(u)) {
          seen.add(u);
          q.add(u);
        }
      }
      if (q.isEmpty())
        return $;
    }
  }
  /** Provides means for iteration over those vertices which are sources for the
   * graph.
   * <p>
   * This class provides a basic implementation, which iterates over an array of
   * the sources, where this array is created by a full iteration over all
   * vertices in the first call to this function.
   * @return vertices from which this graph "starts", i.e., precisely those
   *         vertices with no incident edges. The returned value is never
   *         <code><b>null</b></code>. */
  public abstract ImmutableArrayList<Vertex<E>> sources();
  /** Provides means for counting the number of sinks in the graph.
   * @return a non-negative integer representing the number of sinks. */
  public abstract int sourcesCount();
  @Override  public String toString() {
    return name() + "<" + size() + ";" + countEdges() + "> ";
  }
  public abstract Vertex<E> vertex(E e);
  public abstract ImmutableArrayList<Vertex<E>> vertices();
}

package il.org.spartan.graph;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.collections.*;

/** Provides an inverse perspective on a graph, i.e., inverts the direction of
 * all edges in it.
 * @param <E> type of elements stored in this graph
 * @author Yossi Gil
 * @since 2011-11-11 */
public class InvertedGraph<E> extends GraphDecorator<E> {
  /** A factory method, instantiating this class.
   * @param ¢ an arbitrary graph
   * @param <E> type of elements stored in this graph
   * @return the graph representing the parameter where all the direction of all
   *         edges was inverted. */
   public static <E> InvertedGraph<E> make(final AbstractGraph<E> ¢) {
    return new InvertedGraph<>(¢);
  }
  private InvertedGraph(final AbstractGraph<E> g) {
    super(g);
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.GraphDecorator#incoming(il.ac.technion.cs.
   * ssdl.graph.Vertex) */
  @Override public ImmutableArrayList<Vertex<E>> incoming(final Vertex<E> ¢) {
    return super.outgoing(¢);
  }
  @Override  public String name() {
    final String $ = inner.name();
    return $.length() == 0 ? $ : $ + "'";
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.GraphDecorator#outgoing(il.ac.technion.cs.
   * ssdl.graph.Vertex) */
  @Override public ImmutableArrayList<Vertex<E>> outgoing(final Vertex<E> ¢) {
    return super.incoming(¢);
  }
  @Override public ImmutableArrayList<Vertex<E>> sinks() {
    return super.sources();
  }
  @Override public ImmutableArrayList<Vertex<E>> sources() {
    return super.sinks();
  }
}

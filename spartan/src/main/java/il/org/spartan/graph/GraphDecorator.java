package il.org.spartan.graph;

import org.eclipse.jdt.annotation.Nullable;

import il.org.spartan.collections.ImmutableArrayList;

/** A basic graph decorator, which does nothing except for delegating all its
 * missions to an encapsulated {@link AbstractGraph}. To be used as base class
 * for all other graph decorators.
 * @param <E> type of elements stored in this graph
 * @author Yossi Gil
 * @since 2011-11-11 */
public class GraphDecorator<@Nullable E> extends AbstractGraph<E> {
  /** The encapsulated decorated graph. */
  public final AbstractGraph<E> inner;

  /** Instantiated with a given {@link AbstractGraph} object.
   * @param inner an arbitrary {@link AbstractGraph} to be encapsulated. */
  public GraphDecorator(final AbstractGraph<E> inner) {
    this.inner = inner;
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#contains(java.lang.Object) */
  @Override public boolean contains(final E ¢) {
    return inner.contains(¢);
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.iteration.NamedEntity#name() */
  @Override public String description() {
    return inner.description();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#incoming(il.org.spartan
   * .graph.Vertex) */
  @Override public ImmutableArrayList<Vertex<E>> incoming(final Vertex<E> ¢) {
    return inner.incoming(¢);
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.iteration.NamedEntity#name() */
  @Override public String name() {
    return inner.name();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#outgoing(il.org.spartan
   * .graph.Vertex) */
  @Override public ImmutableArrayList<Vertex<E>> outgoing(final Vertex<E> ¢) {
    return inner.outgoing(¢);
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#sinks() */
  @Override public ImmutableArrayList<Vertex<E>> sinks() {
    return inner.sinks();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#sinksCount() */
  @Override public int sinksCount() {
    return inner.sinksCount();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#sources() */
  @Override public ImmutableArrayList<Vertex<E>> sources() {
    return inner.sources();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#sourcesCount() */
  @Override public int sourcesCount() {
    return inner.sourcesCount();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#vertex(java.lang.Object) */
  @Override public Vertex<E> vertex(final E ¢) {
    return inner.vertex(¢);
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.graph.AbstractGraph#vertices() */
  @Override public ImmutableArrayList<Vertex<E>> vertices() {
    return inner.vertices();
  }
}

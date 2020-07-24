package il.org.spartan.graph;

import org.eclipse.jdt.annotation.Nullable;

import il.org.spartan.collections.ImmutableArrayList;

/** A representation of an immutable vertex in an immutable graph
 * @author Yossi Gil
 * @since Dec 9, 2011
 * @param <E> type of elements stored in each vertex */
public class Vertex<@Nullable E> {
  private final E e;
  private final ImmutableArrayList<Vertex<E>> incoming;
  private final ImmutableArrayList<Vertex<E>> outgoing;

  /** Instantiate {@link Vertex}. Package visibility is to prevent clients from
   * instantiating vertices rather than obtaining these from the containing
   * graph.
   * @param data to be stored in this vertex
   * @param outgoing vertices incident on outgoing edges
   * @param incoming vertices incident on incoming edges */
  Vertex(final E e, final Vertex<E>[] outgoing, final Vertex<E>[] incoming) {
    this.e = e;
    this.incoming = ImmutableArrayList.make(incoming);
    this.outgoing = ImmutableArrayList.make(outgoing);
  }
  public E e() {
    return e;
  }
  @Override public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    @SuppressWarnings("unchecked") final Vertex<E> other = (Vertex<E>) o;
    if (e == null) {
      if (other.e != null)
        return false;
    } else if (!e.equals(other.e))
      return false;
    return true;
  }
  @Override public int hashCode() {
    return e.hashCode();
  }
  /** Which vertices lie on the other end of outgoing edges? Package visibility
   * is deliberate; access to to neighbors must be through the enclosing graph,
   * so as to make it possible to invert edge directionality
   * @return vertices that lie on the other end of outgoing edges */
  public ImmutableArrayList<Vertex<E>> outgoing() {
    return outgoing;
  }
  /** Which vertices lie on the other end of incoming edges? Package visibility
   * is deliberate; access to to neighbors must be through the enclosing graph,
   * so as to make it possible to invert edge directionality
   * @return vertices that lie on the other end of incoming edges */
  ImmutableArrayList<Vertex<E>> incoming() {
    return incoming;
  }
}
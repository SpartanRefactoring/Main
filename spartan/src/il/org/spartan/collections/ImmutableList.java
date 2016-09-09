package il.org.spartan.collections;

import java.io.*;
import java.util.*;

/** A collection class that allows addition, counting, iteration, but noting
 * else.
 * @author Yossi Gil
 * @param <T> Type of elements in the collection.
 * @since 01/05/2007 */
public abstract class ImmutableList<T> implements Iterable<T>, Serializable {
  private static final long serialVersionUID = 3846444108525783786L;
  protected List<T> data = new ArrayList<>();

  /** Add an element to the collection.
   * @param element The element to be added.
   * @return The added element */
  public T add(final T element) {
    if (element != null)
      data.add(element);
    return element;
  }

  /** Adds another collection to this one.
   * @param other The element to be added.t */
  public void addAll(final ImmutableList<T> other) {
    data.addAll(other.data);
  }

  /** Adds another collection to this one.
   * @param other The element to be added.t */
  public void addAll(final Set<T> other) {
    data.addAll(other);
  }

  public boolean contains(final Object o) {
    return data.contains(o);
  }

  public boolean containsAll(final Collection<?> c) {
    return data.containsAll(c);
  }

  @Override public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    @SuppressWarnings("unchecked") final ImmutableList<T> otherList = (ImmutableList<T>) o;
    return data.equals(otherList.data);
  }

  @Override public int hashCode() {
    final int PRIME = 31;
    return PRIME * super.hashCode() + (data == null ? 0 : data.hashCode());
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  @Override public Iterator<T> iterator() {
    return data.iterator();
  }

  /** @return The number of elements in the collection. */
  public int size() {
    return data.size();
  }

  /** convert this collection into an array
   * @return an array of the elements stored in this collection */
  public abstract T[] toArrary();

  public Object[] toArray() {
    return data.toArray();
  }

  public T[] toArray(final T[] ts) {
    return data.toArray(ts);
  }
}

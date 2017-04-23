package il.org.spartan.collections;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

/** A collection class that allows addition, counting, iteration, but noting
 * else.
 * @author Yossi Gil
 * @param <T> Type of elements in the collection.
 * @since 01/05/2007 */
public abstract class ImmutableList<T> implements Iterable<T>, Serializable {
  private static final long serialVersionUID = 0x356150899112CAEAL;
  @NotNull protected List<T> data = new ArrayList<>();

  /** Add an element to the collection.
   * @param element The element to be added.
   * @return The added element */
  @Nullable public T add(@Nullable final T element) {
    if (element != null)
      data.add(element);
    return element;
  }

  /** Adds another collection to this one.
   * @param other The element to be added.t */
  public void addAll(@NotNull final ImmutableList<T> other) {
    data.addAll(other.data);
  }

  /** Adds another collection to this one.
   * @param other The element to be added.t */
  public void addAll(@NotNull final Set<T> other) {
    data.addAll(other);
  }

  public boolean contains(final Object ¢) {
    return data.contains(¢);
  }

  public boolean containsAll(@NotNull final Collection<?> ¢) {
    return data.containsAll(¢);
  }

  @Override public boolean equals(final @Nullable Object ¢) {
    if (¢ == this)
      return true;
    if (¢ == null || getClass() != ¢.getClass())
      return false;
    @Nullable @SuppressWarnings("unchecked") final ImmutableList<T> $ = (ImmutableList<T>) ¢;
    return data.equals($.data);
  }

  @Override public int hashCode() {
    return 31 * super.hashCode() + (data == null ? 0 : data.hashCode());
  }

  public boolean isEmpty() {
    return data.isEmpty();
  }

  @Override @NotNull public Iterator<T> iterator() {
    return data.iterator();
  }

  /** @return The number of elements in the collection. */
  public int size() {
    return data.size();
  }

  /** convert this collection into an array
   * @return an array of the elements stored in this collection */
  @NotNull public abstract T[] toArrary();

  @NotNull public Object[] toArray() {
    return data.toArray();
  }

  @NotNull public T[] toArray(@NotNull final T[] ¢) {
    return data.toArray(¢);
  }
}

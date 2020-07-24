/** Copyright */
package il.org.spatan.iteration;

import il.org.spartan.iterables.iterables.ReadonlyIterator;

/** An array {@linkplain "http://en.wikipedia.org/wiki/Adapter_pattern Adapter"}
 * adjusting it to the {@link Iterable} interface.
 * @author Yossi Gil
 * @since Oct 19, 2009
 * @param <T> type of objects in the array */
public class IterableArray<T> implements Iterable<T> {
  protected final T[] ts;

  /** Instantiate the adapter with an array
   * @param ts the array of T objects over which we iterate */
  public IterableArray(final T[] ts) {
    this.ts = ts;
  }
  public int count() {
    return ts.length;
  }
  @Override public ArrayIterator<T> iterator() {
    return new ArrayIterator<>(ts);
  }

  public static class ArrayIterator<T> extends ReadonlyIterator<T> {
    private int current;
    private final T[] ts;

    public ArrayIterator(final T[] ts) {
      this.ts = ts;
    }
    @Override public boolean hasNext() {
      return current < ts.length;
    }
    @Override public T next() {
      return ts[current++];
    }
  }
}

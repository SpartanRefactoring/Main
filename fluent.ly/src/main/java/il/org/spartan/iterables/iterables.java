/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import java.util.Iterator;

/**
 * No values in an 'enum' used as name space for a collection of 'static'
 * functions.
 *
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 */
public enum iterables {
  ;
  public abstract static class RangeIterator<T> extends ReadonlyIterator<T> {
    private final int n;
    private int i;

    public RangeIterator(final int n) {
      this.n = n;
    }

    @Override public final boolean hasNext() {
      return i < n;
    }

    @Override public T next() {
      final var $ = value();
      ++i;
      return $;
    }

    protected int i() {
      return i;
    }

    protected abstract T value();
  }

  public abstract static class ReadonlyIterator<T> implements Iterator<T> {
    @Override public final void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * An {@linkplain "http://en.wikipedia.org/wiki/Adapter_pattern Adapter"} of a
   * scalar object, adapting it to the {@link Iterable} interface whereby making
   * it possible to iterate over this object in the following sense:. If the
   * object is non-null, then the iteration will return the object and terminate.
   * If the object is null, then the iteration is vacuous.
   *
   * @author Yossi Gil
   * @since Oct 19, 2009
   * @param <T> type of objects in the array
   */
  public static class Singleton<T> implements Iterable<T> {
    public static <T> Iterable<T> make(final T ¢) {
      return ¢ == null ? null : new Singleton<>(¢);
    }

    T t;

    /**
     * Instantiate the adapter with an object
     *
     * @param t the object on which we can iterate.
     */
    public Singleton(final T t) {
      this.t = t;
    }

    @Override public Iterator<T> iterator() {
      return new iterables.ReadonlyIterator<>() {
        @Override public boolean hasNext() {
          return t != null;
        }

        @Override public T next() {
          final var $ = t;
          t = null;
          return $;
        }
      };
    }
  }
}

package il.org.spatan.iteration;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Makes a filtered version of a stream (realized by the {@link Iterable}
 * interface). Only those elements of the stream which satisfy a given boolean
 * Predicate are passed through.
 *
 * @author Yossi Gil
 * @since Oct 22, 2009
 * @param <T> type of objects in the stream.
 */
public abstract class FilteredIterable<T> implements Predicate<T>, Iterable<T> {
  /** The encapsulated stream. */
  Iterable<? extends T> iterable;

  public FilteredIterable(final Iterable<? extends T> iterable) {
    this.iterable = iterable;
  }

  /**
   * Filters the objects in the stream. Must be implemented by the client, which
   * should extend this class.
   *
   * @param t an element to be examined
   * @return true, if and only if, this element is to be passed through.
   */
  @Override public abstract boolean test(T t);

  @Override public final Iterator<T> iterator() {
    return new Iterator<>() {
      T pending;
      boolean hasNext = true;
      final Iterator<? extends T> iterator = iterable.iterator();
      {
        advance();
      }

      @Override public boolean hasNext() {
        return hasNext;
      }

      @Override public T next() {
        final T $ = pending;
        advance();
        return $;
      }

      private void advance() {
        while (iterator.hasNext())
          if (test(pending = iterator.next()))
            return;
        hasNext = false;
      }
    };
  }
}

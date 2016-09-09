package il.org.spatan.iteration;

import java.util.*;

import il.org.spartan.iteration.closures.*;
import il.org.spatan.iteration.Iterables.*;

/** Makes a filtered version of a stream (realized by the {@link Iterable}
 * interface). Only those elements of the stream which satisfy a given boolean
 * condition are passed through.
 * @author Yossi Gil
 * @since Oct 22, 2009
 * @param <T> type of objects in the stream. */
public abstract class FilteredIterable<T> implements Condition<T>, Iterable<T> {
  /** The encapsulated stream. */
  Iterable<? extends T> iterable;

  public FilteredIterable(final Iterable<? extends T> iterable) {
    this.iterable = iterable;
  }

  /** Filters the objects in the stream. Must be implemented by the client,
   * which should extend this class.
   * @param t an element to be examined
   * @return true, if and only if, this element is to be passed through. */
  @Override public abstract boolean holds(T t);

  @Override public final Iterator<T> iterator() {
    return new ReadonlyIterator<T>() {
      private T pending;
      private boolean hasNext = true;
      private final Iterator<? extends T> iterator = iterable.iterator();
      {
        advance();
      }

      @Override final public boolean hasNext() {
        return hasNext;
      }

      @Override final public T next() {
        final T $ = pending;
        advance();
        return $;
      }

      private void advance() {
        while (iterator.hasNext())
          if (holds(pending = iterator.next()))
            return;
        hasNext = false;
      }
    };
  }
}

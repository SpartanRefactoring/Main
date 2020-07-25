package a;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import fluent.ly.as;

/**
 * Singleton collections.
 *
 * @author Ori Roth
 * @since 2017-04-16
 */
public interface singleton {
  /** Singleton list. */
  static <T> List<T> list(final T ¢) {
    return as.list(¢);
  }

  /** Singleton array. */
  static <T> T[] array(final T ¢) {
    return as.array(¢);
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
   * @param <T> JD
   */
  public static <T> Iterable<T> iterator(final T ¢) {
    return new Iterable<>() {
      @Nullable T t = ¢;

      @Override public Iterator<T> iterator() {
        return new Iterator<>() {
          @Override public boolean hasNext() {
            return ¢ != null;
          }

          @Override public T next() {
            if (t == null)
              throw new IllegalStateException();
            assert t != null;
            final T $ = t;
            t = null;
            return $;
          }
        };
      }
    };
  }
}

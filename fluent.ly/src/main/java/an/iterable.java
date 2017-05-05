package an;

import java.util.*;

import org.jetbrains.annotations.*;

public interface iterable {
  /** <code>singleton</code>
   * @param <T> JD
   * @param ¢ JD
   * @return PureIterable.Sized<T> for returned value of method
   *         <code>singleton</code> */
  @NotNull static <T> Iterable<T> singleton(final T ¢) {
    return iterable.over(¢);
  }
  /** Creates an iterable for an array of objects
   * @param < T > an arbitrary type
   * @param ts what to iterate on
   * @return an {@link Iterable} over the parameter */
  @SafeVarargs @NotNull static <T> Iterable<T> over(@NotNull final T... ts) {
    return new Iterable<T>() {
      @Override @NotNull public Iterator<T> iterator() {
        return new Iterator<T>() {
          int current;

          @Override public boolean hasNext() {
            return current < ts.length;
          }
          @Override public T next() {
            return ts[current++];
          }
        };
      }
    };
  }
}

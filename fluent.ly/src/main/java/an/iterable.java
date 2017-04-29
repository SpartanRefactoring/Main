package an;

import org.jetbrains.annotations.NotNull;

import il.org.spartan.iterables.PureIterable;
import il.org.spartan.iterables.PureIterator;

public interface iterable {

  /** <code>singleton</code>
   * @param <T> JD
   * @param ¢ JD
   * @return PureIterable.Sized<T> for returned value of method
   *         <code>singleton</code> */
  @NotNull static <T> PureIterable.Sized<T> singleton(final T ¢) {
    return iterable.over(¢);
  }

  /** Creates an iterable for an array of objects
   * @param < T > an arbitrary type
   * @param ts what to iterate on
   * @return an {@link Iterable} over the parameter */
  @SafeVarargs @NotNull static <T> PureIterable.Sized<T> over(@NotNull final T... ts) {
    return new PureIterable.Sized<T>() {
      @Override @NotNull public PureIterator<T> iterator() {
        return new PureIterator<T>() {
          int current;
  
          @Override public boolean hasNext() {
            return current < ts.length;
          }
  
          @Override public T next() {
            return ts[current++];
          }
        };
      }
  
      @Override public int size() {
        return ts.length;
      }
    };
  }
}

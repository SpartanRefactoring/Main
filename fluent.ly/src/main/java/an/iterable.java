package an;

import java.util.*;

import org.eclipse.jdt.annotation.*;

public interface iterable {
  /** <code>singleton</code>
   * @param <T> JD
   * @param ¢ JD
   * @return PureIterable.Sized<T> for returned value of method
   *         <code>singleton</code> */
   static <T> Iterable<T> singleton(final T ¢) {
    return iterable.over(¢);
  }
  /** Creates an iterable for an array of objects
   * @param < T > an arbitrary type
   * @param ts what to iterate on
   * @return an {@link Iterable} over the parameter */
  @SafeVarargs  static <T> Iterable<T> over( final T... ts) {
    return new Iterable<T>() {
      @Override  public Iterator<T> iterator() {
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

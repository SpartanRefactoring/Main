package an;

import java.util.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-01 */
public enum empty {
  ;
  public static <T> List<T> list() {
    return new ArrayList<>();
  }

  public static <T> Iterable<T> iterable() {
    return () -> new Iterator<T>() {
      @Override public boolean hasNext() {
        return false;
      }

      @Override public T next() {
        return null;
      }
    };
  }
}

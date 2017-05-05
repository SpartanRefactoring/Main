package an;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import fluent.ly.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-01 */
public enum empty {
  ;
  public static void main(final String[] args) {
    azzert.that(the.first.of("Hello")).is("H");
  }
  public static <T> List<T> list() {
    return new ArrayList<>();
  }
  public static <@Nullable T> Iterable<T> iterable() {
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

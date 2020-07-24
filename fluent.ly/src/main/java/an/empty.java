package an;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fluent.ly.azzert;
import fluent.ly.the;

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
  @SuppressWarnings("unchecked") public static <T> T[] array() {
    return (T[]) new Object[0];
  }
  public static <T> Iterable<T> iterable() {
    return () -> new Iterator<>() {
      @Override public boolean hasNext() {
        return false;
      }
      @Override public T next() {
        return null;
      }
    };
  }
  public static <T> Set<T> set() {
    return new LinkedHashSet<>();
  }
  public static <K, V> Map<K, V> map() {
    return new LinkedHashMap<>();
  }
}

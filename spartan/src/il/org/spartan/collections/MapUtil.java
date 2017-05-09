package il.org.spartan.collections;

import java.util.*;

public class MapUtil {
  @SuppressWarnings("boxing") //
  public static <K> void addToValue(final Map<K, Integer> m, final K key, final int val) {
    m.put(key, (m.get(key) != null ? m.get(key) : Integer.valueOf(0)) + val);
  }
  public static <K, V> Iterator<K> keysIterator(final Map<K, V> m) {
    return new Iterator<K>() {
      Iterator<Map.Entry<K, V>> inner = m.entrySet().iterator();

      @Override public boolean hasNext() {
        return inner.hasNext();
      }
      @Override @SuppressWarnings("null") public K next() {
        return inner.next().getKey();
      }
      @Override public void remove() {
        inner.remove();
      }
    };
  }
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> m) {
    final List<Map.Entry<K, V>> list = new ArrayList<>(m.entrySet());
    Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    final Map<K, V> $ = new LinkedHashMap<>();
    for (final Map.Entry<K, V> ¢ : list)
      $.put(¢.getKey(), ¢.getValue());
    return $;
  }
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReverse(final Map<K, V> m) {
    final List<Map.Entry<K, V>> list = new ArrayList<>(m.entrySet());
    Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    final Map<K, V> $ = new LinkedHashMap<>();
    for (final Map.Entry<K, V> ¢ : list)
      $.put(¢.getKey(), ¢.getValue());
    return $;
  }
}

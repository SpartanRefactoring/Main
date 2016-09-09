package il.org.spartan.collections;

import java.util.*;

public class MapUtil {
  @SuppressWarnings("boxing") //
  public static <K> void addToValue(final Map<K, Integer> map, final K key, final int val) {
    Integer i = map.get(key);
    if (i == null)
      i = new Integer(0);
    i += val;
    map.put(key, i);
  }

  public static <K, V> Iterator<K> keysIterator(final Map<K, V> map) {
    return new Iterator<K>() {
      Iterator<Map.Entry<K, V>> inner = map.entrySet().iterator();

      @Override public boolean hasNext() {
        return inner.hasNext();
      }

      @Override public K next() {
        return inner.next().getKey();
      }

      @Override public void remove() {
        inner.remove();
      }
    };
  }

  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
    final List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    final Map<K, V> $ = new LinkedHashMap<>();
    for (final Map.Entry<K, V> entry : list)
      $.put(entry.getKey(), entry.getValue());
    return $;
  }

  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReverse(final Map<K, V> map) {
    final List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    final Map<K, V> $ = new LinkedHashMap<>();
    for (final Map.Entry<K, V> entry : list)
      $.put(entry.getKey(), entry.getValue());
    return $;
  }
}

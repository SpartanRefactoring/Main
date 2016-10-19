package il.org.spartan.collections;

import java.util.*;

import org.eclipse.jdt.annotation.*;

public class MapUtil {
  @SuppressWarnings("boxing") //
  public static <@Nullable K> void addToValue(final Map<K, Integer> k, final K key, final int val) {
    k.put(key, ((k.get(key) != null ? k.get(key) : Integer.valueOf(0)) + val));
  }

  public static <K, V> Iterator<K> keysIterator(final Map<K, V> k) {
    return new Iterator<K>() {
      Iterator<Map.Entry<K, V>> inner = k.entrySet().iterator();

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

  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> k) {
    final List<Map.Entry<K, V>> list = new ArrayList<>(k.entrySet());
    Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    final Map<K, V> $ = new LinkedHashMap<>();
    for (final Map.Entry<K, V> ¢ : list)
      $.put(¢.getKey(), ¢.getValue());
    return $;
  }

  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReverse(final Map<K, V> k) {
    final List<Map.Entry<K, V>> list = new ArrayList<>(k.entrySet());
    Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    final Map<K, V> $ = new LinkedHashMap<>();
    for (final Map.Entry<K, V> ¢ : list)
      $.put(¢.getKey(), ¢.getValue());
    return $;
  }
}

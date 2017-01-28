package il.org.spartan.collections;

import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

public class MapUtil {
  @SuppressWarnings("boxing") //
  public static <@Nullable K> void addToValue(@NotNull final Map<K, Integer> m, final K key, final int val) {
    m.put(key, (m.get(key) != null ? m.get(key) : Integer.valueOf(0)) + val);
  }

  @NotNull public static <K, V> Iterator<K> keysIterator(@NotNull final Map<K, V> m) {
    return new Iterator<K>() {
      @NotNull Iterator<Map.Entry<K, V>> inner = m.entrySet().iterator();

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

  @NotNull public static <@Nullable K, @Nullable V extends Comparable<? super V>> Map<K, V> sortByValue(@NotNull final Map<K, V> m) {
    @NotNull final List<Map.Entry<K, V>> list = new ArrayList<>(m.entrySet());
    Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    @NotNull final Map<K, V> $ = new LinkedHashMap<>();
    for (@NotNull final Map.Entry<K, V> ¢ : list)
      $.put(¢.getKey(), ¢.getValue());
    return $;
  }

  @NotNull public static <@Nullable K, @Nullable V extends Comparable<? super V>> Map<K, V> sortByValueReverse(@NotNull final Map<K, V> m) {
    @NotNull final List<Map.Entry<K, V>> list = new ArrayList<>(m.entrySet());
    Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    @NotNull final Map<K, V> $ = new LinkedHashMap<>();
    for (@NotNull final Map.Entry<K, V> ¢ : list)
      $.put(¢.getKey(), ¢.getValue());
    return $;
  }
}

package il.org.spartan.spartanizer.utils;

import java.util.*;

public final class MapEntry<K, V> implements Map.Entry<K, V> {
  private final K key;
  private V value;

  public MapEntry(final K key, final V value) {
    this.key = key;
    this.value = value;
  }

  @Override public K getKey() {
    return key;
  }

  @Override public V getValue() {
    return value;
  }

  @Override public V setValue(final V value) {
    final V $ = this.value;
    this.value = value;
    return $;
  }
}

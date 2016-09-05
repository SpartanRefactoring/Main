package il.org.spartan.refactoring.utils;

import java.util.*;

public final class MyEntry<K, V> implements Map.Entry<K, V> {
  private final K key;
  private V value;

  public MyEntry(final K key, final V value) {
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

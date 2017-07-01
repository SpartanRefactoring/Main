package il.org.spartan.spartanizer.utils;

import java.util.*;

// TODO Yossi Gil remove this class
public final class MapEntry<K, V> implements Map.Entry<K, V> {
  private final K key;
  private V value;

  public MapEntry(final K key, final V value) {
    this.key = key;
    this.value = value;
  }
  @Override public boolean equals(final Object ¢) {
    return ¢ != null && ¢.getClass() == getClass()
        && (((Map.Entry<?, ?>) ¢).getKey() == null && key == null || key.equals(((Map.Entry<?, ?>) ¢).getKey()))
        && (((Map.Entry<?, ?>) ¢).getValue() == null && value == null || value.equals(((Map.Entry<?, ?>) ¢).getValue()));
    // return iz.equal(this, az.mapEntry(¢));
  }
  @Override public K getKey() {
    return key;
  }
  @Override public V getValue() {
    return value;
  }
  @Override public int hashCode() {
    return (value == null ? 0 : value.hashCode()) + 31 * ((key == null ? 0 : key.hashCode()) + 31);
  }
  @Override public V setValue(final V value) {
    final V ret = this.value;
    this.value = value;
    return ret;
  }
}

package il.org.spartan.refactoring.utils;

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
  
  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }
  
  @Override public boolean equals(Object o){
    return o == null ? false : !(o.getClass() != getClass()) ? false : 
      (((MapEntry<?,?>) o).getKey() == null && key == null || key.equals(((MapEntry<?,?>) o).getKey())) &&
      (((MapEntry<?,?>) o).getValue() == null && value == null || value.equals(((MapEntry<?,?>) o).getValue()));
  }
}

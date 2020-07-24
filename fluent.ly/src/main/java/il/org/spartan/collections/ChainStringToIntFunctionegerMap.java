package il.org.spartan.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ChainStringToIntFunctionegerMap {
  public Map<String, Integer> inner = new HashMap<>();

  public boolean containsKey(final String key) {
    return inner.containsKey(key);
  }

  public boolean containsValue(final int value) {
    return inner.containsValue(Integer.valueOf(value));
  }

  public Set<Entry<String, Integer>> entrySet() {
    return inner.entrySet();
  }

  public int get(final Object key) {
    return inner.get(key).intValue();
  }

  public boolean isEmpty() {
    return inner.isEmpty();
  }

  public Set<String> keySet() {
    return inner.keySet();
  }

  public ChainStringToIntFunctionegerMap put(final String key, final int value) {
    assert !inner.containsKey(key);
    inner.put(key, Integer.valueOf(value));
    return this;
  }

  public ChainStringToIntFunctionegerMap putIfAbsent(final String key, final int value) {
    inner.putIfAbsent(key, Integer.valueOf(value));
    return this;
  }

  public ChainStringToIntFunctionegerMap putAll(final Map<? extends String, ? extends Integer> ¢) {
    inner.putAll(¢);
    return this;
  }

  public ChainStringToIntFunctionegerMap putOn(final int value, final String... keys) {
    for (final String key : keys)
      put(key, value);
    return this;
  }

  public ChainStringToIntFunctionegerMap remove(final String key) {
    inner.remove(key);
    return this;
  }

  public int size() {
    return inner.size();
  }

  public Collection<Integer> values() {
    return inner.values();
  }
}
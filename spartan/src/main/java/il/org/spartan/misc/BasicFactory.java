package il.org.spartan.misc;

import java.util.HashMap;
import java.util.Map;

/** This cache class allows a value to be retrieved by either by a String key or
 * by another value. If the key object was used in the past, the returned value
 * is the previously created object.
 * @param <V> The Value type */
public abstract class BasicFactory<V> implements FactoryConcept<V> {
  private final Map<V, V> map = new HashMap<>();

  public V from(final V k) {
    final V $ = map.get(k);
    if ($ != null)
      return $;
    map.put(k, k);
    return k;
  }
  @Override public V fromString(final String ¢) {
    return from(stringToValue(¢));
  }
  protected abstract V stringToValue(String s);
}

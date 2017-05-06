package il.org.spartan.misc;

import static fluent.ly.___.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.annotation.*;

/** A generic class realizing a cached buffer of elements of an arbitrary kind,
 * but indexed by {@link String}s. If a fetched element is not in the cache,
 * then the class uses its internal {@link Factory} object to create the given
 * element. The implementation of this class uses delegation (to a {@link Map}
 * object) rather than inheritance. This design decision is due to the signature
 * of {@link Map#get(Object)} method, which admits any {@link Object } as its
 * parameter, which may lead to subtle runtime bugs.
 * <p>
 * By using delegation we can define the get() method to accept only
 * {@link String}s thus achieving a higher level of static type safety.
 * @author Yossi Gil
 * @since 16/06/2007
 * @param <T> The type of elements stored in the cache.
 * @see Factory */
public class Cache<T> implements Iterable<Map.Entry<String, T>> {
  /** The {@link Factory} used for creating objects if they are not in the
   * cache. */
  private final Factory<T> factory;
  /** A map that stores cached objects, keyed by their name */
  private final Map<String, T> map = new HashMap<>();
  /** <code><b>true</b></code> <em>iff</em>the cache holds all possible keys. */
  private boolean exhaustive;

  /** Initialize a cache using a given {@link Factory}.
   * @param factory to be used for creating objects not in the cache. */
  public Cache(final Factory<T> factory) {
    this.factory = factory;
  }
   public Collection<T> all() {
    if (!exhaustive)
      map.putAll(factory.all());
    exhaustive = true;
    return map.values();
  }
  /** fetch an element from the cache, but if it is not there, try to create it,
   * and insert it into the cache.
   * @param key the key identifying the element to be fetched, empty string is
   *        fine.
   * @return the element identified by this key. */
  public T get(final String key) {
    notNull(key);
    T $ = map.get(key);
    if ($ != null || exhaustive)
      return $;
    $ = factory.make(key);
    map.put(key, $);
    return $;
  }
  /** Obtain an iterator over the cached object
   * @return Iterator of Entry<String,T> */
  @Override  public Iterator<Entry<String, T>> iterator() {
    return map.entrySet().iterator();
  }
  /** Store the given value in the cache, and associate it with the given key.
   * @param key key with which the specified value is to be associated.
   * @param t value to be stored in the cache.
   * @return previous value associated with specified key, or <tt>null</tt>. */
  public T put(final String key, final T t) {
    return map.put(key, t);
  }
  /** Size of cache
   * @return Number of elements currently stored in the cache */
  public int size() {
    return map.size();
  }

  /** A simple abstract factory, allowing its clients to generate objects not
   * present in the {@link Cache} .
   * @author Yossi Gil
   * @since 16/06/2007
   * @param <T> the type of objects that this factory generates. */
  public abstract static class Factory<T> {
    /** retrieve the universal set, i.e., the set of all possible objects,
     * indexed by their respective keys.
     * @return the base implementation returns the empty set, but it is meant to
     *         be overridden by concrete classes inheriting from
     *         {@link Cache.Factory} */
     public Map<String, T> all() {
      return new HashMap<>();
    }
    /** create a new object from its {@link String} name
     * @param key the identifier for the newly created object.
     * @return the newly created object, or <code><b>null</b></code> if no such
     *         object can be created. */
     public abstract T make(String key);
  }
}

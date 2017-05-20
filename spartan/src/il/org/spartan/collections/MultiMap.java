// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.collections;

import java.util.*;

import il.org.spartan.streotypes.*;

/** import static il.org.spartan.utils.Box.*; import java.util.*; import
 * java.util.Map.*; import il.org.spartan.utils.*; /** import
 * il.org.spartan.stereotypes.Canopy; import java.util.HashMap; import
 * java.util.HashSet; import java.util.Iterator; import java.util.Map; import
 * java.util.Set; /** A 1-to-many mapping, that is: a relation where each source
 * value is associated with several "images". This class is in fact a
 * <b>façade</b> for an instance of the JRE's {@link Map} in which the mapping
 * range is a {@link Set}.
 * @author Itay Maman, The Technion
 * @param <K> Type of source values
 * @param <V> Type of images */
@Canopy
public final class MultiMap<K, V> implements Iterable<K> {
  /** Actual implementation */
  private final HashMap<K, Set<V>> implementation;

  /** Create a new empty {@link MultiMap} */
  public MultiMap() {
    implementation = new HashMap<>();
  }
  /** Create a new empty {@link MultiMap} with a given capacity
   * @param initialCapacity initial capacity of the map */
  public MultiMap(final int initialCapacity) {
    this(initialCapacity, 0.75f);
  }
  /** Create a new empty {@link MultiMap} with a given capacity and load factor
   * @param initialCapacity initial capacity of the map
   * @param loadFactor recreate map if it is filled to this extent */
  public MultiMap(final int initialCapacity, final float loadFactor) {
    implementation = new HashMap<>(initialCapacity, loadFactor);
  }
  /** Clear the set of all images of the given source value
   * @param ¢ Source value
   * @return the newly created set object */
  public Set<V> clear(final K ¢) {
    final Set<V> $ = new HashSet<>();
    implementation.put(¢, $);
    return $;
  }
  /** Find the set of all images of the given source value. If this key does not
   * exist yet, add it with an empty set.
   * @param ¢ key value
   * @return A non-<code><b>null</b></code> representing the set of images
   *         associated with <code>k</code> */
  public Set<V> get(final K ¢) {
     final Set<V> $ = implementation.get(¢);
    return $ != null ? $ : clear(¢);
  }
  /** Return an iterator over all keys
   * @return Iterator<K> object */
  @Override public Iterator<K> iterator() {
    return implementation.keySet().iterator();
  }
  public Set<K> keySet() {
    return implementation.keySet();
  }
  /** Add an image to the given source value
   * @param k Source value
   * @param v Image value */
  public void put(final K k, final V v) {
    get(k).add(v);
  }
  /** How many keys are stored in this map?
   * @return number of keys */
  public int size() {
    return implementation.size();
  }
  @Override public String toString() {
    final StringBuilder $ = new StringBuilder();
    for (final K ¢ : this)
      $.append(¢ + "=>" + get(¢) + '\n');
    return $ + "";
  }
  /** Obtain all images
   * @return Set of V objects */
  public Set<V> values() {
    final Set<V> $ = new HashSet<>();
    implementation.values().forEach($::addAll);
    return $;
  }
}

// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.collections;

import il.org.spartan.streotypes.Canopy;
import il.org.spartan.streotypes.Classical;
import il.org.spartan.streotypes.Instantiable;

/** A mutable map, associating an <code><b>int</b></code> value with each value
 * of an enumerated type.
 * @param <E> an <code><b>enum</b></code> type, whose values are to be
 *        associated with <code><b>int</b></code> values by this map.
 * @see ImmutableEnumIntMap
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Canopy
@Classical
@Instantiable
public class MutableEnumIntMap<E extends Enum<E>> implements EnumIntMap<E> {
  /** Suite metric values are stored internally here. */
  private final int[] implementation;

  /** Initialize this class, with a map associating a zero with each of the
   * enumerated type values.
   * @param dummy some arbitrary, non-<code><b>null</b></code> value of type
   *        <code>E</code>, used for figuring out the number of distinct
   *        enumerated values in the type <code>E</code>. An elegant way of
   *        passing such a dummy value is by fetching the first enumerated
   *        value, as follows
   *
   *        <pre>
   *        MutableIntMap&lt;E&gt; mutableMap = new MutableIntMap&lt;E&gt;(E.values()[0]);
   *        </pre>
  */
  public MutableEnumIntMap(final E dummy) {
    assert dummy != null;
    this.implementation = new int[dummy.getClass().getEnumConstants().length];
  }
  /** Add to the value associated with a specific <code><b>enum</b></code>
   * value.
   * @param e some non-<code><b>null</b></code> value of type <code>E</code>.
   * @param value what to add to the value associated with this enumerated type
   *        value. */
  public void add(final E e, final int value) {
    assert e != null;
    implementation[e.ordinal()] += value;
  }
  public ImmutableEnumIntMap<E> asImmutable() {
    return new ImmutableEnumIntMap<>(implementation);
  }
  @Override public int get(final E ¢) {
    return implementation[¢.ordinal()];
  }
  /** Increment the value associated with a specific <code><b>enum</b></code>
   * value.
   * @param ¢ some non-<code><b>null</b></code> value of type <code>E</code>. */
  public void increment(final E ¢) {
    assert ¢ != null;
    ++implementation[¢.ordinal()];
  }
  /** Set the value associated with a specific <code><b>enum</b></code> value.
   * @param e some non-<code><b>null</b></code> value of type <code>E</code>.
   * @param value new value to be associated with <code>e</code>. */
  public void set(final E e, final int value) {
    assert e != null;
    implementation[e.ordinal()] = value;
  }
}

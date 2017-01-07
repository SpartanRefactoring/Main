// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.collections;

/** Base interface of classes mapping enumerated values to <code>int</code>s.
 * Such classes give a structured means for using values of an
 * <code><b>enum</b></code> as array indices.
 * @param <E> an <code><b>enum</b></code> type, whose values are to be
 *        associated with <code><b>int</b></code> values by this map.
 * @see ImmutableEnumIntMap
 * @see MutableEnumIntMap
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
public interface EnumIntMap<E extends Enum<E>> {
  /** Retrieve the value associated with a given enumerated value.
   * @param e some non-<code><b>null</b></code> value of type <code>E</code>.
   * @return the <code><b>int</b></code> value associated with the parameter. */
  int get(E e);
}

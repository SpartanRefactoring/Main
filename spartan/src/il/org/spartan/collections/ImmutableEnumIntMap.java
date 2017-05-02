// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.collections;

import static fluent.ly.___.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** /** An immutable map, associating an <code><b>int</b></code> value with each
 * value of an enumerated type.
 * @param <E> an <code><b>enum</b></code> type, whose values are to be
 *        associated with <code><b>int</b></code> values by this map.
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008
 * @see MutableEnumIntMap */
@Canopy @Classical @Immutable public class ImmutableEnumIntMap<E extends Enum<E>> extends ImmutableArray.Ints implements EnumIntMap<E> {
  /** Initialize an instance with a given array of appropriate size.
   * @param is an array whose <i>i<sup>th</sup></i> entry is the value of the
   *        <i>i<sup>th</sup></i> enumerated value. The number of entries in
   *        this array must be the same as the number of enumerated value. */
  public ImmutableEnumIntMap(final int[] is) {
    super(is);
  }

  /* (non-Javadoc)
   *
   * @see il.org.spartan.collections.EnumIntMap#get(E) */
  @Override public int get(@NotNull final E ¢) {
    sure(length() == ¢.getClass().getEnumConstants().length);
    return get(¢.ordinal());
  }
}

// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.tables;

import java.util.*;
import java.util.Map.*;

import il.org.spartan.external.*;
import il.org.spartan.utils.*;

/** Represents a row of a {@link Table}
 * @param <Self>
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-04 */
public abstract class Row<Self extends Row<?>> extends LinkedHashMap<String, Object> {
  public Row() {
    reset();
  }

  public Self col(final Accumulator ¢) {
    return col(¢.name(), ¢.value());
  }

  public Self col(final Accumulator... as) {
    for (final Accumulator ¢ : as)
      col(¢);
    return self();
  }

  public Self col(final Enum<?> key, final int value) {
    return col(key + "", value + "");
  }

  public Self col(final Enum<?> key, final String value) {
    return col(key + "", value);
  }

  /** Add a key without a value to this instance.
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @return this */
  public final Self col(final String key) {
    return col(key, "");
  }

  /** Add a key and a <code><b>char</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self col(final String key, final char value) {
    return col(key, value + "");
  }

  /** Add a key and a <code><b>double</b><code> value to this instance
                          * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self col(final String key, final double value) {
    return col(key, value + "");
  }

  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self col(final String key, final int value) {
    return col(key, value + "");
  }

  /** Add a key and a general {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self col(final String key, final Integer value) {
    return value == null ? col(key) : col(key, value.intValue());
  }

  /** Add a key and a <code><b>long</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self col(final String key, final long value) {
    return col(key, value + "");
  }

  /** Add a key and a non specific {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self col(final String key, final Object value) {
    return value == null ? col(key) : col(key, value + "");
  }

  public final Self col(final String key, final Object a[], final int i) {
    return col(key, a == null || i < 0 || i >= a.length ? null : a[i]);
  }

  public final Self col(final String key, final Object[] os) {
    return col(key, os == null ? null : Separate.by(os, ARRAY_SEPARATOR));
  }

  /** A mutator to add a key and a general {@link String} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self col(final String key, final String value) {
    super.put(key, value);
    return self();
  }

  /** Adds all {@link External} properties in a given object.
   * @param t an arbitrary object, usually with some of its fields and methods
   *        marked {@link External}
   * @return the parameter */
  public <T> T extract(final T $) {
    for (final Entry<String, String> ¢ : External.Introspector.toOrderedMap($).entrySet())
      col(¢.getKey(), ¢.getValue());
    return $;
  }

  protected abstract Self reset();

  protected abstract Self self();

  public static final String ARRAY_SEPARATOR = ";";
  private static final long serialVersionUID = 1L;
}

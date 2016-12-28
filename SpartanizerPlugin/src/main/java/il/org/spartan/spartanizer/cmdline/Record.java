// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.spartanizer.cmdline;

import java.util.*;
import java.util.Map.*;

import il.org.spartan.external.*;
import il.org.spartan.utils.*;

public abstract class Record<Self extends Record<?>> extends LinkedHashMap<String, Object> {
  private static final long serialVersionUID = 1L;
  public static final String ARRAY_SEPARATOR = ";";

  public Record() {
    reset();
  }

  protected abstract Self reset();

  /** Adds all {@link External} properties in a given object.
   * @param t an arbitrary object, usually with some of its fields and methods
   *        marked {@link External}
   * @return the parameter */
  public <T> T extract(final T $) {
    for (final Entry<String, String> ¢ : External.Introspector.toOrderedMap($).entrySet())
      put(¢.getKey(), ¢.getValue());
    return $;
  }

  public Self put(final Accumulator ¢) {
    return put(¢.name(), ¢.value());
  }

  public Self put(final Accumulator... as) {
    for (final Accumulator ¢ : as)
      put(¢);
    return self();
  }

  protected abstract Self self();

  public Self put(final Enum<?> key, final int value) {
    return put(key + "", value + "");
  }

  public Self put(final Enum<?> key, final String value) {
    return put(key + "", value);
  }

  /** Add a key without a value to this instance.
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @return this */
  public final Self put(final String key) {
    return put(key, "");
  }

  /** Add a key and a <code><b>boolean</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self put(final String key, final boolean value) {
    return put(key, value + "");
  }

  /** Add a key and a <code><b>char</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self put(final String key, final char value) {
    return put(key, value + "");
  }

  /** Add a key and a <code><b>double</b><code> value to this instance
                  * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self put(final String key, final double value) {
    return put(key, value + "");
  }

  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self put(final String key, final int value) {
    return put(key, value + "");
  }

  /** Add a key and a general {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self put(final String key, final Integer value) {
    return value == null ? put(key) : put(key, value + "");
  }

  /** Add a key and a <code><b>long</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self put(final String key, final long value) {
    return put(key, value + "");
  }

  /** Add a key and a general {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  @Override public final Self put(final String key, final Object value) {
    return value == null ? put(key) : put(key, value + "");
  }

  public final Self put(final String key, final Object a[], final int i) {
    return put(key, a == null || i < 0 || i >= a.length ? null : a[i]);
  }

  public final Self put(final String key, final Object[] os) {
    return put(key, os == null ? null : Separate.by(os, ARRAY_SEPARATOR));
  }

  /** A mutator to add a key and a general {@link String} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self put(final String key, final String value) {
    super.put(key, value);
    return self();
  }
}

// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.tables;

import java.util.*;

import il.org.spartan.*;
import il.org.spartan.external.*;
import il.org.spartan.utils.*;

/** Represents a row of a {@link Table}
 * @param <Self> uses for fluent API, type of subclass
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-04 */
public abstract class Row<Self extends Row<?>> extends LinkedHashMap<String, Object> {
  private static final long serialVersionUID = 429192337773634368L;

  public Row() {
    reset();
  }

  public Self col(final Accumulator ¢) {
    return col(¢.name(), ¢.value());
  }

  public Self col(final Accumulator... ¢) {
    as.list(¢).forEach(this::col);
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
   * @return <code><b>this</b></code> */
  public final Self col(final String key) {
    return col(key, "");
  }

  /** Add a key and a <code><b>char</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public final Self col(final String key, final char value) {
    return col(key, value + "");
  }

  /** Adds a key and a <code><b>double</b><code> value to this instance 
                  * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public Self col(final String key, final double value) {
    return col(key, value + "");
  }

  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public Self col(final String key, final int value) {
    return col(key, value + "");
  }

  /** Add a key and a general {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public final Self col(final String key, final Integer value) {
    return value == null ? col(key) : col(key, value.intValue());
  }

  /** Add a key and a <code><b>long</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public Self col(final String key, final long value) {
    return col(key, value + "");
  }

  /** Add a key and a non specific {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public final Self col(final String key, final Object value) {
    if (value == null)
      return col(key);
    super.put(key, value);
    return self();
  }

  public final Self col(final String key, final Object[] a, final int i) {
    return col(key, a == null || i < 0 || i >= a.length ? null : a[i]);
  }

  public final Self col(final String key, final Object... os) {
    return col(key, os == null || os.length == 0 ? null : (Object) os);
  }

  /** A mutator to add a key and a general {@link String} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return <code><b>this</b></code> */
  public final Self col(final String key, final String value) {
    super.put(key, value);
    return self();
  }

  /** Adds all {@link External} properties in a given object.
   * @param t an arbitrary object, usually with some of its fields and methods
   *        marked {@link External}
   * @return the parameter */
  public <T> T extract(final T $) {
    External.Introspector.toOrderedMap($).entrySet().forEach(λ -> col(λ.getKey(), λ.getValue()));
    return $;
  }

  protected abstract Self reset();

  protected abstract Self self();

  public static final String ARRAY_SEPARATOR = "; ";
  
}

// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.utils.Box.*;

import java.util.*;
import java.util.Map.*;

import il.org.spartan.*;
import il.org.spartan.Aggregator.*;
import il.org.spartan.Aggregator.Aggregation.*;
import il.org.spartan.external.*;
import il.org.spartan.utils.*;

public abstract class Record<Self extends Record<?>> extends LinkedHashMap<String, Object> {
  private static final long serialVersionUID = 1L;
  public static final String ARRAY_SEPARATOR = ";";
  protected final Aggregator aggregator = new Aggregator();

  public boolean aggregating() {
    return !aggregator.isEmpty();
  }

  public Record() {
    reset();
  }
  
 protected abstract Self reset() ; 


  public Iterable<Aggregation> aggregations() {
    return aggregator.aggregations();
  }

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
    return put(key, value, new FormatSpecifier[0]);
  }

  /** Add a key and a <code><b>double</b><code> value to this instance
                           *
                           * &#64;param key
                           *          The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @param ss Which (if any) aggregate statistics should be produced for this
   *        column
   * @return this */
  public Self put(final String key, final double value, final FormatSpecifier... ss) {
    aggregator.record(key, value, ss);
    return put(key, value + "");
  }

  /** Add a key and a <code><b>double</b><code> value to this instance
                           *
                           * &#64;param key
                           *          The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @param format How should the value be formatted
   * @param ss Which (if any) aggregate statistics should be produced for this
   *        column
   * @return this */
  public Self put(final String key, final double value, final String format, final FormatSpecifier... ss) {
    aggregator.record(key, value, ss);
    ___.sure(ss.length == 0 || aggregating());
    return put(key, String.format(format, box(value)));
  }

  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public Self put(final String key, final int value) {
    return put(key, value + "");
  }

  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @param format How should this value be formatted?
   * @param ss List of aggregations to collect on this column and their
   *        respective formatting
   * @return this */
  public Self put(final String key, final int value, final String format, final FormatSpecifier... ss) {
    aggregator.record(key, value, ss);
    ___.sure(ss.length == 0 || aggregating());
    return put(key, String.format(format, box(value)));
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

  /** A mutator to add a key and a general {@link String} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final Self putAggregatorColumn(final String key, final String value) {
    aggregator.markColumn(key);
    return put(key, value);
  }

  protected void addAggregates(final AbstractStringProperties to, final Aggregation a) {
    aggregator.addAggregates(keySet(), to, a);
  }
}

// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import static fluent.ly.box.*;

import java.util.*;
import java.util.Map.*;

import fluent.ly.*;
import il.org.spartan.Aggregator.*;
import il.org.spartan.Aggregator.Aggregation.*;
import il.org.spartan.external.*;
import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Separate.*;

/** Create a line in an "Comma Separated Values" format from a sequence of named
 * values.
 * @author Yossi Gil */
@Instantiable
public abstract class CSVLine extends AbstractStringProperties implements Cloneable {
  /** Separator of multi-values, i.e., array elements stored in a single
   * field */
  public static final String ARRAY_SEPARATOR = ";";
  final Map<String, String> map;
  protected final Aggregator aggregator = new Aggregator();

  /** Instantiate {@link CSVLine}.
   * @param map which implementation should we use for storing values */
  CSVLine(final Map<String, String> map) {
    this.map = map;
  }
  /** Instantiate {@link CSVLine}.
   * @param renderer
   * @param map which implementation should we use for storing values */
  CSVLine(final Renderer renderer, final Map<String, String> map) {
    super(renderer);
    this.map = map;
  }
  public boolean aggregating() {
    return !aggregator.isEmpty();
  }
  public Iterable<Aggregation> aggregations() {
    return aggregator.aggregations();
  }
  public final String asKeyValuePairs() {
    return Separate.by((F<Entry<String, String>>) λ -> λ.getKey() + "=" + λ.getValue(), entries(), ", ");
  }
  public final Iterable<? extends Map.Entry<String, String>> entries() {
    return map.entrySet();
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
  @Override public String get(final String key) {
    return map.get(key);
  }
  @Override public Collection<String> keys() {
    return map.keySet();
  }
  public CSVLine put(final Accumulator ¢) {
    return put(¢.name(), ¢.value());
  }
  public CSVLine put(final Accumulator... as) {
    for (final Accumulator ¢ : as)
      put(¢);
    return this;
  }
  public CSVLine put(final Enum<?> key, final int value) {
    return put(key + "", value + "");
  }
  public CSVLine put(final Enum<?> key, final String value) {
    return put(key + "", value);
  }
  /** Add a key without a value to this instance.
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @return this */
  public final CSVLine put(final String key) {
    return put(key, "");
  }
  /** Add a key and a <code><b>boolean</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final CSVLine put(final String key, final boolean value) {
    return put(key, value + "");
  }
  /** Add a key and a <code><b>char</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final CSVLine put(final String key, final char value) {
    return put(key, value + "");
  }
  /** Add a key and a <code><b>double</b><code> value to this instance
                                  *
                                  * &#64;param key
                                  *          The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public CSVLine put(final String key, final double value) {
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
  public CSVLine put(final String key, final double value, final FormatSpecifier... ss) {
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
  public CSVLine put(final String key, final double value, final String format, final FormatSpecifier... ss) {
    aggregator.record(key, value, ss);
    ___.sure(ss.length == 0 || aggregating());
    return put(key, String.format(format, box(value)));
  }
  /** Add a key and a general <code><b>float</b><code> value to this instance
                                  *
                                  * &#64;param key
                                  *          The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final CSVLine put(final String key, final float value) {
    return put(key, value + "");
  }
  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public CSVLine put(final String key, final int value) {
    return put(key, value + "");
  }
  /** Add a key and an <code><b>int</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @param format How should this value be formatted?
   * @param ss List of aggregations to collect on this column and their
   *        respective formatting
   * @return this */
  public CSVLine put(final String key, final int value, final String format, final FormatSpecifier... ss) {
    aggregator.record(key, value, ss);
    ___.sure(ss.length == 0 || aggregating());
    return put(key, String.format(format, box(value)));
  }
  /** Add a key and a general {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final CSVLine put(final String key, final Integer value) {
    return value == null ? put(key) : put(key, value + "");
  }
  /** Add a key and a <code><b>long</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public CSVLine put(final String key, final long value) {
    return put(key, value + "");
  }
  /** Add a key and a general {@link Object} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>; must
   *        not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final CSVLine put(final String key, final Object value) {
    return value == null ? put(key) : put(key, value + "");
  }
  public final CSVLine put(final String key, final Object a[], final int i) {
    return put(key, a == null || i < 0 || i >= a.length ? null : a[i]);
  }
  public final CSVLine put(final String key, final Object[] os) {
    return put(key, os == null ? null : Separate.by(os, ARRAY_SEPARATOR));
  }
  /** Add a key and a <code><b>short</b></code> value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public CSVLine put(final String key, final short value) {
    return put(key, value + "");
  }
  /** A mutator to add a key and a general {@link String} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  @Override public final CSVLine put(final String key, final String value) {
    map.put(key, value);
    return this;
  }
  /** A mutator to add a key and a general {@link String} value to this instance
   * @param key The key to be added; must not be <code><b>null</b></code>
   * @param value The value associated with the key
   * @return this */
  public final CSVLine putAggregatorColumn(final String key, final String value) {
    aggregator.markColumn(key);
    return put(key, value);
  }
  @Override public int size() {
    return map.size();
  }
  @Override public Collection<String> values() {
    return map.values();
  }
  protected void addAggregates(final AbstractStringProperties to, final Aggregation a) {
    aggregator.addAggregates(map.keySet(), to, a);
  }

  @Canopy
  public static class Ordered extends CSVLine {
    public Ordered() {
      super(new LinkedHashMap<String, String>());
    }
    public Ordered(final Renderer renderer) {
      super(renderer, new LinkedHashMap<String, String>());
    }

    public class Separated extends CSVLine.Ordered {
      final String separator;

      public Separated(final String separator) {
        this.separator = separator;
      }
    }
  }

  @Canopy
  public static final class Sorterd extends CSVLine {
    public Sorterd() {
      super(new TreeMap<String, String>());
    }
  }
}

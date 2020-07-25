package il.org.spartan.collections;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.util.Arrays;

import fluent.ly.___.Bug.Invariantable;
import fluent.ly.as;
import fluent.ly.azzert;

/** An unsorted map of integers.
 * <p>
 * Implementation employs a linear hash table s, with quadratic probes (i.e.,
 * +1, +3, +6, +10, ... modulo the table size). Array capacity is always a power
 * of two, and is doubled when the load goes above {@link #MAX_LOAD}; capacity
 * is halved when the load drops below {@value #MIN_LOAD}. When the fraction of
 * removed keys goes below {@link #REMOVE_LOAD}, the table is rehashed.
 *
 * @author Yossi Gil
 * @since December 2010 */
public final class IntegersMap {
  public static final float MAX_LOAD = 0.75f;
  public static final float MIN_LOAD = 0.25f;
  public static final float REMOVE_LOAD = 0.20f;
  public static final int MIN_CAPACITY = 4;

  static int hash(final int ¢) {
    return ¢ ^ ¢ >>> 12 ^ ¢ >>> 20 ^ (¢ ^ ¢ >>> 12 ^ ¢ >>> 20) >>> 4 ^ (¢ ^ ¢ >>> 12 ^ ¢ >>> 20) >>> 7;
  }

  private static int roundUp(final int ¢) {
    int $ = 1;
    while ($ < ¢)
      $ <<= 1;
    return $;
  }

  private int[] values;
  private int[] data;
  private boolean[] occupied;
  private boolean[] placeholder;
  private int size;
  private int removed;

  /** Instantiate this class */
  public IntegersMap() {
    this(MIN_CAPACITY);
  }

  /** Instantiate this class, using a given size for the hash table.
   *
   * @param initialCapacity suggests a hash table size, will be rounded up to the
   *                        next power of two. */
  public IntegersMap(final int initialCapacity) {
    reset(Math.max(MIN_CAPACITY, roundUp(initialCapacity)));
  }

  /** What's the underlying table size?
   *
   * @return the hash table size (always a power of two) */
  public int capacity() {
    return data.length;
  }

  /** Remove all elements from this set, preserving capacity.
   *
   * @return <code><b>this</b>/code> */
  public IntegersMap clear() {
    return reset(capacity());
  }

  /** Determine whether a given value is in this set.
   *
   * @param ¢ an arbitrary integer
   * @return <code><b>true</b></code> if, and only if, the parameter is contained
   *         in this set. */
  public boolean contains(final int ¢) {
    return location(¢) >= 0;
  }

  /** Check whether an array of integers is contained in this set.
   *
   * @param is an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, all elements in the array
   *         are contained in this set */
  public boolean contains(final int... is) {
    for (final int ¢ : is)
      if (!contains(¢))
        return false;
    return true;
  }

  /** Check whether this object is disjoint from an array of integers
   *
   * @param is an array of of integers; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, this object is disjoint
   *         from the set of elements in the parameter */
  public boolean disjoint(final int... is) {
    for (final int ¢ : is)
      if (contains(¢))
        return false;
    return true;
  }

  public int[] get(final int keys[]) {
    final int[] $ = new int[keys.length];
    for (int ¢ = 0; ¢ < keys.length; ++¢)
      $[¢] = get(keys[¢]);
    return $;
  }

  public int get(final int key) {
    return values[location(key)];
  }

  public int increment(final int key) {
    final int $ = location(key);
    if ($ >= 0)
      return ++values[$];
    final int find = find(key);
    data[find] = key;
    values[find] = 1;
    occupied[find] = true;
    if (++size > MAX_LOAD * capacity())
      rehash(data.length << 1);
    return 1;
  }

  /** @param key
   * @return <code>this</code> */
  public IntegersMap init(final int key) {
    final int location = location(key);
    if (location >= 0)
      values[location] = 0;
    else {
      final int find = find(key);
      data[find] = key;
      values[find] = 0;
      occupied[find] = true;
    }
    if (++size > MAX_LOAD * capacity())
      rehash(data.length << 1);
    return this;
  }

  /** What are all values stored in this object?
   *
   * @return an array of all elements in this set. */
  public int[] keys() {
    final int[] $ = new int[size];
    for (int ¢ = 0, j = 0; ¢ < capacity(); ++¢)
      if (occupied[¢] && !placeholder[¢])
        $[j++] = data[¢];
    return $;
  }/*
   * What are all values stored in this object?
   *
   * @return an array of all elements in this set.
   */

  public IntegersMap put(final int key, final int value) {
    final int location = location(key);
    if (location >= 0)
      values[location] = value;
    else {
      final int find = find(key);
      data[find] = key;
      values[find] = value;
      occupied[find] = true;
    }
    if (++size > MAX_LOAD * capacity())
      rehash(data.length << 1);
    return this;
  }

  /** Recreate the table, inserting all elements in it afresh.
   *
   * @return <code><b>this</b>/code> */
  public IntegersMap rehash() {
    return rehash(capacity());
  }

  /** Remove an array of integers to this set, if they are in it.
   *
   * @param is an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>this</b>/code> */
  public IntegersMap remove(final int... is) {
    for (final int ¢ : is)
      remove(¢);
    return this;
  }

  /** Remove an element from this set, it is in it
   *
   * @param n some integer to be removed from the set
   * @return <code><b>this</b>/code> */
  public IntegersMap remove(final int n) {
    final int i = location(n);
    assert i >= -1 && i < capacity();
    if (i < 0)
      return this;
    assert occupied[i] && data[i] == n;
    placeholder[i] = true;
    return --size < MIN_LOAD * capacity() && capacity() > MIN_CAPACITY ? rehash(data.length >> 1)
        : ++removed > REMOVE_LOAD * capacity() ? rehash() : this;
  }

  /** How many elements are there in this set?
   *
   * @return the number of values in the set. */
  public int size() {
    return size;
  }

  public int[] sortedKeys() {
    final int[] $ = keys();
    Arrays.sort($);
    return $;
  }

  /** Find the index in the hash table into which the parameter could be inserted.
   *
   * @param i some integer
   * @return -1 if the parameter is in the table already, otherwise, the index at
   *         which it could be safely inserted. */
  int find(final int i) {
    for (int $ = -1, ¢ = hash(i), t = 0;; ¢ += ++t) {
      ¢ &= data.length - 1;
      if (placeholder[¢] || !occupied[¢])
        $ = $ < 0 ? ¢ : $;
      if (!occupied[¢])
        return $;
      if (data[¢] == i)
        return -1;
    }
  }

  /** Find the index in the hash table of the parameter
   *
   * @param i some integer
   * @return index of the element if the parameter is in the table, otherwise,
   *         -1; */
  int location(final int i) {
    for (int $ = hash(i), ¢ = 0;; $ += ++¢) {
      $ &= data.length - 1;
      if (!occupied[$])
        return -1;
      if (!placeholder[$] && data[$] == i)
        return $;
    }
  }

  /** resize internal storage to the specified capacity, which must be a power of
   * two.
   *
   * @param newCapacity new initialCapacity for the internal array
   * @return <code><b>this</b>/code> */
  private IntegersMap rehash(final int newCapacity) {
    assert (newCapacity & newCapacity - 1) == 0;
    assert newCapacity >= MIN_CAPACITY;
    final int[] keys = keys(), oldValues = get(keys);
    reset(newCapacity);
    for (int ¢ = 0; ¢ < keys.length; ++¢)
      put(keys[¢], oldValues[¢]);
    return this;
  }

  private IntegersMap reset(final int capacity) {
    data = new int[capacity];
    occupied = new boolean[capacity];
    placeholder = new boolean[capacity];
    values = new int[capacity];
    size = removed = 0;
    return this;
  }

  @SuppressWarnings("boxing")
  public final class INVARIANT implements Invariantable {
    @Override public void check() {
      azzert.that(size, lessThanOrEqualTo(capacity()));
      azzert.that(capacity(), greaterThanOrEqualTo(MIN_CAPACITY));
      azzert.that(placeholder.length, lessThanOrEqualTo(capacity()));
      azzert.that(occupied.length, lessThanOrEqualTo(capacity()));
      azzert.that(data.length, lessThanOrEqualTo(capacity()));
      azzert.that(size, lessThanOrEqualTo((int) (MAX_LOAD * capacity())));
      azzert.that(size, greaterThanOrEqualTo((int) (MIN_LOAD * capacity())));
      azzert.that(removed, lessThanOrEqualTo((int) (REMOVE_LOAD * capacity())));
      azzert.that(removed, comparesEqualTo(count(placeholder)));
      azzert.that(size, comparesEqualTo(count(occupied) - removed));
      for (int ¢ = 0; ¢ < capacity(); ++¢)
        if (placeholder[¢])
          assert occupied[¢];
    }

    private int count(final boolean bs[]) {
      int $ = 0;
      for (final boolean ¢ : bs)
        $ += as.bit(¢);
      return $;
    }
  }
}

package il.org.spartan.collections;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import il.org.spartan.utils.*;
import il.org.spartan.utils.___.*;

/** An unsorted map of any entry to integers.
 * <p>
 * Implementation employs a linear hash table s, with quadratic probes (i.e.,
 * +1, +3, +6, +10, ... modulo the table size). Array capacity is always a power
 * of two, and is doubled when the load goes above {@link #MAX__LOAD}; capacity
 * is halved when the load drops below {@value #MIN__LOAD}. When the fraction of
 * removed keys goes below {@link #REMOVE__LOAD}, the table is rehashed.
 * @param <E> type of keys
 * @author Yossi Gil
 * @since December 2011 */
public final class ToIntegers<E> {
  public static final float MAX__LOAD = 0.75f;
  public static final float MIN__LOAD = 0.25f;
  public static final float REMOVE__LOAD = 0.20f;
  public static final int MIN__CAPACITY = 4;

  static <E> int hash(final E e) {
    int $ = e.hashCode();
    $ ^= $ >>> 12 ^ $ >>> 20;
    return $ ^ $ >>> 4 ^ $ >>> 7;
  }

  private static <E> E[] allocate(final int n) {
    @SuppressWarnings("unchecked") final E[] $ = (E[]) new Object[n];
    return $;
  }

  private static int roundUp(final int i) {
    int $ = 1;
    while ($ < i)
      $ <<= 1;
    return $;
  }

  private int[] values;
  private E[] data;
  private boolean[] occupied;
  private boolean[] placeholder;
  private int size;
  private int removed;

  /** Instantiate this class */
  public ToIntegers() {
    this(MIN__CAPACITY);
  }

  /** Instantiate this class, using a given size for the hash table.
   * @param initialCapacity suggests a hash table size, will be rounded up to
   *        the next power of two. */
  public ToIntegers(final int initialCapacity) {
    reset(Math.max(MIN__CAPACITY, roundUp(initialCapacity)));
  }

  /** What's the underlying table size?
   * @return the hash table size (always a power of two) */
  public int capacity() {
    return data.length;
  }

  /** Remove all elements from this set, preserving capacity.
   * @return <code><b>this</b>/code> */
  public ToIntegers<E> clear() {
    return reset(capacity());
  }

  /** Determine whether a given key is in this set.
   * @param key an arbitrary key
   * @return <code><b>true</b></code> if, and only if, the parameter is
   *         contained in this set. */
  public boolean contains(final E key) {
    return location(key) >= 0;
  }

  /** Check whether an array of integers is contained in this set.
   * @param ns an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, all elements in the array
   *         are contained in this set */
  public boolean contains(final E... ns) {
    for (final E n : ns)
      if (!contains(n))
        return false;
    return true;
  }

  /** Check whether this object is disjoint from an array of integers
   * @param ns an array of of integers; must not be <code><b>null</b></code>.
   * @return <code><b>true</b></code> if, and only if, this object is disjoint
   *         from the set of elements in the parameter */
  public boolean disjoint(final E... ns) {
    for (final E n : ns)
      if (contains(n))
        return false;
    return true;
  }

  public int[] get(final E keys[]) {
    final int[] $ = new int[keys.length];
    for (int i = 0; i < keys.length; i++)
      $[i] = get(keys[i]);
    return $;
  }

  public int get(final E key) {
    return values[location(key)];
  }

  public int increment(final E key) {
    final int location = location(key);
    if (location >= 0)
      return ++values[location];
    final int find = find(key);
    data[find] = key;
    values[find] = 1;
    occupied[find] = true;
    if (++size > capacity() * MAX__LOAD)
      rehash(data.length << 1);
    return 1;
  }

  /** @param key
   * @return <code>this</code> */
  public ToIntegers<E> init(final E key) {
    final int location = location(key);
    if (location >= 0)
      values[location] = 0;
    else {
      final int find = find(key);
      data[find] = key;
      values[find] = 0;
      occupied[find] = true;
    }
    if (++size > capacity() * MAX__LOAD)
      rehash(data.length << 1);
    return this;
  }

  /** What are all values stored in this object?
   * @return an array of all elements in this set. */
  public E[] keys() {
    final E[] $ = allocate(size);
    for (int i = 0, j = 0; i < capacity(); i++)
      if (occupied[i] && !placeholder[i])
        $[j++] = data[i];
    return $;
  }

  public ToIntegers<E> put(final E key, final int value) {
    final int location = location(key);
    if (location >= 0)
      values[location] = value;
    else {
      final int find = find(key);
      data[find] = key;
      values[find] = value;
      occupied[find] = true;
    }
    if (++size > capacity() * MAX__LOAD)
      rehash(data.length << 1);
    return this;
  }

  /** Recreate the table, inserting all elements in it afresh.
   * @return <code><b>this</b>/code> */
  public ToIntegers<E> rehash() {
    return rehash(capacity());
  }

  /** Remove an element from this set, it is in it
   * @param e some integer to be removed from the set
   * @return <code><b>this</b>/code> */
  public ToIntegers<E> remove(final E e) {
    final int i = location(e);
    assert i >= -1 && i < capacity();
    if (i < 0)
      return this;
    assert occupied[i] && data[i] == e;
    placeholder[i] = true;
    if (--size < capacity() * MIN__LOAD && capacity() > MIN__CAPACITY)
      return rehash(data.length >> 1);
    if (++removed > capacity() * REMOVE__LOAD)
      return rehash();
    return this;
  }

  /** Remove an array of integers to this set, if they are in it.
   * @param ns an array of integers; ; must not be <code><b>null</b></code>.
   * @return <code><b>this</b>/code> */
  public ToIntegers<E> remove(final int... ns) {
    for (final int n : ns)
      remove(n);
    return this;
  }

  /** How many elements are there in this set?
   * @return the number of values in the set. */
  public int size() {
    return size;
  }

  /** Find the index in the hash table into which the parameter could be
   * inserted.
   * @param n some value
   * @return -1 if the parameter is in the table already, otherwise, the index
   *         at which it could be safely inserted. */
  int find(final E e) {
    int $ = -1;
    for (int i = hash(e), t = 0;; i += ++t) {
      i &= data.length - 1;
      if (placeholder[i] || !occupied[i])
        $ = $ < 0 ? i : $;
      if (!occupied[i])
        return $;
      if (data[i] == e)
        return -1;
    }
  }

  /** Find the index in the hash table of the parameter
   * @param n some integer
   * @return index of the element if the parameter is in the table, otherwise,
   *         -1; */
  int location(final E e) {
    for (int i = hash(e), t = 0;; i += ++t) {
      i &= data.length - 1;
      if (!occupied[i])
        return -1;
      if (placeholder[i])
        continue;
      if (data[i] == e)
        return i;
    }
  }

  /** resize internal storage to the specified capacity, which must be a power
   * of two.
   * @param newCapacity new initialCapacity for the internal array
   * @return <code><b>this</b>/code> */
  private ToIntegers<E> rehash(final int newCapacity) {
    assert (newCapacity & newCapacity - 1) == 0;
    assert newCapacity >= MIN__CAPACITY;
    final E[] keys = keys();
    final int[] oldValues = get(keys);
    reset(newCapacity);
    for (int i = 0; i < keys.length; i++)
      put(keys[i], oldValues[i]);
    return this;
  }

  private final ToIntegers<E> reset(final int capacity) {
    data = allocate(capacity);
    occupied = new boolean[capacity];
    placeholder = new boolean[capacity];
    values = new int[capacity];
    size = removed = 0;
    return this;
  }

  @SuppressWarnings({ "synthetic-access", "boxing" }) //
  public final class INVARIANT implements Invariantable {
    @Override public void check() {
      assertThat(size, lessThanOrEqualTo(capacity()));
      assertThat(capacity(), greaterThanOrEqualTo(MIN__CAPACITY));
      assertThat(placeholder.length, lessThanOrEqualTo(capacity()));
      assertThat(occupied.length, lessThanOrEqualTo(capacity()));
      assertThat(data.length, lessThanOrEqualTo(capacity()));
      assertThat(size, lessThanOrEqualTo((int) (capacity() * MAX__LOAD)));
      assertThat(size, greaterThanOrEqualTo((int) (capacity() * MIN__LOAD)));
      assertThat(removed, lessThanOrEqualTo((int) (capacity() * REMOVE__LOAD)));
      assertThat(removed, comparesEqualTo(count(placeholder)));
      assertThat(size, comparesEqualTo(count(occupied) - removed));
      for (int i = 0; i < capacity(); i++)
        if (placeholder[i])
          assert occupied[i];
    }

    private int count(final boolean bs[]) {
      int $ = 0;
      for (final boolean b : bs)
        $ += As.binary(b);
      return $;
    }
  }
}

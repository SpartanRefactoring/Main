package il.org.spartan.reflection;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;

@SuppressWarnings({ "unchecked", "static-method" }) //
public final class MyHashMap<K, V> implements Map<K, V> {
  /** The default initial capacity - MUST be a power of two. */
  static final int DEFAULT_INITIAL_CAPACITY = 16;// ProbesTest.HASH_MAP_SIZE;
  /** The maximum capacity, used if a higher value is implicitly specified by
   * either of the constructors with arguments. MUST be a power of two <=
   * 1<<30. */
  static final int MAXIMUM_CAPACITY = 1 << 30;
  /** The load factor used when none specified in constructor. */
  static final float DEFAULT_LOAD_FACTOR = 0.75f;

  /** Applies a supplemental hash function to a given hashCode, which defends
   * against poor quality hash functions. This is critical because HashMap uses
   * power-of-two length hash tables, that otherwise encounter collisions for
   * hashCodes that do not differ in lower bits. Note: Null keys always map to
   * hash 0, thus index 0. */
  static int hash(final int h) {
    return h ^ h >>> 12 ^ h >>> 20 ^ (h ^ h >>> 12 ^ h >>> 20) >>> 4 ^ (h ^ h >>> 12 ^ h >>> 20) >>> 7;
  }

  /** Returns index for hash code h. */
  static int indexFor(final int h, final int length) {
    return h & length - 1;
  }

  /** The table, resized as necessary. Length MUST Always be a power of two. */
  @SuppressWarnings("rawtypes") transient Entry[] table;
  /** The number of key-value mappings contained in this map. */
  transient int size;
  /** The next size value at which to resize (capacity * load factor).
   * @serial */
  int threshold;
  /** The load factor for the hash table.
   * @serial */
  final float loadFactor;
  /** The number of times this HashMap has been structurally modified Structural
   * modifications are those that change the number of mappings in the HashMap
   * or otherwise modify its internal structure (e.g., rehash). This field is
   * used to make iterators on Collection-views of the HashMap fail-fast. (See
   * ConcurrentModificationException). */
  transient volatile int modCount;
  // Views
  private transient Set<Map.Entry<K, V>> entrySet = null;
  transient volatile Set<K> keySet;
  transient volatile Collection<V> values;

  /** Constructs an empty <tt>HashMap</tt> with the default initial capacity
   * (16) and the default load factor (0.75). */
  public MyHashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    table = new Entry[DEFAULT_INITIAL_CAPACITY];
  }

  /** Constructs an empty <tt>HashMap</tt> with the specified initial capacity
   * and the default load factor (0.75).
   * @param initialCapacity the initial capacity.
   * @throws IllegalArgumentException if the initial capacity is negative. */
  public MyHashMap(final int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  /** Constructs an empty <tt>HashMap</tt> with the specified initial capacity
   * and load factor.
   * @param initialCapacity the initial capacity
   * @param loadFactor the load factor
   * @throws IllegalArgumentException if the initial capacity is negative or the
   *         load factor is nonpositive */
  public MyHashMap(final int initialCapacity, final float loadFactor) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    // Find a power of 2 >= initialCapacity
    int capacity = 1;
    while (capacity < Math.min(initialCapacity, MAXIMUM_CAPACITY))
      capacity <<= 1;
    this.loadFactor = loadFactor;
    threshold = (int) (capacity * loadFactor);
    table = new Entry[capacity];
  }

  /** Constructs a new <tt>HashMap</tt> with the same mappings as the specified
   * <tt>Map</tt>. The <tt>HashMap</tt> is created with default load factor
   * (0.75) and an initial capacity sufficient to hold the mappings in the
   * specified <tt>Map</tt>.
   * @param m the map whose mappings are to be placed in this map
   * @throws NullPointerException if the specified map is null */
  public MyHashMap(final Map<? extends K, ? extends V> m) {
    this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
    putAllForCreate(m);
  }

  /** Removes all of the mappings from this map. The map will be empty after
   * this call returns. */
  @Override public void clear() {
    ++modCount;
    @SuppressWarnings("rawtypes") final Entry[] tab = table;
    for (int ¢ = 0; ¢ < tab.length; ++¢)
      tab[¢] = null;
    size = 0;
  }

  /** Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
   * values themselves are not cloned.
   * @return a shallow copy of this map */
  @Override public Object clone() {
    MyHashMap<K, V> $ = null;
    try {
      $ = (MyHashMap<K, V>) super.clone();
    } catch (final CloneNotSupportedException e) {
      assert false;
      return null;
    }
    $.table = new Entry[table.length];
    $.entrySet = null;
    $.modCount = 0;
    $.size = 0;
    $.putAllForCreate(this);
    return $;
  }

  /** Returns <tt>true</tt> if this map contains a mapping for the specified
   * key.
   * @param key The key whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the specified
   *         key. */
  @Override public boolean containsKey(final Object key) {
    return getEntry(key) != null;
  }

  /** Returns <tt>true</tt> if this map maps one or more keys to the specified
   * value.
   * @param value value whose presence in this map is to be tested
   * @return <tt>true</tt> if this map maps one or more keys to the specified
   *         value */
  @Override public boolean containsValue(final Object value) {
    if (value == null)
      return containsNullValue();
    @SuppressWarnings("rawtypes") final Entry[] tab = table;
    for (final Entry element : tab)
      for (@SuppressWarnings("rawtypes") Entry ¢ = element; ¢ != null; ¢ = ¢.next)
        if (value.equals(¢.value))
          return true;
    return false;
  }

  /** Returns a {@link Set} view of the mappings contained in this map. The set
   * is backed by the map, so changes to the map are reflected in the set, and
   * vice-versa. If the map is modified while an iteration over the set is in
   * progress (except through the iterator's own <tt>remove</tt> operation, or
   * through the <tt>setValue</tt> operation on a map entry returned by the
   * iterator) the results of the iteration are undefined. The set supports
   * element removal, which removes the corresponding mapping from the map, via
   * the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt> ,
   * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the
   * <tt>add</tt> or <tt>addAll</tt> operations.
   * @return a set view of the mappings contained in this map */
  @Override public Set<Map.Entry<K, V>> entrySet() {
    return entrySet0();
  }

  /** Returns the value to which the specified key is mapped, or {@code null} if
   * this map contains no mapping for the key.
   * <p>
   * More formally, if this map contains a mapping from a key {@code k} to a
   * value {@code v} such that {@code (key==null ? k==null : key.equals(k))},
   * then this method returns {@code v}; otherwise it returns {@code null}.
   * (There can be at most one such mapping.)
   * <p>
   * A return value of {@code null} does not <i>necessarily</i> indicate that
   * the map contains no mapping for the key; it's also possible that the map
   * explicitly maps the key to {@code null}. The {@link #containsKey
   * containsKey} operation may be used to distinguish these two cases.
   * @see #put(Object, Object) */
  @Override public V get(final Object key) {
    if (key == null)
      return getForNullKey();
    final int hash = hash(key.hashCode());
    for (Entry<K, V> $ = table[indexFor(hash, table.length)];; $ = $.next) {
      if ($ == null)
        break;
      Object k;
      if ($.hash == hash && ((k = $.key) == key || key.equals(k)))
        return $.value;
    }
    return null;
  }

  /** Returns <tt>true</tt> if this map contains no key-value mappings.
   * @return <tt>true</tt> if this map contains no key-value mappings */
  @Override public boolean isEmpty() {
    return size == 0;
  }

  /** Returns a {@link Set} view of the keys contained in this map. The set is
   * backed by the map, so changes to the map are reflected in the set, and
   * vice-versa. If the map is modified while an iteration over the set is in
   * progress (except through the iterator's own <tt>remove</tt> operation), the
   * results of the iteration are undefined. The set supports element removal,
   * which removes the corresponding mapping from the map, via the
   * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
   * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support the
   * <tt>add</tt> or <tt>addAll</tt> operations. */
  @Override public Set<K> keySet() {
    return keySet != null ? keySet : (keySet = new KeySet());
  }

  /** Associates the specified value with the specified key in this map. If the
   * map previously contained a mapping for the key, the old value is replaced.
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
   *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
   *         can also indicate that the map previously associated <tt>null</tt>
   *         with <tt>key</tt>.) */
  @Override public V put(final K key, final V value) {
    if (key == null)
      return putForNullKey(value);
    final int hash = hash(key.hashCode()), i = indexFor(hash, table.length);
    for (Entry<K, V> e = table[i]; e != null; e = e.next) {
      Object k;
      if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
        final V $ = e.value;
        e.value = value;
        e.recordAccess(this);
        return $;
      }
    }
    ++modCount;
    addEntry(hash, key, value, i);
    return null;
  }

  /** Copies all of the mappings from the specified map to this map. These
   * mappings will replace any mappings that this map had for any of the keys
   * currently in the specified map.
   * @param m mappings to be stored in this map
   * @throws NullPointerException if the specified map is null */
  @Override public void putAll(final Map<? extends K, ? extends V> m) {
    final int numKeysToBeAdded = m.size();
    if (numKeysToBeAdded == 0)
      return;
    /* Expand the map if the map if the number of mappings to be added is
     * greater than or equal to threshold. This is conservative; the obvious
     * condition is (m.size() + size) >= threshold, but this condition could
     * result in a map with twice the appropriate capacity, if the keys to be
     * added overlap with the keys already in this map. By using the
     * conservative calculation, we subject ourself to at most one extra
     * resize. */
    if (numKeysToBeAdded > threshold) {
      int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
      if (targetCapacity > MAXIMUM_CAPACITY)
        targetCapacity = MAXIMUM_CAPACITY;
      int newCapacity = table.length;
      while (newCapacity < targetCapacity)
        newCapacity <<= 1;
      if (newCapacity > table.length)
        resize(newCapacity);
    }
    for (final java.util.Map.Entry<? extends K, ? extends V> ¢ : m.entrySet())
      put(¢.getKey(), ¢.getValue());
  }

  /** Removes the mapping for the specified key from this map if present.
   * @param key key whose mapping is to be removed from the map
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
   *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
   *         can also indicate that the map previously associated <tt>null</tt>
   *         with <tt>key</tt>.) */
  @Override public V remove(final Object key) {
    final Entry<K, V> $ = removeEntryForKey(key);
    return $ == null ? null : $.value;
  }

  public int selfBytes() {
    return 0;
  }

  public int selfIntegers() {
    return 4;
  }

  public int selfObjects() {
    return 2;
  }

  public int selfReferences() {
    return 1;
  }

  /** Returns the number of key-value mappings in this map.
   * @return the number of key-value mappings in this map */
  @Override public int size() {
    return size;
  }

  public Entry<K, V>[] table() {
    return table;
  }

  @Override @SuppressWarnings("synthetic-access") public Collection<V> values() {
    return values != null ? values : (values = new Values());
  }

  /** Adds a new entry with the specified key, value and hash code to the
   * specified bucket. It is the responsibility of this method to resize the
   * table if appropriate. Subclass overrides this to alter the behavior of put
   * method. */
  void addEntry(final int hash, final K key, final V value, final int bucketIndex) {
    final Entry<K, V> e = table[bucketIndex];
    table[bucketIndex] = new Entry<>(hash, key, value, e);
    if (size++ >= threshold)
      resize(2 * table.length);
  }

  // These methods are used when serializing HashSets
  int capacity() {
    return table.length;
  }

  /** Like addEntry except that this version is used when creating entries as
   * part of Map construction or "pseudo-construction" (cloning,
   * deserialization). This version needn't worry about resizing the table.
   * Subclass overrides this to alter the behavior of HashMap(Map), clone, and
   * readObject. */
  void createEntry(final int hash, final K key, final V value, final int bucketIndex) {
    final Entry<K, V> e = table[bucketIndex];
    table[bucketIndex] = new Entry<>(hash, key, value, e);
    ++size;
  }

  /** Returns the entry associated with the specified key in the HashMap.
   * Returns null if the HashMap contains no mapping for the key. */
  Entry<K, V> getEntry(final Object key) {
    final int hash = key == null ? 0 : hash(key.hashCode());
    for (Entry<K, V> $ = table[indexFor(hash, table.length)]; $ != null; $ = $.next) {
      Object k;
      if ($.hash == hash && ((k = $.key) == key || key != null && key.equals(k)))
        return $;
    }
    return null;
  }

  float loadFactor() {
    return loadFactor;
  }

  Iterator<Map.Entry<K, V>> newEntryIterator() {
    return new EntryIterator();
  }

  // Subclass overrides these to alter behavior of views' iterator() method
  Iterator<K> newKeyIterator() {
    return new KeyIterator();
  }

  Iterator<V> newValueIterator() {
    return new ValueIterator();
  }

  /** Removes and returns the entry associated with the specified key in the
   * HashMap. Returns null if the HashMap contains no mapping for this key. */
  Entry<K, V> removeEntryForKey(final Object key) {
    final int hash = key == null ? 0 : hash(key.hashCode()), i = indexFor(hash, table.length);
    Entry<K, V> prev = table[i], $ = prev;
    while ($ != null) {
      final Entry<K, V> next = $.next;
      Object k;
      if ($.hash == hash && ((k = $.key) == key || key != null && key.equals(k))) {
        ++modCount;
        --size;
        if (prev == $)
          table[i] = next;
        else
          prev.next = next;
        $.recordRemoval(this);
        break;
      }
      prev = $;
      $ = next;
    }
    return $;
  }

  /** Special version of remove for EntrySet. */
  Entry<K, V> removeMapping(final Object o) {
    if (!(o instanceof Map.Entry))
      return null;
    final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
    final Object key = entry.getKey();
    final int hash = key == null ? 0 : hash(key.hashCode()), i = indexFor(hash, table.length);
    Entry<K, V> prev = table[i], $ = prev;
    while ($ != null) {
      final Entry<K, V> next = $.next;
      if ($.hash == hash && $.equals(entry)) {
        ++modCount;
        --size;
        if (prev == $)
          table[i] = next;
        else
          prev.next = next;
        $.recordRemoval(this);
        break;
      }
      prev = $;
      $ = next;
    }
    return $;
  }

  /** Rehashes the contents of this map into a new array with a larger capacity.
   * This method is called automatically when the number of keys in this map
   * reaches its threshold. If current capacity is MAXIMUM_CAPACITY, this method
   * does not resize the map, but sets threshold to Integer.MAX_VALUE. This has
   * the effect of preventing future calls.
   * @param newCapacity the new capacity, MUST be a power of two; must be
   *        greater than current capacity unless current capacity is
   *        MAXIMUM_CAPACITY (in which case value is irrelevant). */
  void resize(final int newCapacity) {
    @SuppressWarnings("rawtypes") final Entry[] oldTable = table;
    final int oldCapacity = oldTable.length;
    if (oldCapacity == MAXIMUM_CAPACITY) {
      threshold = Integer.MAX_VALUE;
      return;
    }
    @SuppressWarnings("rawtypes") final Entry[] newTable = new Entry[newCapacity];
    transfer(newTable);
    table = newTable;
    threshold = (int) (newCapacity * loadFactor);
  }

  /** Transfers all entries from current table to newTable. */
  void transfer(@SuppressWarnings("rawtypes") final Entry[] newTable) {
    @SuppressWarnings("rawtypes") final Entry[] src = table;
    final int newCapacity = newTable.length;
    for (int j = 0; j < src.length; ++j) {
      Entry<K, V> e = src[j];
      if (e != null) {
        src[j] = null;
        do {
          final Entry<K, V> next = e.next;
          final int i = indexFor(e.hash, newCapacity);
          e.next = newTable[i];
          newTable[i] = e;
          e = next;
        } while (e != null);
      }
    }
  }

  /** Special-case code for containsValue with null argument */
  private boolean containsNullValue() {
    @SuppressWarnings("rawtypes") final Entry[] tab = table;
    for (final Entry element : tab)
      for (@SuppressWarnings("rawtypes") Entry ¢ = element; ¢ != null; ¢ = ¢.next)
        if (¢.value == null)
          return true;
    return false;
  }

  private Set<Map.Entry<K, V>> entrySet0() {
    return entrySet != null ? entrySet : (entrySet = new EntrySet());
  }

  /** Offloaded version of get() to look up null keys. Null keys map to index 0.
   * This null case is split out into separate methods for the sake of
   * performance in the two most commonly used operations (get and put), but
   * incorporated with conditionals in others. */
  private V getForNullKey() {
    for (Entry<K, V> $ = table[0]; $ != null; $ = $.next)
      if ($.key == null)
        return $.value;
    return null;
  }

  private void putAllForCreate(final Map<? extends K, ? extends V> m) {
    for (final java.util.Map.Entry<? extends K, ? extends V> ¢ : m.entrySet())
      putForCreate(¢.getKey(), ¢.getValue());
  }

  /** This method is used instead of put by constructors and pseudoconstructors
   * (clone, readObject). It does not resize the table, check for
   * comodification, etc. It calls createEntry rather than addEntry. */
  private void putForCreate(final K key, final V value) {
    final int hash = key == null ? 0 : hash(key.hashCode()), i = indexFor(hash, table.length);
    /** Look for preexisting entry for key. This will never happen for clone or
     * deserialize. It will only happen for construction if the input Map is a
     * sorted map whose ordering is inconsistent w/ equals. */
    for (Entry<K, V> e = table[i]; e != null; e = e.next) {
      Object k;
      if (e.hash == hash && ((k = e.key) == key || key != null && key.equals(k))) {
        e.value = value;
        return;
      }
    }
    createEntry(hash, key, value, i);
  }

  /** Offloaded version of put for null keys */
  private V putForNullKey(final V value) {
    for (Entry<K, V> e = table[0]; e != null; e = e.next)
      if (e.key == null) {
        final V $ = e.value;
        e.value = value;
        e.recordAccess(this);
        return $;
      }
    ++modCount;
    addEntry(0, null, value, 0);
    return null;
  }

  /** Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
   * serialize it).
   * @serialData The <i>capacity</i> of the HashMap (the length of the bucket
   *             array) is emitted (int), followed by the <i>size</i> (an int,
   *             the number of key-value mappings), followed by the key (Object)
   *             and value (Object) for each key-value mapping. The key-value
   *             mappings are emitted in no particular order. */
  private void writeObject(final java.io.ObjectOutputStream s) throws IOException {
    final Iterator<Map.Entry<K, V>> i = size <= 0 ? null : entrySet0().iterator();
    // Write out the threshold, loadfactor, and any hidden stuff
    s.defaultWriteObject();
    // Write out number of buckets
    s.writeInt(table.length);
    // Write out size (number of Mappings)
    s.writeInt(size);
    // Write out keys and values (alternating)
    if (i != null)
      while (i.hasNext()) {
        final Map.Entry<K, V> e = i.next();
        s.writeObject(e.getKey());
        s.writeObject(e.getValue());
      }
  }

  static class Entry<K, V> implements Map.Entry<K, V> {
    final K key;
    V value;
    Entry<K, V> next;
    final int hash;

    /** Creates new entry. */
    Entry(final int h, final K k, final V v, final Entry<K, V> n) {
      value = v;
      next = n;
      key = k;
      hash = h;
    }

    @Override public final boolean equals(final Object o) {
      if (!(o instanceof Map.Entry))
        return false;
      @SuppressWarnings("rawtypes") final Map.Entry e = (Map.Entry) o;
      final Object k1 = getKey(), k2 = e.getKey();
      if (k1 != k2 && (k1 == null || !k1.equals(k2)))
        return false;
      final Object v1 = getValue(), v2 = e.getValue();
      return v1 == v2 || v1 != null && v1.equals(v2);
    }

    @Override public final K getKey() {
      return key;
    }

    @Override public final V getValue() {
      return value;
    }

    @Override public final int hashCode() {
      return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    @Override public final V setValue(final V newValue) {
      final V $ = value;
      value = newValue;
      return $;
    }

    @Override public final String toString() {
      return getKey() + "=" + getValue();
    }

    /** This method is invoked whenever the value in an entry is overwritten by
     * an invocation of put(k,v) for a key k that's already in the HashMap. */
    void recordAccess(final MyHashMap<K, V> ¢) {
      unused(¢);
    }

    /** This method is invoked whenever the entry is removed from the table. */
    void recordRemoval(final MyHashMap<K, V> ¢) {
      unused(¢);
    }
  }

  final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
    @Override public Map.Entry<K, V> next() {
      return nextEntry();
    }
  }

  final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
    @Override public void clear() {
      MyHashMap.this.clear();
    }

    @Override public boolean contains(final Object o) {
      if (!(o instanceof Map.Entry))
        return false;
      final Map.Entry<K, V> $ = (Map.Entry<K, V>) o;
      final Entry<K, V> candidate = getEntry($.getKey());
      return candidate != null && candidate.equals($);
    }

    @Override public Iterator<Map.Entry<K, V>> iterator() {
      return newEntryIterator();
    }

    @Override public boolean remove(final Object ¢) {
      return removeMapping(¢) != null;
    }

    @Override public int size() {
      return size;
    }
  }

  final class KeyIterator extends HashIterator<K> {
    @Override public K next() {
      return nextEntry().getKey();
    }
  }

  final class KeySet extends AbstractSet<K> {
    @Override public void clear() {
      MyHashMap.this.clear();
    }

    @Override public boolean contains(final Object ¢) {
      return containsKey(¢);
    }

    @Override public Iterator<K> iterator() {
      return newKeyIterator();
    }

    @Override public boolean remove(final Object ¢) {
      return MyHashMap.this.removeEntryForKey(¢) != null;
    }

    @Override public int size() {
      return size;
    }
  }

  final class ValueIterator extends HashIterator<V> {
    @Override public V next() {
      return nextEntry().value;
    }
  }

  private abstract class HashIterator<E> implements Iterator<E> {
    Entry<K, V> next; // next entry to return
    int expectedModCount; // For fast-fail
    int index; // current slot
    Entry<K, V> current; // current entry

    HashIterator() {
      expectedModCount = modCount;
      if (size > 0)
        for (@SuppressWarnings("rawtypes") final Entry[] ¢ = table; index < ¢.length && (next = ¢[index++]) == null;)
          nothing();
    }

    @Override public final boolean hasNext() {
      return next != null;
    }

    @Override public void remove() {
      if (current == null)
        throw new IllegalStateException();
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      final Object k = current.key;
      current = null;
      MyHashMap.this.removeEntryForKey(k);
      expectedModCount = modCount;
    }

    final Entry<K, V> nextEntry() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      final Entry<K, V> $ = next;
      if ($ == null)
        throw new NoSuchElementException();
      if ((next = $.next) == null)
        for (@SuppressWarnings("rawtypes") final Entry[] ¢ = table; index < ¢.length && (next = ¢[index++]) == null;)
          nothing();
      current = $;
      return $;
    }
  }

  private final class Values extends AbstractCollection<V> {
    @Override public void clear() {
      MyHashMap.this.clear();
    }

    @Override public boolean contains(final Object ¢) {
      return containsValue(¢);
    }

    @Override public Iterator<V> iterator() {
      return newValueIterator();
    }

    @Override public int size() {
      return size;
    }
  }
}

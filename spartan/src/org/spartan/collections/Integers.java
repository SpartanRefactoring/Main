/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan.collections;
/**
 * An unsorted set of integers supporting the basic set operations: {@link #add}
 * , {@link #contains} and {@link #remove}.
 * <p>
 * Implementation is with a linear hash table s, with quadratic probes (i.e.,
 * +1, +3, +6, +10, +15, ... modulo the table size). Array capacity is always a
 * power of two, and is doubled when the load goes above {@link #MAX_LOAD};
 * capacity is halved when the load drops below {@value #MIN_LOAD}. When the
 * fraction of removed keys goes below {@link #REMOVE_LOAD}, the table is
 * rehashed.
 * 
 * @author Yossi Gil
 * @since December 2010
 */
public class Integers {
	/**
	 * Instantiates this class
	 */
	public Integers() {
		this(MIN_CAPACITY);
	}
	/**
	 * Instantiate this class, using a given size for the hash table.
	 * 
	 * @param initialCapacity
	 *          suggests a hash table size, will be rounded up to the next power
	 *          of two.
	 */
	public Integers(final int initialCapacity) {
		int capacity = Math.max(MIN_CAPACITY, roundUp(initialCapacity));
		data = new int[capacity];
		occupied = new boolean[capacity];
		placeholder = new boolean[capacity];
		size = removed = 0;
		subclassReset(capacity);
	}
	/**
	 * How many elements are there in this set?
	 * 
	 * @return the number of values in the set.
	 */
	public int size() {
		return size;
	}
	/**
	 * Determine whether a given value is in this set.
	 * 
	 * @param n
	 *          an arbitrary integer
	 * @return <code><b>true</b></code> if, and only if, the parameter is
	 *         contained in this set.
	 */
	public boolean contains(final int n) {
		return 0 <= location(n);
	}
	/**
	 * Check whether an array of integers is contained in this set.
	 * 
	 * @param ns
	 *          an array of integers; ; must not be <code><b>null</b></code>.
	 * @return <code><b>true</b></code> if, and only if, all elements in the array
	 *         are contained in this set
	 */
	public boolean contains(final int... ns) {
		for (final int n : ns)
			if (!contains(n))
				return false;
		return true;
	}
	/**
	 * Check whether this object is disjoint from an array of integers
	 * 
	 * @param ns
	 *          an array of of integers; must not be <code><b>null</b></code>.
	 * @return <code><b>true</b></code> if, and only if, this object is disjoint
	 *         from the set of elements in the parameter
	 */
	public boolean disjoint(final int... ns) {
		for (final int n : ns)
			if (contains(n))
				return false;
		return true;
	}
	/**
	 * What are all values stored in this object?
	 * 
	 * @return an array of all elements in this set.
	 */
	public int[] entries() {
		final int[] $ = new int[size];
		for (int i = 0, j = 0; i < capacity(); i++)
			if (occupied[i] && !placeholder[i])
				$[j++] = data[i];
		return $;
	}
	/**
	 * Add an integer to the set, if it is not already there.
	 * 
	 * @param n
	 *          an arbitrary integer
	 * @return <code><b>this</b>/code>
	 */
	public Integers add(final int n) {
		final int i = find(n);
		assert i >= -1 && i < capacity();
		if (i < 0)
			return this;
		data[i] = n;
		occupied[i] = true;
		if (++size > capacity() * MAX_LOAD)
			rehash(data.length << 1);
		return this;
	}
	/**
	 * Add an array of integers to this set, if they are not already in it.
	 * 
	 * @param ns
	 *          an arbitrary array of integers; ; must not be
	 *          <code><b>null</b></code>.
	 * @return <code><b>this</b>/code>
	 */
	public Integers add(final int... ns) {
		for (final int n : ns)
			add(n);
		return this;
	}
	/**
	 * Remove an element from this set, it is in it
	 * 
	 * @param n
	 *          some integer to be removed from the set
	 * @return <code><b>this</b>/code>
	 */
	public Integers remove(final int n) {
		final int i = location(n);
		assert i >= -1 && i < capacity();
		if (i < 0)
			return this;
		assert occupied[i] && n == data[i];
		placeholder[i] = true;
		if (--size < capacity() * MIN_LOAD && capacity() > MIN_CAPACITY)
			return rehash(data.length >> 1);
		return ++removed <= capacity() * REMOVE_LOAD ? this : rehash();
	}
	/**
	 * Remove an array of integers to this set, if they are in it.
	 * 
	 * @param ns
	 *          an array of integers; ; must not be <code><b>null</b></code>.
	 * @return <code><b>this</b>/code>
	 */
	public Integers remove(final int... ns) {
		for (final int n : ns)
			remove(n);
		return this;
	}
	/**
	 * Recreate the table, inserting all elements in it afresh.
	 * 
	 * @return <code><b>this</b>/code>
	 */
	public Integers rehash() {
		return rehash(capacity());
	}
	/**
	 * Remove all elements from this set, preserving capacity.
	 * 
	 * @return <code><b>this</b>/code>
	 */
	public Integers clear() {
		return reset(capacity());
	}
	/**
	 * What's the underlying table size?
	 * 
	 * @return the hash table size (always a power of two)
	 */
	public int capacity() {
		return data.length;
	}
	public static final float MAX_LOAD = 0.75f;
	public static final float MIN_LOAD = 0.25f;
	public static final float REMOVE_LOAD = 0.20f;
	public static final int MIN_CAPACITY = 4;
	/**
	 * Find the index in the hash table of the parameter
	 * 
	 * @param n
	 *          some integer
	 * @return index of the element if the parameter is in the table, otherwise,
	 *         -1;
	 */
	private int location(final int n) {
		for (int i = hash(n), t = 0;; i += ++t) {
			i &= data.length - 1;
			if (!occupied[i])
				return -1;
			if (placeholder[i])
				continue;
			if (n == data[i])
				return i;
		}
	}
	/**
	 * Find the index in the hash table into which the parameter could be
	 * inserted.
	 * 
	 * @param n
	 *          some integer
	 * @return -1 if the parameter is in the table already, otherwise, the index
	 *         at which it could be safely inserted.
	 */
	protected int find(final int n) {
		int $ = -1;
		for (int i = hash(n), t = 0;; i += ++t) {
			i &= data.length - 1;
			if (placeholder[i] || !occupied[i])
				$ = $ < 0 ? i : $;
			if (!occupied[i])
				return $;
			if (n == data[i])
				return -1;
		}
	}
	/**
	 * resize internal storage to the specified capacity, which must be a power of
	 * two.
	 * 
	 * @param newCapacity
	 *          new initialCapacity for the internal array
	 * @return <code><b>this</b>/code>
	 */
	protected Integers rehash(final int newCapacity) {
		assert 0 == (newCapacity & newCapacity - 1);
		assert newCapacity >= MIN_CAPACITY;
		return reset(newCapacity).add(entries());
	}
	protected int[] data;
	private boolean[] occupied;
	private boolean[] placeholder;
	private int size;
	private int removed;
	final protected Integers reset(final int capacity) {
		data = new int[capacity];
		occupied = new boolean[capacity];
		placeholder = new boolean[capacity];
		size = removed = 0;
		subclassReset(capacity);
		return this;
	}
	/**
	 * @param capacity
	 *          new hash table size
	 */
	protected void subclassReset(final int capacity) {
		//
	}
	static int hash(final int n) {
		int $ = n;
		$ ^= $ >>> 20 ^ $ >>> 12;
		return $ ^ $ >>> 7 ^ $ >>> 4;
	}
	private static int roundUp(final int n) {
		int $ = 1;
		while ($ < n)
			$ <<= 1;
		return $;
	}
}

package il.org.spartan.collections;

import static fluent.ly.___.*;

import org.jetbrains.annotations.*;

/** A collection of functions and data structures pertaining to the
 * representation of a set of small non-negative integers as a bit mask. Each
 * such set is represented as a <code><b>short</b></code> integer.
 * Representation follows the convention that the <i>least significant</i> bit
 * is 1 if, and only if, the set contains the integer <code>0</code>.
 * @author Yossi Gil
 * @since 17/05/2007 */
public enum BitSet {
  ;
  /** The maximal number in any set we may be able to encode efficiently. */
  public static final int MAX = 7;
  /** 2 raised to the power of {@link #MAX} */
  public static final int EXP_MAX = 1 << MAX;
  /** A bit representation of singleton sets. For each position <code>p</code>
   * where <code>0 <= p <= {@link BitSet#MAX}</code>, the value
   * <code>{@link #$$}[p]</code> is the bit encoding of the positions' set whose
   * sole member is <code>p</code>.
   * <p>
   * Viewed differently, <code>{@link #$$}[p]</code> is the bit mask whose
   * <i>p</i><sup>th</sup> bit is set. */
  @NotNull public static final short $$[];
  static {
    $$ = new short[MAX + 1];
    for (int ¢ = 0; ¢ <= MAX; ++¢)
      $$[¢] = (short) (1 << ¢);
  }
  /** A data structure which makes it possible to iterate over the set of all
   * positions given by a bit representation of such a set. If <code>s</code> is
   * a <code><b>short</b></code> value whose bit representing encodes a set of
   * positions, then <code>positions[s]</code> is an array of the integers, in
   * an increasing order, that occur in this set. */
  public static final short positions[][] = new short[EXP_MAX][];
  static {
    for (int set = 0; set < EXP_MAX; ++set) {
      positions[set] = new short[cardinality(set)];
      int j = 0;
      for (short b = 0; b < MAX; ++b)
        if ((set & $$[b]) != 0)
          positions[set][j++] = b;
      sure(j == positions[set].length);
    }
  }

  /** compute a mask representing the set [0...n-1]
   * @param ¢ a non-negative integer, must be no greater than {@link BitSet#MAX}
   * @return a bit mask including where all bits 0...n-1 are set. */
  public static short all(final int ¢) {
    nonnegative(¢);
    require(¢ <= MAX);
    return (short) ($$[¢ + 1] - 1);
  }
  /** Return the number of elements in a set represented as a bit mask.
   * @param mask a bit mask to examine
   * @return the number of bits equal to 1 in the mask. */
  public static int cardinality(final int mask) {
    return mask == 0 ? 0 : (mask & 0x01) + cardinality(mask >>> 1);
  }
  /** Turn off a specific bit in a mask.
   * @param mask a bit mask representation of a set
   * @param i index of the bit to turn off
   * @return the mask with the n<i>th</i> bit turned off. */
  public static short clear(final short mask, final int i) {
    return (short) (mask & ~$$[i]);
  }
  /** Determine set membership
   * @param mask a bit mask representing a set
   * @param i an integer whose membership in the set is to be checked. Must be
   *        in the range <code>0...{@link BitSet#MAX}</code>
   * @return <code><b>true</b></code> <i>iff</i> <code>n</code> is a member of
   *         the set <code>mask</code> */
  public static boolean contains(final short mask, final int i) {
    return (mask & $$[i]) != 0;
  }
  /** Compute the intersection of two set sets of positions.
   * @param s1 a bit mask representation of a set of positions.
   * @param s2 another bit mask representation of a set of position.
   * @return the intersection of s1 an s2. */
  public static short intersect(final short s1, final short s2) {
    return (short) (s1 & s2);
  }
  @SuppressWarnings("boxing") public static void main(final String argv[]) {
    for (int ¢ = 0; ¢ < MAX; ++¢)
      System.out.printf("exp[%d] = %d\n", ¢, $$[¢]);
    for (int ¢ = 0; ¢ < MAX; ++¢)
      System.out.printf("bits(%d) = %d\n", ¢, cardinality(¢));
    System.out.printf("set: %o ", union(set(3), set(6)));
    for (int i = 0; i < EXP_MAX; ++i) {
      System.out.print(i + ". [");
      for (final int j : positions[i])
        System.out.print(j + " ");
      System.out.print("] \n");
    }
  }
  /** Obtain a list of all integers in a given mask
   * @param mask a bit mask representing a set
   * @return an array containing all integers in the set represented by
   *         <code>mask</code>; these integers are found in ascending order */
  public static short[] positions(final short mask) {
    require(mask < EXP_MAX);
    return positions[mask];
  }
  /** Turn on a specific bit in a mask.
   * @param mask A bit mask representation of a set
   * @param i index of the bit to turn on
   * @return the mask with the n<i>th</i> bit turned off. */
  public static short raise(final short mask, final int i) {
    return (short) (mask | $$[i]);
  }
  /** Convert an integer to a singleton set containing it.
   * @param ¢ an integer in the range <code>0...{@link BitSet#MAX}</code>
   * @return a bit mask representing the set whose sole member is
   *         <code>n</code> */
  public static short set(final int ¢) {
    nonnegative(¢);
    require(¢ < MAX);
    return $$[¢];
  }
  /** convert an array of <code><b>int</b></code>s into a bit mask
   * @param set the set of numbers to be converted; each member must be in the
   *        range <code>0...{@link #MAX}</code>
   * @return the bit mask representation of <code>set</code> */
  public static short toMask(@NotNull final int[] set) {
    short $ = 0;
    for (final int position : set)
      $ = raise($, position);
    return $;
  }
  /** convert a bunch of <code><b>{@link Integer}</b></code> into a bit mask
   * @param set the set of numbers to be converted; each member must non-
   *        <code><b>null</b></code> and in the range <code>0...
   *            {@link #MAX}</code>
   * @return the bit mask representation of <code>set</code> */
  public static short toMask(@NotNull final Iterable<Integer> set) {
    short $ = 0;
    for (final int position : set)
      $ = raise($, position);
    return $;
  }
  /** convert an array of <code><b>short</b></code> integers into a bit mask
   * @param set the set of numbers to be converted; each member must be in the
   *        range <code>0...{@link #MAX}</code>
   * @return the bit mask representation of <code>set</code> */
  public static short toMask(@NotNull final short[] set) {
    short $ = 0;
    for (final short position : set)
      $ = raise($, position);
    return $;
  }
  /** Compute the union of two set sets of positions.
   * @param s1 a bit mask representation of a set of position.
   * @param s2 another bit mask representation of a set of position.
   * @return the union of s1 an s2. */
  public static short union(final short s1, final short s2) {
    return (short) (s1 | s2);
  }
}

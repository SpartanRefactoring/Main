package il.org.spartan.utils;

import java.util.*;

import il.org.spartan.*;

/** An immutable integral range, representing all integers between
 * {@link #from}, up to, but not including, {@link #to}, i.e.,
 *
 * <pre>
 * {@link #from}, {@link #from}+1, ..., {@link #to}-1
 * </pre>
 *
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2012 */
public class Range {
  /** the beginning of the range (inclusive) */
  public final int from;
  /** the end of the range (exclusive) */
  public final int to;

  /** Instantiates from beginning and end locations
   * @param from JD
   * @param to JD */
  public Range(final int from, final int to) {
    this.from = from;
    this.to = to;
  }

  /** Instantiates using values found in another intance
   * @param other other */
  public Range(final Range other) {
    this(other.from, other.to);
  }

  @Override public boolean equals(final Object o) {
    return o instanceof Range && from == ((Range) o).from && to == ((Range) o).to;
  }

  /** Find an including range
   * @param rs some arbitrary {@link Range} objects
   * @return first {@link Range} object in the parameters that contains this
   *         instance, or <code><b>null</b></code> if not such value can be
   *         found. */
  public Range findIncludedIn(final Iterable<? extends Range> rs) {
    for (final Range $ : rs)
      if (includedIn($))
        return $;
    return null;
  }

  @Override public int hashCode() {
    // Cantor pairing function
    return (int) (from + 0.5 * (to + from) * (to + from + 1));
  }

  /** @param r arbitrary
   * @return <code><b>true</b></code> <i>iff</i> <code><b>this</b></code> is
   *         included in the parameter. */
  public boolean includedIn(final Range r) {
    return from >= r.from && to <= r.to;
  }

  /** Merge with another record
   * @param r JD
   * @return A newly created range representing the merge. */
  public Range merge(final Range r) {
    return new Range(Math.min(from, r.from), Math.max(to, r.to));
  }

  /** Determine whether overlaps in any part another range
   * @param r arbitrary
   * @return <code><b>true</b></code> <i>iff</i> <code><b>this</b></code>
   *         overlaps the parameter. */
  public boolean overlapping(final Range r) {
    return from >= r.from || to <= r.to;
  }

  /** Prune all ranges in a given list that include this object.
   * @param rs JD */
  public void pruneIncluders(final List<? extends Range> rs) {
    for (;;) {
      final Range includesMe = findIncludedIn(rs);
      if (includesMe == null)
        return;
      rs.remove(includesMe);
    }
  }

  /** The number of integers in the range
   * @return a non-negative integer, computed as {@link #to} -{@link #from} */
  public int size() {
    return to - from;
  }

  @Override public String toString() {
    return String.format("[%d, %d]", box.it(from), box.it(to));
  }

  public boolean isEmpty() {
    return size() <= 0;
  }
}
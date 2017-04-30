package il.org.spartan.utils;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public Range(@NotNull final Range other) {
    this(other.from, other.to);
  }

  @Override public boolean equals(final Object ¢) {
    return ¢ instanceof Range && from == ((Range) ¢).from && to == ((Range) ¢).to;
  }

  /** Find an including range
   * @param ¢ some arbitrary {@link Range} objects
   * @return first {@link Range} object in the parameters that contains this
   *         instance, or <code><b>null</b></code> if not such value can be
   *         found. */
  @Nullable public Range findIncludedIn(@NotNull final Iterable<? extends Range> ¢) {
    for (@NotNull final Range $ : ¢)
      if (includedIn($))
        return $;
    return null;
  }

  @Override public int hashCode() {
    // Cantor pairing function
    return (int) (from + 0.5 * (to + from) * (to + from + 1));
  }

  /** @param ¢ arbitrary
   * @return <code><b>true</b></code> <i>iff</i> <code><b>this</b></code> is
   *         included in the parameter. */
  public boolean includedIn(@NotNull final Range ¢) {
    return from >= ¢.from && to <= ¢.to;
  }

  public boolean isEmpty() {
    return size() <= 0;
  }

  /** Merge with another record
   * @param ¢ JD
   * @return A newly created range representing the merge. */
  @NotNull public Range merge(@NotNull final Range ¢) {
    return new Range(Math.min(from, ¢.from), Math.max(to, ¢.to));
  }

  /** Determine whether overlaps in any part another range
   * @param ¢ arbitrary
   * @return <code><b>true</b></code> <i>iff</i> <code><b>this</b></code>
   *         overlaps the parameter. */
  public boolean overlapping(@NotNull final Range ¢) {
    return from >= ¢.from || to <= ¢.to;
  }

  /** Prune all ranges in a given list that include this object.
   * @param rs JD */
  public void pruneIncluders(@NotNull final List<? extends Range> rs) {
    for (;;) {
      @Nullable final Range r = findIncludedIn(rs);
      if (r == null)
        return;
      rs.remove(r);
    }
  }

  /** The number of integers in the range
   * @return a non-negative integer, computed as {@link #to} -{@link #from} */
  public int size() {
    return to - from;
  }

  @Override public String toString() {
    return String.format("[%d, %d]", fluent.ly.box.it(from), fluent.ly.box.it(to));
  }
}
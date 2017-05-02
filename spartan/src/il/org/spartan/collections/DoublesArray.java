package il.org.spartan.collections;

import java.util.*;

import org.jetbrains.annotations.*;

/** A <href a=http://en.wikipedia.org/wiki/Dynamic_array>growable array</a> of
 * <code><b>double</b></code>s.
 * @author Yossi Gil
 * @since February 20, 2012 */
public class DoublesArray {
  private int length;
  @NotNull private double[] inner = new double[0];

  /** Makes sure the specified array index exists, extending {@link #length()}
   * if necessary
   * @param i a non-negative array index
   * @return <code><b>this</b></code> */
  @NotNull public DoublesArray access(final int i) {
    if (i < length)
      return this;
    final int n = inner.length;
    inner = i < n ? inner : Arrays.copyOf(inner, 1 + Math.max(i, n + (n >> 1)));
    length = i + 1;
    return this;
  }

  /** @param i a non-negative array index
   * @param value
   * @return the new contents of the specified array cell */
  public double addTo(final int i, final double value) {
    return access(i).inner[i] += value;
  }

  /** @param ¢ a non-negative array index
   * @return the new contents of the specified array cell */
  public double dec(final int ¢) {
    return --access(¢).inner[¢];
  }

  /** Retrieve the content of a specified array cell. If the contents of the
   * specified cell was not previously set, the returned value is zero, and the
   * array grow.
   * @param ¢ a non-negative array index
   * @return the contents at the specified location */
  public double get(final int ¢) {
    return access(¢).inner[¢];
  }

  /** @param ¢ a non-negative array index
   * @return the new contents of the specified array cell */
  public double inc(final int ¢) {
    return ++access(¢).inner[¢];
  }

  /** What is this array's length?
   * @return current array length */
  public int length() {
    return length;
  }

  /** Removes the last cell
   * @return the removed value */
  public double pop() {
    return inner[--length];
  }

  /** Adds a value at the end, extending this array
   * @param value an arbitrary value to be added */
  public void push(final double value) {
    set(length, value);
  }

  /** Set the contents of a specified array location
   * @param i a non-negative array index
   * @param value the new contents of the specified array cell
   * @return the new contents of the specified array cell */
  public double set(final int i, final double value) {
    return access(i).inner[i] = value;
  }

  /** A representation as a Java array.
   * @return the underlying array; likely to be invalid if the array grows */
  @NotNull public double[] toArray() {
    return Arrays.copyOf(inner, length);
  }
}

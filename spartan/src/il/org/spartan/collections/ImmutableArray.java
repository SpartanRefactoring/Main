// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.collections;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** An immutable array of any reference type.
 * @param <T> Type of elements stored in the array.
 * @author Yossi Gil, the Technion.
 * @since 23/08/2008 */
@Canopy
@Immutable
@Instantiable
public class ImmutableArray<T> implements Iterable<T> {
  /** The encapsulated array. */
  private final T[] implementation;

  /** @param ts an array that this class will protect from modifications. */
  public ImmutableArray(final T[] ts) {
    implementation = ts;
  }
  /** Retrieve a value from the encapsulated array.
   * @param ¢ from which array index should the value be retrieved?
   * @return the value at the location specified by the parameter. */
  public T get(final int ¢) {
    return implementation[¢];
  }
  @Override @NotNull public Iterator<T> iterator() {
    return new Iterator<T>() {
      int i;

      @Override public boolean hasNext() {
        return i < length();
      }
      @Override public T next() {
        if (i >= length())
          throw new NoSuchElementException();
        return get(i++);
      }
    };
  }
  /** Determine how many elements are there in the encapsulated array.
   * @return the number of elements in this array. */
  public int length() {
    return implementation.length;
  }

  /** An immutable array of <code>boolean</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Booleans {
    /** The encapsulated array. */
    private final boolean[] implementation;

    /** @param bs an array that this class will protect from modifications. */
    public Booleans(final boolean[] bs) {
      implementation = bs;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public boolean get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>byte</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Bytes {
    /** The encapsulated array. */
    private final byte[] implementation;

    /** @param bs an array that this class will protect from modifications. */
    public Bytes(final byte[] bs) {
      implementation = bs;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public byte get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>char</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Chars {
    /** The encapsulated array. */
    private final char[] implementation;

    /** @param cs an array that this class will protect from modifications. */
    public Chars(final char[] cs) {
      implementation = cs;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public char get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>double</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Doubles {
    /** The encapsulated array. */
    private final double[] implementation;

    /** @param ds an array that this class will protect from modifications. */
    public Doubles(final double[] ds) {
      implementation = ds;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public double get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>float</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Floats {
    /** The encapsulated array. */
    private final float[] implementation;

    /** @param fs an array that this class will protect from modifications. */
    public Floats(final float[] fs) {
      implementation = fs;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public float get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>int</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Ints {
    /** The encapsulated array. */
    private final int[] implementation;

    /** @param is an array that this class will protect from modifications. */
    public Ints(final int[] is) {
      implementation = is;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public int get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>long</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Longs {
    /** The encapsulated array. */
    private final long[] implementation;

    /** @param ls an array that this class will protect from modifications. */
    public Longs(final long[] ls) {
      implementation = ls;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public long get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }

  /** An immutable array of <code>short</code>s.
   * @author Yossi Gil, the Technion.
   * @since 23/08/2008 */
  @Canopy
  @Immutable
  public static class Shorts {
    /** The encapsulated array. */
    private final short[] implementation;

    /** @param ss an array that this class will protect from modifications. */
    public Shorts(final short[] ss) {
      implementation = ss;
    }
    /** Retrieve a value from the encapsulated array.
     * @param ¢ from which array index should the value be retrieved?
     * @return the value at the location specified by the parameter. */
    public short get(final int ¢) {
      return implementation[¢];
    }
    /** Determine how many elements are there in the encapsulated array.
     * @return the number of elements in this array. */
    public int length() {
      return implementation.length;
    }
  }
}

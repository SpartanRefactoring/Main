package il.org.spartan.utils;

import java.util.*;

import il.org.spartan.streotypes.*;

/** A utility class, offering a collection of function to unbox arrays and
 * collection of the boxed versions of the primitive types. The input of each
 * unboxing function is a {@link Collection} or an array of one the following
 * eight reference types
 * <ol>
 * <li>{@link Boolean}
 * <li>{@link Byte}
 * <li>{@link Character}
 * <li>{@link Double}
 * <li>{@link Float}
 * <li>{@link Integer}
 * <li>{@link Long}
 * <li>{@link Short}
 * </ol>
 * The returned value is an array of the equivalent primitive type.
 * <p>
 * Note that unboxing of a single value of a reference type is easy using a
 * function such as {@link Long#longValue()}
 * @author Yossi Gil.
 * @see Box */
@Utility public enum Unbox {
  // A namespace: no values to this <code><b>enum</b></code>
  ;
  public static double[] it(final Double[] ds) {
    return unbox(ds);
  }

  public static int it(final Integer i) {
    return i.intValue();
  }

  public static int[] it(final Integer[] is) {
    return unbox(is);
  }

  public static int[] it(final List<Integer> is) {
    return it(is.toArray(new Integer[is.size()]));
  }

  public static boolean unbox(final Boolean b) {
    return b.booleanValue();
  }

  /** Unbox an array of {@link Boolean}s into an array of
   * <code><b>boolean</b></code>s.
   * @param bs an array of {@link Boolean}s
   * @return an equivalent array of <code><b>boolean</b></code>s. */
  public static boolean[] unbox(final Boolean[] bs) {
    final boolean[] $ = new boolean[bs.length];
    for (int i = 0; i < bs.length; i++)
      $[i] = bs[i].booleanValue();
    return $;
  }

  public static byte unbox(final Byte b) {
    return b.byteValue();
  }

  /** Unbox an array of {@link Byte}s into an array of <code><b>byte</b></code>
   * s.
   * @param bs an array of {@link Byte}s
   * @return an equivalent array of <code><b>byte</b></code>s. */
  public static byte[] unbox(final Byte[] bs) {
    final byte[] $ = new byte[bs.length];
    for (int i = 0; i < bs.length; i++)
      $[i] = bs[i].byteValue();
    return $;
  }

  public static char unbox(final Character c) {
    return c.charValue();
  }

  /** Unbox an array of {@link Character}s into an array of
   * <code><b>char</b></code>s.
   * @param cs an array of {@link Character}s
   * @return an equivalent array of <code><b>char</b></code>s. */
  public static char[] unbox(final Character[] cs) {
    final char[] $ = new char[cs.length];
    for (int i = 0; i < cs.length; i++)
      $[i] = cs[i].charValue();
    return $;
  }

  /** Unbox a {@link Collection} of {@link Short}s into an array of
   * <code><b>short</b></code>s.
   * @param ss a {@link Collection} of {@link Integer}s
   * @return an equivalent array of <code><b>short</b></code>s. */
  public static short[] unbox(final Collection<Short> ss) {
    final short[] $ = new short[ss.size()];
    int i = 0;
    for (final Short v : ss)
      $[i++] = v.shortValue();
    return $;
  }

  public static double unbox(final Double d) {
    return d.doubleValue();
  }

  /** Unbox an array of {@link Double}s into an array of
   * <code><b>double</b></code>s.
   * @param ds an array of {@link Double}s
   * @return an equivalent array of <code><b>double</b></code>s. */
  public static double[] unbox(final Double[] ds) {
    final double[] $ = new double[ds.length];
    for (int i = 0; i < ds.length; i++)
      $[i] = ds[i].floatValue();
    return $;
  }

  public static float unbox(final Float f) {
    return f.floatValue();
  }

  /** Unbox an array of {@link Float}s into an array of
   * <code><b>float</b></code> s.
   * @param fs an array of {@link Float}s
   * @return an equivalent array of <code><b>float</b></code>s. */
  public static float[] unbox(final Float[] fs) {
    final float[] $ = new float[fs.length];
    for (int i = 0; i < fs.length; i++)
      $[i] = fs[i].floatValue();
    return $;
  }

  public static int unbox(final Integer i) {
    return i.intValue();
  }

  /** Unbox an array of {@link Integer}s into an array of
   * <code><b>int</b></code> s.
   * @param is an array of {@link Integer}s
   * @return an equivalent array of <code><b>int</b></code>s. */
  public static int[] unbox(final Integer[] is) {
    final int[] $ = new int[is.length];
    for (int i = 0; i < is.length; i++)
      $[i] = is[i].intValue();
    return $;
  }

  public static long unbox(final Long l) {
    return l.longValue();
  }

  /** Unbox an array of {@link Long}s into an array of <code><b>long</b></code>
   * s.
   * @param ls an array of {@link Long}s
   * @return an equivalent array of <code><b>long</b></code>s. */
  public static long[] unbox(final Long[] ls) {
    final long[] $ = new long[ls.length];
    for (int i = 0; i < ls.length; i++)
      $[i] = ls[i].longValue();
    return $;
  }

  public static short unbox(final Short s) {
    return s.shortValue();
  }

  /** Unbox an array of {@link Short}s into an array of
   * <code><b>short</b></code> s.
   * @param ss an array of {@link Integer}s
   * @return an equivalent array of <code><b>short</b></code>s. */
  public static short[] unbox(final Short[] ss) {
    final short[] $ = new short[ss.length];
    for (int i = 0; i < ss.length; i++)
      $[i] = ss[i].shortValue();
    return $;
  }
}

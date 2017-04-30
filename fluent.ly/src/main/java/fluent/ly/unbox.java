package fluent.ly;

import java.util.*;

import org.jetbrains.annotations.*;

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
 * @see box */
public enum unbox {
  // A namespace: no values to this <code><b>enum</b></code>
  ;
  @NotNull public static double[] it(@NotNull final Double[] ¢) {
    return unbox(¢);
  }

  public static int it(@NotNull final Integer ¢) {
    return ¢.intValue();
  }

  @NotNull public static int[] it(@NotNull final Integer[] ¢) {
    return unbox(¢);
  }

  @NotNull public static int[] it(@NotNull final List<Integer> ¢) {
    return it(¢.toArray(new Integer[¢.size()]));
  }

  public static boolean unbox(@NotNull final Boolean ¢) {
    return ¢.booleanValue();
  }

  /** unbox an array of {@link Boolean}s into an array of
   * <code><b>boolean</b></code>s.
   * @param bs an array of {@link Boolean}s
   * @return an equivalent array of <code><b>boolean</b></code>s. */
  @NotNull public static boolean[] unbox(@NotNull final Boolean[] bs) {
    @NotNull final boolean[] $ = new boolean[bs.length];
    for (int ¢ = 0; ¢ < bs.length; ++¢)
      $[¢] = bs[¢].booleanValue();
    return $;
  }

  public static byte unbox(@NotNull final Byte ¢) {
    return ¢.byteValue();
  }

  /** unbox an array of {@link Byte}s into an array of <code><b>byte</b></code>
   * s.
   * @param bs an array of {@link Byte}s
   * @return an equivalent array of <code><b>byte</b></code>s. */
  @NotNull public static byte[] unbox(@NotNull final Byte[] bs) {
    @NotNull final byte[] $ = new byte[bs.length];
    for (int ¢ = 0; ¢ < bs.length; ++¢)
      $[¢] = bs[¢].byteValue();
    return $;
  }

  public static char unbox(@NotNull final Character ¢) {
    return ¢.charValue();
  }

  /** unbox an array of {@link Character}s into an array of
   * <code><b>char</b></code>s.
   * @param cs an array of {@link Character}s
   * @return an equivalent array of <code><b>char</b></code>s. */
  @NotNull public static char[] unbox(@NotNull final Character[] cs) {
    @NotNull final char[] $ = new char[cs.length];
    for (int ¢ = 0; ¢ < cs.length; ++¢)
      $[¢] = cs[¢].charValue();
    return $;
  }

  /** unbox a {@link Collection} of {@link Short}s into an array of
   * <code><b>short</b></code>s.
   * @param ss a {@link Collection} of {@link Integer}s
   * @return an equivalent array of <code><b>short</b></code>s. */
  @NotNull public static short[] unbox(@NotNull final Collection<Short> ss) {
    @NotNull final short[] $ = new short[ss.size()];
    int i = 0;
    for (@NotNull final Short v : ss)
      $[i++] = v.shortValue();
    return $;
  }

  public static double unbox(@NotNull final Double ¢) {
    return ¢.doubleValue();
  }

  /** unbox an array of {@link Double}s into an array of
   * <code><b>double</b></code>s.
   * @param ds an array of {@link Double}s
   * @return an equivalent array of <code><b>double</b></code>s. */
  @NotNull public static double[] unbox(@NotNull final Double[] ds) {
    @NotNull final double[] $ = new double[ds.length];
    for (int ¢ = 0; ¢ < ds.length; ++¢)
      $[¢] = ds[¢].floatValue();
    return $;
  }

  public static float unbox(@NotNull final Float ¢) {
    return ¢.floatValue();
  }

  /** unbox an array of {@link Float}s into an array of
   * <code><b>float</b></code> s.
   * @param fs an array of {@link Float}s
   * @return an equivalent array of <code><b>float</b></code>s. */
  @NotNull public static float[] unbox(@NotNull final Float[] fs) {
    @NotNull final float[] $ = new float[fs.length];
    for (int ¢ = 0; ¢ < fs.length; ++¢)
      $[¢] = fs[¢].floatValue();
    return $;
  }

  public static int unbox(@NotNull final Integer ¢) {
    return ¢.intValue();
  }

  /** unbox an array of {@link Integer}s into an array of
   * <code><b>int</b></code> s.
   * @param is an array of {@link Integer}s
   * @return an equivalent array of <code><b>int</b></code>s. */
  @NotNull public static int[] unbox(@NotNull final Integer[] is) {
    @NotNull final int[] $ = new int[is.length];
    for (int ¢ = 0; ¢ < is.length; ++¢)
      $[¢] = is[¢].intValue();
    return $;
  }

  public static long unbox(@NotNull final Long ¢) {
    return ¢.longValue();
  }

  /** unbox an array of {@link Long}s into an array of <code><b>long</b></code>
   * s.
   * @param ls an array of {@link Long}s
   * @return an equivalent array of <code><b>long</b></code>s. */
  @NotNull public static long[] unbox(@NotNull final Long[] ls) {
    @NotNull final long[] $ = new long[ls.length];
    for (int ¢ = 0; ¢ < ls.length; ++¢)
      $[¢] = ls[¢].longValue();
    return $;
  }

  public static short unbox(@NotNull final Short ¢) {
    return ¢.shortValue();
  }

  /** unbox an array of {@link Short}s into an array of
   * <code><b>short</b></code> s.
   * @param ss an array of {@link Integer}s
   * @return an equivalent array of <code><b>short</b></code>s. */
  @NotNull public static short[] unbox(@NotNull final Short[] ss) {
    @NotNull final short[] $ = new short[ss.length];
    for (int ¢ = 0; ¢ < ss.length; ++¢)
      $[¢] = ss[¢].shortValue();
    return $;
  }
}

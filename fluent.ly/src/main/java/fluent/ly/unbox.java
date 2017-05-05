package fluent.ly;

import java.util.*;

import org.eclipse.jdt.annotation.*;

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
   public static double[] it( final Double[] ¢) {
    return unbox(¢);
  }
   public static float[] it(final Float[] fs) {
    return unbox(fs);
  }
  public static int it( final Integer ¢) {
    return ¢.intValue();
  }
   public static int[] it( final Integer[] ¢) {
    return unbox(¢);
  }
   public static int[] it( final List<Integer> ¢) {
    return it(¢.toArray(new Integer[¢.size()]));
  }
  public static boolean unbox( final Boolean ¢) {
    return ¢.booleanValue();
  }
  /** unbox an array of {@link Boolean}s into an array of
   * <code><b>boolean</b></code>s.
   * @param bs an array of {@link Boolean}s
   * @return an equivalent array of <code><b>boolean</b></code>s. */
   public static boolean[] unbox( final Boolean[] bs) {
     final boolean[] $ = new boolean[bs.length];
    for (int ¢ = 0; ¢ < bs.length; ++¢)
      $[¢] = bs[¢].booleanValue();
    return $;
  }
  public static byte unbox( final Byte ¢) {
    return ¢.byteValue();
  }
  /** unbox an array of {@link Byte}s into an array of <code><b>byte</b></code>
   * s.
   * @param bs an array of {@link Byte}s
   * @return an equivalent array of <code><b>byte</b></code>s. */
   public static byte[] unbox( final Byte[] bs) {
     final byte[] $ = new byte[bs.length];
    for (int ¢ = 0; ¢ < bs.length; ++¢)
      $[¢] = bs[¢].byteValue();
    return $;
  }
  public static char unbox( final Character ¢) {
    return ¢.charValue();
  }
  /** unbox an array of {@link Character}s into an array of
   * <code><b>char</b></code>s.
   * @param cs an array of {@link Character}s
   * @return an equivalent array of <code><b>char</b></code>s. */
   public static char[] unbox( final Character[] cs) {
     final char[] $ = new char[cs.length];
    for (int ¢ = 0; ¢ < cs.length; ++¢)
      $[¢] = cs[¢].charValue();
    return $;
  }
  /** unbox a {@link Collection} of {@link Short}s into an array of
   * <code><b>short</b></code>s.
   * @param ss a {@link Collection} of {@link Integer}s
   * @return an equivalent array of <code><b>short</b></code>s. */
   public static short[] unbox( final Collection<Short> ss) {
     final short[] $ = new short[ss.size()];
    int i = 0;
    for ( final Short v : ss)
      $[i++] = v.shortValue();
    return $;
  }
  public static double unbox( final Double ¢) {
    return ¢.doubleValue();
  }
  /** unbox an array of {@link Double}s into an array of
   * <code><b>double</b></code>s.
   * @param ds an array of {@link Double}s
   * @return an equivalent array of <code><b>double</b></code>s. */
   public static double[] unbox( final Double[] ds) {
     final double[] $ = new double[ds.length];
    for (int ¢ = 0; ¢ < ds.length; ++¢)
      $[¢] = ds[¢].floatValue();
    return $;
  }
  public static float unbox( final Float ¢) {
    return ¢.floatValue();
  }
  /** unbox an array of {@link Float}s into an array of
   * <code><b>float</b></code> s.
   * @param fs an array of {@link Float}s
   * @return an equivalent array of <code><b>float</b></code>s. */
   public static float[] unbox( final Float[] fs) {
     final float[] $ = new float[fs.length];
    for (int ¢ = 0; ¢ < fs.length; ++¢)
      $[¢] = fs[¢].floatValue();
    return $;
  }
  public static int unbox( final Integer ¢) {
    return ¢.intValue();
  }
  /** unbox an array of {@link Integer}s into an array of
   * <code><b>int</b></code> s.
   * @param is an array of {@link Integer}s
   * @return an equivalent array of <code><b>int</b></code>s. */
   public static int[] unbox( final Integer[] is) {
     final int[] $ = new int[is.length];
    for (int ¢ = 0; ¢ < is.length; ++¢)
      $[¢] = is[¢].intValue();
    return $;
  }
  public static long unbox( final Long ¢) {
    return ¢.longValue();
  }
  /** unbox an array of {@link Long}s into an array of <code><b>long</b></code>
   * s.
   * @param ls an array of {@link Long}s
   * @return an equivalent array of <code><b>long</b></code>s. */
   public static long[] unbox( final Long[] ls) {
     final long[] $ = new long[ls.length];
    for (int ¢ = 0; ¢ < ls.length; ++¢)
      $[¢] = ls[¢].longValue();
    return $;
  }
  public static short unbox( final Short ¢) {
    return ¢.shortValue();
  }
  /** unbox an array of {@link Short}s into an array of
   * <code><b>short</b></code> s.
   * @param ss an array of {@link Integer}s
   * @return an equivalent array of <code><b>short</b></code>s. */
   public static short[] unbox( final Short[] ss) {
     final short[] $ = new short[ss.length];
    for (int ¢ = 0; ¢ < ss.length; ++¢)
      $[¢] = ss[¢].shortValue();
    return $;
  }
}

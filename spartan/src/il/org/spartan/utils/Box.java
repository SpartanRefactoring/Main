// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import il.org.spartan.streotypes.*;

/** A utility class, with a collection of function to box primitive types in
 * their reference type equivalent representation. Similarly, this class offers
 * functions to box arrays of primitive types into their reference type
 * equivalent.
 * @author Yossi Gil, the Technion.
 * @since 21/06/2008
 * @see Unbox */
@Utility public enum Box {
  // A namespace: no values to this <code><b>enum</b></code>
  ;
  /** Box a <code><b>boolean</b></code> into a {@link Boolean} object.
   * @param b some <code><b>boolean</b></code> value
   * @return a non-<code><b>null</b></code> {@link Boolean} with the value of
   *         <code>c</code> */
  public static Boolean box(final boolean b) {
    return Boolean.valueOf(b);
  }

  /** Box an array of <code><b>boolean</b></code>s into an array of
   * {@link Boolean}s.
   * @param bs an array of <code><b>boolean</b></code>s
   * @return an array of {@link Boolean} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Boolean[] box(final boolean bs[]) {
    final Boolean[] $ = new Boolean[bs.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(bs[i]);
    return $;
  }

  /** Box a <code><b>byte</b></code> into a {@link Byte} object.
   * @param b some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code> */
  public static Byte box(final byte b) {
    return Byte.valueOf(b);
  }

  /** Box an array of <code><b>byte</b></code>s into an array of {@link Byte}s.
   * @param bs an array of <code><b>byte</b></code>s
   * @return an array of {@link Byte} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Byte[] box(final byte bs[]) {
    final Byte[] $ = new Byte[bs.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(bs[i]);
    return $;
  }

  /** Box a <code><b>char</b></code> into a {@link Character} object.
   * @param c some <code><b>char</b></code> value
   * @return a non-<code><b>null</b></code> {@link Character} with the value of
   *         <code>c</code> */
  public static Character box(final char c) {
    return Character.valueOf(c);
  }

  /** Box an array of <code><b>byte</b></code>s into an array of
   * {@link Character}s.
   * @param cs an array of <code><b>long</b></code>s
   * @return an array of {@link Character} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Character[] box(final char cs[]) {
    final Character[] $ = new Character[cs.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(cs[i]);
    return $;
  }

  /** Box a <code><b>double</b></code> into a {@link Double} object.
   * @param d some <code><b>double</b></code> value
   * @return a non-<code><b>null</b></code> {@link Double} with the value of
   *         <code>d</code> */
  public static Double box(final double d) {
    return Double.valueOf(d);
  }

  /** Box an array of <code><b>double</b></code>s into an array of
   * {@link Double} s.
   * @param ds an array of <code><b>double</b></code>s
   * @return an array of {@link Double} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Double[] box(final double ds[]) {
    final Double[] $ = new Double[ds.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(ds[i]);
    return $;
  }

  /** Box a <code><b>float</b></code> into a {@link Float} object.
   * @param f some <code><b>float</b></code> value
   * @return a non-<code><b>null</b></code> {@link Float} with the value of
   *         <code>f</code> */
  public static Float box(final float f) {
    return Float.valueOf(f);
  }

  /** Box an array of <code><b>float</b></code>s into an array of {@link Float}
   * s.
   * @param fs an array of <code><b>float</b></code>s
   * @return an array of {@link Float} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Float[] box(final float fs[]) {
    final Float[] $ = new Float[fs.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(fs[i]);
    return $;
  }

  /** Box an <code><b>int</b></code> into an {@link Integer} object.
   * @param n some <code><b>int</b></code> value
   * @return a non-<code><b>null</b></code> {@link Integer} with the value of
   *         <code>n</code> */
  public static Integer box(final int n) {
    return Integer.valueOf(n);
  }

  /** Box an array of <code><b>int</b></code>s into an array of {@link Integer}
   * s.
   * @param is an array of <code><b>int</b></code>s
   * @return an array of {@link Integer} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Integer[] box(final int is[]) {
    final Integer[] $ = new Integer[is.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(is[i]);
    return $;
  }

  /** Box a <code><b>long</b></code> into a {@link Long} object.
   * @param l some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code> */
  public static Long box(final long l) {
    return Long.valueOf(l);
  }

  /** Box an array of <code><b>long</b></code>s into an array of {@link Long}s.
   * @param ls an array of <code><b>long</b></code>s
   * @return an array of {@link Long} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Long[] box(final long ls[]) {
    final Long[] $ = new Long[ls.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(ls[i]);
    return $;
  }

  /** Box a <code><b>short</b></code> into a {@link Short} object.
   * @param s some <code><b>short</b></code> value
   * @return a non-<code><b>null</b></code> {@link Short} with the value of
   *         <code>s</code> */
  public static Short box(final short s) {
    return Short.valueOf(s);
  }

  /** Box an array of <code><b>short</b></code>s into an array of {@link Short}
   * s.
   * @param ss an array of <code><b>short</b></code>s
   * @return an array of {@link Short} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Short[] box(final short ss[]) {
    final Short[] $ = new Short[ss.length];
    for (int i = 0; i < $.length; i++)
      $[i] = box(ss[i]);
    return $;
  }

  public static Double it(final double d) {
    return box(d);
  }

  /** Box an <code><b>int</b></code> into an {@link Integer} object.
   * @param n some <code><b>int</b></code> value
   * @return a non-<code><b>null</b></code> {@link Integer} with the value of
   *         <code>n</code>
   * @see #box(int) */
  public static Integer it(final int n) {
    return box(n);
  }

  /** Box an array of <code><b>int</b></code>s into an array of {@link Integer}
   * s.
   * @param is an array of <code><b>int</b></code>s
   * @return an array of {@link Integer} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Integer[] it(final int is[]) {
    return box(is);
  }

  /** Box a <code><b>long</b></code> into a {@link Long} object.
   * @param l some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code> */
  public static Long it(final long l) {
    return box(l);
  }
}

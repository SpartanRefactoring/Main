// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package fluent.ly;

import static il.org.spartan.Utils.cantBeNull;

/**
 * A utility class, with a collection of function to box primitive types in
 * their reference type equivalent representation. Similarly, this class offers
 * functions to box arrays of primitive types into their reference type
 * equivalent.
 *
 * @author Yossi Gil, the Technion.
 * @since 21/06/2008
 * @see unbox
 */
public enum box {
  // A namespace: no values to this <code><b>enum</b></code>
  ;
  /**
   * box a <code><b>boolean</b></code> into a {@link Boolean} object.
   *
   * @param ¢ some <code><b>boolean</b></code> value
   * @return a non-<code><b>null</b></code> {@link Boolean} with the value of
   *         <code>c</code>
   */
  public static Boolean it(final boolean ¢) {
    return cantBeNull(Boolean.valueOf(¢));
  }

  /**
   * box an array of <code><b>boolean</b></code>s into an array of
   * {@link Boolean}s.
   *
   * @param bs an array of <code><b>boolean</b></code>s
   * @return an array of {@link Boolean} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position is
   *         the boxed value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Boolean[] it(final boolean bs[]) {
    final var $ = new Boolean[bs.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(bs[¢]);
    return $;
  }

  /**
   * box a <code><b>byte</b></code> into a {@link Byte} object.
   *
   * @param ¢ some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code>
   */
  public static Byte it(final byte ¢) {
    return cantBeNull(Byte.valueOf(¢));
  }

  /**
   * box an array of <code><b>byte</b></code>s into an array of {@link Byte}s.
   *
   * @param bs an array of <code><b>byte</b></code>s
   * @return an array of {@link Byte} of the same length as that of the parameter,
   *         and such that it in its <tt>i</tt><em>th</em> position is the boxed
   *         value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Byte[] it(final byte bs[]) {
    final var $ = new Byte[bs.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(bs[¢]);
    return $;
  }

  /**
   * box a <code><b>char</b></code> into a {@link Character} object.
   *
   * @param ¢ some <code><b>char</b></code> value
   * @return a non-<code><b>null</b></code> {@link Character} with the value of
   *         <code>c</code>
   */
  public static Character it(final char ¢) {
    return cantBeNull(Character.valueOf(¢));
  }

  /**
   * box an array of <code><b>byte</b></code>s into an array of
   * {@link Character}s.
   *
   * @param cs an array of <code><b>long</b></code>s
   * @return an array of {@link Character} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position is
   *         the boxed value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Character[] it(final char cs[]) {
    final var $ = new Character[cs.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(cs[¢]);
    return $;
  }

  /**
   * box a <code><b>double</b></code> into a {@link Double} object.
   *
   * @param ¢ some <code><b>double</b></code> value
   * @return a non-<code><b>null</b></code> {@link Double} with the value of
   *         <code>d</code>
   */
  public static Double it(final double ¢) {
    return cantBeNull(Double.valueOf(¢));
  }

  /**
   * box an array of <code><b>double</b></code>s into an array of {@link Double}
   * s.
   *
   * @param ds an array of <code><b>double</b></code>s
   * @return an array of {@link Double} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position is
   *         the boxed value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Double[] it(final double ds[]) {
    final var $ = new Double[ds.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(ds[¢]);
    return $;
  }

  /**
   * box a <code><b>float</b></code> into a {@link Float} object.
   *
   * @param ¢ some <code><b>float</b></code> value
   * @return a non-<code><b>null</b></code> {@link Float} with the value of
   *         <code>f</code>
   */
  public static Float it(final float ¢) {
    return cantBeNull(Float.valueOf(¢));
  }

  /**
   * box an array of <code><b>float</b></code>s into an array of {@link Float} s.
   *
   * @param fs an array of <code><b>float</b></code>s
   * @return an array of {@link Float} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position is
   *         the boxed value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Float[] it(final float fs[]) {
    final var $ = new Float[fs.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(fs[¢]);
    return $;
  }

  /**
   * box an <code><b>int</b></code> into an {@link Integer} object.
   *
   * @param ¢ some <code><b>int</b></code> value
   * @return a non-<code><b>null</b></code> {@link Integer} with the value of
   *         <code>n</code>
   * @see idiomatic#box(int)
   */
  public static Integer it(final int ¢) {
    return idiomatic.box(¢);
  }

  /**
   * box an array of <code><b>int</b></code>s into an array of {@link Integer} s.
   *
   * @param ¢ an array of <code><b>int</b></code>s
   * @return an array of {@link Integer} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position is
   *         the boxed value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Integer[] it(final int ¢[]) {
    return idiomatic.box(¢);
  }

  /**
   * box a <code><b>long</b></code> into a {@link Long} object.
   *
   * @param ¢ some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code>
   */
  public static Long it(final long ¢) {
    return cantBeNull(Long.valueOf(¢));
  }

  /**
   * box an array of <code><b>long</b></code>s into an array of {@link Long}s.
   *
   * @param ls an array of <code><b>long</b></code>s
   * @return an array of {@link Long} of the same length as that of the parameter,
   *         and such that it in its <tt>i</tt><em>th</em> position is the boxed
   *         value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Long[] it(final long ls[]) {
    final var $ = new Long[ls.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(ls[¢]);
    return $;
  }

  /**
   * box a <code><b>short</b></code> into a {@link Short} object.
   *
   * @param ¢ some <code><b>short</b></code> value
   * @return a non-<code><b>null</b></code> {@link Short} with the value of
   *         <code>s</code>
   */
  public static Short it(final short ¢) {
    return cantBeNull(Short.valueOf(¢));
  }

  /**
   * box an array of <code><b>short</b></code>s into an array of {@link Short} s.
   *
   * @param ss an array of <code><b>short</b></code>s
   * @return an array of {@link Short} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position is
   *         the boxed value of the <tt>i</tt><em>th</em> of the parameter
   */
  public static Short[] it(final short ss[]) {
    final var $ = new Short[ss.length];
    for (var ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = it(ss[¢]);
    return $;
  }
}

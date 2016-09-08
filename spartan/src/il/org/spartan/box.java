/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.annotation.*;

/** A utility class, with a collection of function to box primitive types in
 * their reference type equivalent representation. Similarly, this class offers
 * functions to box arrays of primitive types into their reference type
 * equivalent.
 * @author Yossi Gil
 * @since 2008/06/21 */
public enum box {
  // A namespace: no values to this <code><b>enum</b></code>
  ;
  /** Box a <code><b>boolean</b></code> into a {@link Boolean} object.
   * @param b some <code><b>boolean</b></code> value
   * @return a non-<code><b>null</b></code> {@link Boolean} with the value of
   *         <code>c</code> */
  public static Boolean it(final boolean b) {
    return cantBeNull(Boolean.valueOf(b));
  }

  /** Box an array of <code><b>boolean</b></code>s into an array of
   * {@link Boolean}s.
   * @param b an array of <code><b>boolean</b></code>s
   * @return an array of {@link Boolean} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Boolean[] it(final boolean b[]) {
    final Boolean @NonNull [] $ = new Boolean @NonNull [b.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(b[i]);
    return $;
  }

  /** Box a <code><b>byte</b></code> into a {@link Byte} object.
   * @param b some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code> */
  public static Byte it(final byte b) {
    return cantBeNull(Byte.valueOf(b));
  }

  /** Box an array of <code><b>byte</b></code>s into an array of {@link Byte}s.
   * @param b an array of <code><b>byte</b></code>s
   * @return an array of {@link Byte} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Byte[] it(final byte b[]) {
    final Byte @NonNull [] $ = new Byte @NonNull [b.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(b[i]);
    return $;
  }

  /** Box a <code><b>char</b></code> into a {@link Character} object.
   * @param c some <code><b>char</b></code> value
   * @return a non-<code><b>null</b></code> {@link Character} with the value of
   *         <code>c</code> */
  public static Character it(final char c) {
    return cantBeNull(Character.valueOf(c));
  }

  /** Box an array of <code><b>byte</b></code>s into an array of
   * {@link Character}s.
   * @param c an array of <code><b>long</b></code>s
   * @return an array of {@link Character} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Character[] it(final char c[]) {
    final Character @NonNull [] $ = new Character @NonNull [c.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(c[i]);
    return $;
  }

  /** Box a <code><b>double</b></code> into a {@link Double} object.
   * @param d some <code><b>double</b></code> value
   * @return a non-<code><b>null</b></code> {@link Double} with the value of
   *         <code>d</code> */
  public static Double it(final double d) {
    return cantBeNull(Double.valueOf(d));
  }

  /** Box an array of <code><b>double</b></code>s into an array of
   * {@link Double} s.
   * @param d an array of <code><b>double</b></code>s
   * @return an array of {@link Double} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Double[] it(final double d[]) {
    final Double @NonNull [] $ = new Double @NonNull [d.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(d[i]);
    return $;
  }

  /** Box a <code><b>float</b></code> into a {@link Float} object.
   * @param f some <code><b>float</b></code> value
   * @return a non-<code><b>null</b></code> {@link Float} with the value of
   *         <code>f</code> */
  public static Float it(final float f) {
    return cantBeNull(Float.valueOf(f));
  }

  /** Box an array of <code><b>float</b></code>s into an array of {@link Float}
   * s.
   * @param f an array of <code><b>float</b></code>s
   * @return an array of {@link Float} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Float[] it(final float f[]) {
    final Float @NonNull [] $ = new Float @NonNull [f.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(f[i]);
    return $;
  }

  /** Box an <code><b>int</b></code> into an {@link Integer} object.
   * @param i some <code><b>int</b></code> value
   * @return a non-<code><b>null</b></code> {@link Integer} with the value of
   *         <code>n</code> */
  public static Integer it(final int i) {
    return cantBeNull(Integer.valueOf(i));
  }

  /** Box an array of <code><b>int</b></code>s into an array of {@link Integer}
   * s.
   * @param is an array of <code><b>int</b></code>s
   * @return an array of {@link Integer} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Integer[] it(final int is[]) {
    final Integer @NonNull [] $ = new Integer @NonNull [is.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(is[i]);
    return $;
  }

  /** Box a <code><b>long</b></code> into a {@link Long} object.
   * @param l some <code><b>long</b></code> value
   * @return a non-<code><b>null</b></code> {@link Long} with the value of
   *         <code>l</code> */
  public static Long it(final long l) {
    return cantBeNull(Long.valueOf(l));
  }

  /** Box an array of <code><b>long</b></code>s into an array of {@link Long}s.
   * @param l an array of <code><b>long</b></code>s
   * @return an array of {@link Long} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Long[] it(final long l[]) {
    final Long @NonNull [] $ = new Long @NonNull [l.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(l[i]);
    return $;
  }

  /** Box a <code><b>short</b></code> into a {@link Short} object.
   * @param s some <code><b>short</b></code> value
   * @return a non-<code><b>null</b></code> {@link Short} with the value of
   *         <code>s</code> */
  public static Short it(final short s) {
    return cantBeNull(Short.valueOf(s));
  }

  /** Box an array of <code><b>short</b></code>s into an array of {@link Short}
   * s.
   * @param s an array of <code><b>short</b></code>s
   * @return an array of {@link Short} of the same length as that of the
   *         parameter, and such that it in its <tt>i</tt><em>th</em> position
   *         is the boxed value of the <tt>i</tt><em>th</em> of the parameter */
  public static Short[] it(final short s[]) {
    final Short @NonNull [] $ = new Short @NonNull [s.length];
    for (int i = 0; i < $.length; ++i)
      $[i] = it(s[i]);
    return $;
  }
}

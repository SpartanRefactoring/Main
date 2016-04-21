package il.org.spartan.hamcrest;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.utils.Utils.compressSpaces;

import org.hamcrest.Matcher;

import il.org.spartan.misc.Wrapper;

/**
 * Non-auto-boxing version for all primitive types of the family of Hamcrest
 * functions {@link org.hamcrest.MatcherAssert#assertThat}
 *
 * @author Yossi Gil
 * @since 2015-07-18
 */
public class MatcherAssert extends org.hamcrest.MatcherAssert {
  /**
   * A non-auto-boxing version for the primitive type
   * <code><b>boolean</b></code> of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param b
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final boolean b, final Matcher<? super Boolean> m) {
    assertThat(Boolean.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>byte</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param b
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final byte b, final Matcher<? super Byte> m) {
    assertThat(Byte.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>char</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param c
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final char c, final Matcher<? super Character> m) {
    assertThat(Character.valueOf(c), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>double</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param d
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final double d, final Matcher<? super Double> m) {
    assertThat(Double.valueOf(d), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>float</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param f
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final float f, final Matcher<? super Float> m) {
    assertThat(Float.valueOf(f), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>int</b></code> of
   * the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param i
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final int i, final Matcher<? super Integer> m) {
    assertThat(Integer.valueOf(i), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>long</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param l
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final long l, final Matcher<? super Long> m) {
    assertThat(Long.valueOf(l), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>short</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}; the boxing
   * in the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param s
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(Object, Matcher)
   */
  public static void assertThat(final short s, final Matcher<? super Short> m) {
    assertThat(Short.valueOf(s), m);
  }
  /**
   * A non-auto-boxing version for the primitive type
   * <code><b>boolean</b></code> of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param b
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final boolean b, final Matcher<? super Boolean> m) {
    assertThat(reason, Boolean.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>byte</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param b
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final byte b, final Matcher<? super Byte> m) {
    assertThat(reason, Byte.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>char</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param c
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final char c, final Matcher<? super Character> m) {
    assertThat(reason, Character.valueOf(c), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>double</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param d
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final double d, final Matcher<? super Double> m) {
    assertThat(reason, Double.valueOf(d), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>float</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param f
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final float f, final Matcher<? super Float> m) {
    assertThat(reason, Float.valueOf(f), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>int</b></code> of
   * function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param i
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final int i, final Matcher<? super Integer> m) {
    assertThat(reason, Integer.valueOf(i), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>long</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param l
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final long l, final Matcher<? super Long> m) {
    assertThat(reason, Long.valueOf(l), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>short</b></code>
   * of the original Hamcrest function
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason
   *          as in the original Hamcrest function
   *          {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}
   * @param s
   *          JD
   * @param m
   *          JD
   * @see org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void assertThat(final String reason, final short s, final Matcher<? super Short> m) {
    assertThat(reason, Short.valueOf(s), m);
  }
  /**
   * A variant of {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}
   * which compares {link @String} representation of an object, with an expected
   * such representation, while ignoring white space characters, unless these
   * occur between identifiers.
   *
   * @param actual
   *          the actual object
   * @param expected
   *          the expected textual representation of the first parameter
   */
  public static void assertThat(final Object actual, final Wrapper<String> expected) {
    assertThat(compressSpaces(actual + ""), is(compressSpaces(expected.get())));
  }
  /**
   * Wraps the provided {@link String}
   *
   * @param s
   *          a {@link String} to wrap
   * @return a wrapped {@link String}
   */
  public static final Wrapper<String> iz(final String s) {
    return new Wrapper<>(s);
  }
}

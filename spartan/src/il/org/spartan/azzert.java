package il.org.spartan;

import static il.org.spartan.Utils.*;
import il.org.spartan.misc.*;

import org.eclipse.jdt.annotation.*;
import org.hamcrest.*;

/**
 * @author Yossi Gil
 * @since 2015-07-18
 */
public class azzert extends org.junit.Assert {
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> Matcher<T> allOf(final java.lang.Iterable<Matcher<? super T>> matchers) {
    return org.hamcrest.core.AllOf.<T> allOf(matchers);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   *
   * @param matchers JD
   * @return
   */
  @SafeVarargs public static <T> Matcher<T> allOf(final Matcher<? super T>... matchers) {
    return org.hamcrest.core.AllOf.<T> allOf(matchers);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second) {
    return org.hamcrest.core.AllOf.<T> allOf(first, second);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
    return org.hamcrest.core.AllOf.<T> allOf(first, second, third);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth) {
    return org.hamcrest.core.AllOf.<T> allOf(first, second, third, fourth);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   *
   * @param first JD
   * @param second JD
   * @param third JD
   * @param fourth JD
   * @param fifth JD
   * @return
   */
  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return org.hamcrest.core.AllOf.<T> allOf(first, second, third, fourth, fifth);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ALL</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, allOf(startsWith(&quot;my&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth,
      final Matcher<? super T> sixth) {
    return org.hamcrest.core.AllOf.<T> allOf(first, second, third, fourth, fifth, sixth);
  }
  /**
   * Creates a matcher that matches when the examined object is an instance of
   * the specified <code>type</code>, as determined by calling the
   * {@link java.lang.Class#isInstance(Object)} method on that type, passing the
   * the examined object.
   *
   * <p>
   * The created matcher forces a relationship between specified type and the
   * examined object, and should be used when it is necessary to make generics
   * conform, for example in the JMock clause
   * <code>with(any(Thing.class))</code>
   * </p>
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(new Canoe(), instanceOf(Canoe.class));
   * </pre>
   */
  public static <T> Matcher<T> any(final java.lang.Class<T> type) {
    return org.hamcrest.core.IsInstanceOf.<T> any(type);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   *
   * @param matchers
   * @param <T> JD
   * @return
   */
  public static <T> org.hamcrest.core.@Nullable AnyOf<T> anyOf(final java.lang.Iterable<Matcher<? super T>> matchers) {
    return org.hamcrest.core.AnyOf.<T> anyOf(matchers);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  @SafeVarargs public static <T> org.hamcrest.core.@Nullable AnyOf<T> anyOf(final Matcher<? super T>... matchers) {
    return org.hamcrest.core.AnyOf.<T> anyOf(matchers);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> org.hamcrest.core.@Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second) {
    return org.hamcrest.core.AnyOf.<T> anyOf(first, second);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> org.hamcrest.core.@Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third) {
    return org.hamcrest.core.AnyOf.<T> anyOf(first, second, third);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> org.hamcrest.core.@Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth) {
    return org.hamcrest.core.AnyOf.<T> anyOf(first, second, third, fourth);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   *
   * @param <T> JD
   * @param first JD
   * @param second JD
   * @param third JD
   * @param fourth JD
   * @param fifth JD
   * @return a matcher
   */
  public static <T> org.hamcrest.core.AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return org.hamcrest.core.AnyOf.<T> anyOf(first, second, third, fourth, fifth);
  }
  /**
   * Creates a matcher that matches if the examined object matches <b>ANY</b> of
   * the specified matchers.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myValue&quot;, anyOf(startsWith(&quot;foo&quot;), containsString(&quot;Val&quot;)))
   * </pre>
   */
  public static <T> org.hamcrest.core.@Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth,
      final Matcher<? super T> sixth) {
    return org.hamcrest.core.AnyOf.<T> anyOf(first, second, third, fourth, fifth, sixth);
  }
  /**
   * Creates a matcher that always matches, regardless of the examined object.
   */
  public static @Nullable Matcher<java.lang.Object> anything() {
    return org.hamcrest.core.IsAnything.anything();
  }
  /**
   * Creates a matcher that always matches, regardless of the examined object,
   * but describes itself with the specified {@link String}.
   *
   * @param description a meaningful {@link String} used when describing itself
   * @return
   */
  public static org.hamcrest.@Nullable Matcher<@Nullable Object> anything(final java.lang.String description) {
    return org.hamcrest.core.IsAnything.anything(description);
  }
  public static void assertEquals(final int expected, final int actual) {
    assertEquals(box.it(expected), box.it(actual));
  }
  public static void assertEquals(final Object exp, final @Nullable Object val) {
    that(val, is(exp));
  }
  public static void assertEquals(final String reason, final int i1, final int i2) {
    assertThat(reason, box.it(i1), CoreMatchers.equalTo(box.it(i2)));
  }
  public static void assertFalse(final String s, final boolean b) {
    that(s, b, is(Boolean.FALSE));
  }
  public static void assertNotEquals(final Object o1, final @Nullable Object o2) {
    that(o1, CoreMatchers.not(o2));
  }
  public static void assertNotEquals(final String reason, final Object o1, final Object o2) {
    assertThat(reason, o1, CoreMatchers.not(o2));
  }
  public static void assertNotNull(final Object o) {
    that(o, CoreMatchers.notNullValue());
  }
  public static void assertNotNull(final String s, final @Nullable Object o) {
    assertThat(s, o, notNullValue());
  }
  /**
   * @param os JD
   */
  public static void assertNotNulls(final @Nullable Object @Nullable... os) {
    assert os != null;
    assertNotNull(os);
    for (final @Nullable Object o : os)
      assertNotNull("" + os, o);
  }
  public static void assertNull(final @Nullable Object o) {
    that(o, nullValue());
  }
  /**
   * assert that a given integer is positive
   *
   * @param i
   */
  public static void assertPositive(final int i) {
    assertTrue(i > 0);
  }
  /**
   * A non-auto-boxing version for the primitive type
   * <code><b>boolean</b></code> of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param b JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final boolean b, final Matcher<? super @Nullable Boolean> m) {
    that(Boolean.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>byte</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param b JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final byte b, final @Nullable Matcher<? super @Nullable Byte> m) {
    that(Byte.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>char</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param c JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final char c, final Matcher<? super @Nullable Character> m) {
    that(Character.valueOf(c), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>double</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param d JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final double d, final Matcher<? super @Nullable Double> m) {
    that(Double.valueOf(d), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>float</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param f JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final float f, final Matcher<? super @Nullable Float> m) {
    that(Float.valueOf(f), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>int</b></code> of
   * the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param i JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final int i, final Matcher<? super @Nullable Integer> m) {
    that(Integer.valueOf(i), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>long</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param l JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final long l, final Matcher<? super @Nullable Long> m) {
    that(Long.valueOf(l), m);
  }
  /**
   * A variant of {@link MatcherAssert#assertThat(Object, Matcher)} which
   * compares {link @String} representation of an object, with an expected such
   * representation, while ignoring white space characters, unless these occur
   * between identifiers.
   *
   * @param actual the actual object
   * @param expected the expected textual representation of the first parameter
   */
  public static void that(final Object actual, final Wrapper<@Nullable String> expected) {
    that(compressSpaces(actual + ""), is(compressSpaces(expected.get())));
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>short</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(Object, Matcher)}; the boxing in the
   * present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param s JD
   * @param m JD
   * @see MatcherAssert#assertThat(Object, Matcher)
   */
  public static void that(final short s, final Matcher<? super @Nullable Short> m) {
    that(Short.valueOf(s), m);
  }

  /**
   * A non-auto-boxing version for the primitive type
   * <code><b>boolean</b></code> of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param b JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final boolean b, final Matcher<? super Boolean> m) {
    assertThat(reason, Boolean.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>byte</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param b JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final byte b, final Matcher<? super @Nullable Byte> m) {
    assertThat(reason, Byte.valueOf(b), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>char</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param c JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final char c, final Matcher<? super Character> m) {
    assertThat(reason, Character.valueOf(c), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>double</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param d JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final double d, final Matcher<? super @Nullable Double> m) {
    assertThat(reason, Double.valueOf(d), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>float</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param f JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final float f, final Matcher<? super Float> m) {
    assertThat(reason, Float.valueOf(f), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>int</b></code> of
   * function {@link MatcherAssert#assertThat(String, Object, Matcher)}; the
   * boxing in the present function is explicit, and after it is being carried
   * out, computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param i JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final int i, final Matcher<? super Integer> m) {
    assertThat(reason, Integer.valueOf(i), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>long</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param l JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final long l, final Matcher<? super @Nullable Long> m) {
    assertThat(reason, Long.valueOf(l), m);
  }
  /**
   * A non-auto-boxing version for the primitive type <code><b>short</b></code>
   * of the original Hamcrest function
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}; the boxing in
   * the present function is explicit, and after it is being carried out,
   * computation is delegated to
   * {@link MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason as in the original Hamcrest function
   *          {@link MatcherAssert#assertThat(String, Object, Matcher)}
   * @param s JD
   * @param m JD
   * @see MatcherAssert#assertThat(String, Object, Matcher)
   */
  public static void that(final String reason, final short s, final Matcher<? super Short> m) {
    assertThat(reason, Short.valueOf(s), m);
  }
  /**
   * @param <T> JD
   * @param actual
   * @param matcher
   */
  public static <@Nullable T> void that(final @Nullable T actual, final @Nullable Matcher<? super @Nullable T> matcher) {
    assertThat("", actual, matcher);
  }
  /**
   * @param b
   */
  public static void assertTrue(final boolean b) {
    assertTrue(b);
  }
  public static void assertTrue(final String s, final boolean b) {
    assertThat(s, Boolean.valueOf(b), is(Boolean.TRUE));
  }
  /**
   * Assert that an integer is zero
   *
   * @param i JD
   */
  public static void assertZero(final int i) {
    assertEquals(0, i);
  }
  /**
   * Creates a matcher that matches when both of the specified matchers match
   * the examined object.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;fab&quot;, both(containsString(&quot;a&quot;)).and(containsString(&quot;b&quot;)))
   * </pre>
   */
  public static <LHS> org.hamcrest.core.CombinableMatcher.CombinableBothMatcher<LHS> both(final Matcher<? super LHS> matcher) {
    return org.hamcrest.core.CombinableMatcher.<LHS> both(matcher);
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Boolean> comparesEqualTo(final boolean b) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Byte> comparesEqualTo(final byte b) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Byte.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>char</b></code>.
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Character> comparesEqualTo(final char c) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Character.valueOf(c));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>double</b></code>.
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Double> comparesEqualTo(final double d) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Double.valueOf(d));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>float</b></code>.
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Float> comparesEqualTo(final float f) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Integer> comparesEqualTo(final int i) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Integer.valueOf(i));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>long</b></code>.
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Long> comparesEqualTo(final long l) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Long.valueOf(l));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>short</b></code>.
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Short> comparesEqualTo(final short s) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Short.valueOf(s));
  }
  /**
   * Creates a matcher that matches if the examined {@link String} contains the
   * specified {@link String} anywhere.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myStringOfNote&quot;, containsString(&quot;ring&quot;))
   * </pre>
   *
   * @param substring the substring that the returned matcher will expect to
   *          find within any examined string
   */
  public static @Nullable Matcher<java.lang.String> containsString(final java.lang.String substring) {
    return org.hamcrest.core.StringContains.containsString(substring);
  }
  /**
   * Wraps an existing matcher, overriding its description with that specified.
   * All other functions are delegated to the decorated matcher, including its
   * mismatch description.
   * <p/>
   * For example:
   *
   * <pre>
   * describedAs(&quot;a big decimal equal to %0&quot;, equalTo(myBigDecimal), myBigDecimal.toPlainString())
   * </pre>
   *
   * @param description the new description for the wrapped matcher
   * @param matcher the matcher to wrap
   * @param values optional values to insert into the tokenised description
   */
  public static <T> Matcher<T> describedAs(final java.lang.String description, final Matcher<T> matcher,
      final java.lang.Object... values) {
    return org.hamcrest.core.DescribedAs.<T> describedAs(description, matcher, values);
  }
  /**
   * Creates a matcher that matches when either of the specified matchers match
   * the examined object.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;fan&quot;, either(containsString(&quot;a&quot;)).and(containsString(&quot;b&quot;)))
   * </pre>
   */
  public static <LHS> org.hamcrest.core.CombinableMatcher.CombinableEitherMatcher<LHS> either(
      final Matcher<? super LHS> matcher) {
    return org.hamcrest.core.CombinableMatcher.<LHS> either(matcher);
  }
  /**
   * Creates a matcher that matches if the examined {@link String} ends with the
   * specified {@link String}.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myStringOfNote&quot;, endsWith(&quot;Note&quot;))
   * </pre>
   *
   * @param suffix the substring that the returned matcher will expect at the
   *          end of any examined string
   */
  public static @Nullable Matcher<java.lang.String> endsWith(final java.lang.String suffix) {
    return org.hamcrest.core.StringEndsWith.endsWith(suffix);
  }
  /**
   * Creates a matcher that matches when the examined object is logically equal
   * to the specified <code>operand</code>, as determined by calling the
   * {@link java.lang.Object#equals} method on the <b>examined</b> object.
   *
   * <p>
   * If the specified operand is <code>null</code> then the created matcher will
   * only match if the examined object's <code>equals</code> method returns
   * <code>true</code> when passed a <code>null</code> (which would be a
   * violation of the <code>equals</code> contract), unless the examined object
   * itself is <code>null</code>, in which case the matcher will return a
   * positive match.
   * </p>
   *
   * <p>
   * The created matcher provides a special behaviour when examining
   * <code>Array</code>s, whereby it will match if both the operand and the
   * examined object are arrays of the same length and contain items that are
   * equal to each other (according to the above rules) <b>in the same
   * indexes</b>.
   * </p>
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;foo&quot;, equalTo(&quot;foo&quot;));
   * assertThat(new String[] { &quot;foo&quot;, &quot;bar&quot; }, equalTo(new String[] { &quot;foo&quot;, &quot;bar&quot; }));
   * </pre>
   */
  public static <T> Matcher<T> equalTo(final T operand) {
    return org.hamcrest.core.IsEqual.<T> equalTo(operand);
  }
  /**
   * Creates a matcher for {@link Iterable}s that only matches when a single
   * pass over the examined {@link Iterable} yields items that are all matched
   * by the specified <code>itemMatcher</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(Arrays.asList(&quot;bar&quot;, &quot;baz&quot;), everyItem(startsWith(&quot;ba&quot;)))
   * </pre>
   *
   * @param itemMatcher the matcher to apply to every item provided by the
   *          examined {@link Iterable}
   */
  public static <U> Matcher<java.lang.Iterable<U>> everyItem(final Matcher<U> itemMatcher) {
    return org.hamcrest.core.Every.<U> everyItem(itemMatcher);
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Boolean> greaterThan(final boolean b) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Byte> greaterThan(final byte b) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Byte.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>char</b></code> .
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Character> greaterThan(final char c) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Character.valueOf(c));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>double</b></code> .
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Double> greaterThan(final double d) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Double.valueOf(d));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>float</b></code> .
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Float> greaterThan(final float f) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Integer> greaterThan(final int i) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Integer.valueOf(i));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>long</b></code> .
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Long> greaterThan(final long l) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Long.valueOf(l));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>short</b></code> .
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Short> greaterThan(final short s) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Short.valueOf(s));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo} for
   * primitive type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Boolean> greaterThanOrEqualTo(final boolean b) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo} for
   * primitive type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Byte> greaterThanOrEqualTo(final byte b) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Byte.valueOf(b));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>char</b></code> .
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Character> greaterThanOrEqualTo(final char c) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Character.valueOf(c));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>double</b></code> .
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Double> greaterThanOrEqualTo(final double d) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Double.valueOf(d));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>float</b></code> .
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Float> greaterThanOrEqualTo(final float f) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo} for
   * primitive type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Integer> greaterThanOrEqualTo(final int i) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(i));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>long</b></code> .
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Long> greaterThanOrEqualTo(final long l) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Long.valueOf(l));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>short</b></code> .
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static @Nullable Matcher<Short> greaterThanOrEqualTo(final short s) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Short.valueOf(s));
  }
  /**
   * Creates a matcher for {@link Iterable}s that only matches when a single
   * pass over the examined {@link Iterable} yields at least one item that is
   * matched by the specified <code>itemMatcher</code>. Whilst matching, the
   * traversal of the examined {@link Iterable} will stop as soon as a matching
   * item is found.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(Arrays.asList(&quot;foo&quot;, &quot;bar&quot;), hasItem(startsWith(&quot;ba&quot;)))
   * </pre>
   *
   * @param itemMatcher the matcher to apply to items provided by the examined
   *          {@link Iterable}
   */
  public static <T> Matcher<java.lang.Iterable<? super @Nullable T>> hasItem(final Matcher<? super @Nullable T> itemMatcher) {
    return org.hamcrest.core.IsCollectionContaining.<T> hasItem(itemMatcher);
  }
  /**
   * Creates a matcher for {@link Iterable}s that only matches when a single
   * pass over the examined {@link Iterable} yields at least one item that is
   * equal to the specified <code>item</code>. Whilst matching, the traversal of
   * the examined {@link Iterable} will stop as soon as a matching item is
   * found.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(Arrays.asList(&quot;foo&quot;, &quot;bar&quot;), hasItem(&quot;bar&quot;))
   * </pre>
   *
   * @param item the item to compare against the items provided by the examined
   *          {@link Iterable}
   */
  public static <T> Matcher<java.lang.Iterable<? super @Nullable T>> hasItem(final T item) {
    return org.hamcrest.core.IsCollectionContaining.<T> hasItem(item);
  }
  /**
   * Creates a matcher for {@link Iterable}s that matches when consecutive
   * passes over the examined {@link Iterable} yield at least one item that is
   * matched by the corresponding matcher from the specified
   * <code>itemMatchers</code>. Whilst matching, each traversal of the examined
   * {@link Iterable} will stop as soon as a matching item is found.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(Arrays.asList(&quot;foo&quot;, &quot;bar&quot;, &quot;baz&quot;), hasItems(endsWith(&quot;z&quot;), endsWith(&quot;o&quot;)))
   * </pre>
   *
   * @param itemMatchers the matchers to apply to items provided by the examined
   *          {@link Iterable}
   */
  @SafeVarargs public static <T> Matcher<java.lang.Iterable<T>> hasItems(final Matcher<? super T>... itemMatchers) {
    return org.hamcrest.core.IsCollectionContaining.<T> hasItems(itemMatchers);
  }
  /**
   * Creates a matcher for {@link Iterable}s that matches when consecutive
   * passes over the examined {@link Iterable} yield at least one item that is
   * equal to the corresponding item from the specified <code>items</code>.
   * Whilst matching, each traversal of the examined {@link Iterable} will stop
   * as soon as a matching item is found.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(Arrays.asList(&quot;foo&quot;, &quot;bar&quot;, &quot;baz&quot;), hasItems(&quot;baz&quot;, &quot;foo&quot;))
   * </pre>
   *
   * @param items the items to compare against the items provided by the
   *          examined {@link Iterable}
   */
  @SafeVarargs public static <T> Matcher<java.lang.Iterable<T>> hasItems(final T... items) {
    return org.hamcrest.core.IsCollectionContaining.<T> hasItems(items);
  }
  /**
   * Creates a matcher that matches when the examined object is an instance of
   * the specified <code>type</code>, as determined by calling the
   * {@link java.lang.Class#isInstance(Object)} method on that type, passing the
   * the examined object.
   *
   * <p>
   * The created matcher assumes no relationship between specified type and the
   * examined object.
   * </p>
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(new Canoe(), instanceOf(Paddlable.class));
   * </pre>
   *
   * @param type JD
   * @return
   */
  public static <T> Matcher<T> instanceOf(final java.lang.Class<?> type) {
    return org.hamcrest.core.IsInstanceOf.<T> instanceOf(type);
  }
  /**
   * A shortcut to the frequently used <code>is(new Boolean(...))</code>.
   *
   * @param b JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Boolean> is(final boolean b) {
    return is(Boolean.valueOf(b));
  }
  /**
   * A shortcut to the frequently used <code>is(new Byte(...))</code>.
   *
   * @param b JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Byte> is(final byte b) {
    return is(Byte.valueOf(b));
  }
  /**
   * A shortcut to the frequently used <code>is(new Character(...))</code>.
   *
   * @param c JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Character> is(final char c) {
    return is(Character.valueOf(c));
  }
  /**
   * A shortcut to the frequently used <code>is(new Double(...))</code>.
   *
   * @param d JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Double> is(final double d) {
    return is(Double.valueOf(d));
  }
  /**
   * A shortcut to the frequently used <code>is(new Float(...))</code>.
   *
   * @param f JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Float> is(final float f) {
    return is(Float.valueOf(f));
  }
  /**
   * A shortcut to the frequently used <code>is(new Integer(...))</code>.
   *
   * @param i JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Integer> is(final int i) {
    return is(Integer.valueOf(i));
  }
  /**
   * A shortcut to the frequently used <code>is(new Long(...))</code>.
   *
   * @param l JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Long> is(final long l) {
    return is(Long.valueOf(l));
  }
  /**
   * Decorates another Matcher, retaining its behavior, but allowing tests to be
   * slightly more expressive, e.g.,
   *
   * <pre>
   * assertThat(cheese, is(equalTo(smelly)))
   * </pre>
   *
   * instead of:
   *
   * <pre>
   * assertThat(cheese, equalTo(smelly))
   * </pre>
   */
  public static <T> Matcher<T> is(final Matcher<T> matcher) {
    return org.hamcrest.core.Is.<T> is(matcher);
  }
  /**
   * A shortcut to the frequently used <code>is(new Short(...))</code>.
   *
   * @param s JD
   * @return a matcher for the value specified by the parameter
   */
  public static @Nullable Matcher<Short> is(final short s) {
    return is(Short.valueOf(s));
  }
  /**
   * A shortcut to the frequently used <code>is(equalTo(x))</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(smelly))
   * </pre>
   *
   * instead of:
   *
   * <pre>
   * assertThat(cheese, is(equalTo(smelly)))
   * </pre>
   */
  public static <T> Matcher<T> is(final T value) {
    return org.hamcrest.core.Is.<T> is(value);
  }
  /**
   * A shortcut to the frequently used
   * <code>is(instanceOf(SomeClass.class))</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, isA(Cheddar.class))
   * </pre>
   *
   * instead of:
   *
   * <pre>
   * assertThat(cheese, is(instanceOf(Cheddar.class)))
   * </pre>
   */
  public static <T> Matcher<T> isA(final java.lang.Class<T> type) {
    return org.hamcrest.core.Is.<T> isA(type);
  }
  /**
   * Wraps the provided {@link String}
   *
   * @param s a {@link String} to wrap
   * @return a wrapped {@link String}
   */
  public static final Wrapper<String> iz(final @NonNull String s) {
    return new Wrapper<String>(s);
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThan} for primitive type
   * <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Boolean> lessThan(final boolean b) {
    return org.hamcrest.number.OrderingComparison.lessThan(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThan} for primitive type
   * <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Byte> lessThan(final byte b) {
    return org.hamcrest.number.OrderingComparison.lessThan(Byte.valueOf(b));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>char</b></code>.
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Character> lessThan(final char c) {
    return org.hamcrest.number.OrderingComparison.lessThan(Character.valueOf(c));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>double</b></code> .
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Double> lessThan(final double d) {
    return org.hamcrest.number.OrderingComparison.lessThan(Double.valueOf(d));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>float</b></code>.
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Float> lessThan(final float f) {
    return org.hamcrest.number.OrderingComparison.lessThan(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThan} for primitive type
   * <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Integer> lessThan(final int i) {
    return org.hamcrest.number.OrderingComparison.lessThan(Integer.valueOf(i));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>long</b></code>.
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Long> lessThan(final long l) {
    return org.hamcrest.number.OrderingComparison.lessThan(Long.valueOf(l));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>short</b></code>.
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static @Nullable Matcher<Short> lessThan(final short s) {
    return org.hamcrest.number.OrderingComparison.lessThan(Short.valueOf(s));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Boolean> lessThanOrEqualTo(final boolean b) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Byte> lessThanOrEqualTo(final byte b) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Byte.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>char</b></code>.
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Character> lessThanOrEqualTo(final char c) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Character.valueOf(c));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>double</b></code>.
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Double> lessThanOrEqualTo(final double d) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Double.valueOf(d));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>float</b></code>.
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Float> lessThanOrEqualTo(final float f) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Integer> lessThanOrEqualTo(final int i) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Integer.valueOf(i));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>long</b></code>.
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Long> lessThanOrEqualTo(final long l) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Long.valueOf(l));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>short</b></code>.
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static @Nullable Matcher<Short> lessThanOrEqualTo(final short s) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Short.valueOf(s));
  }
  public static @Nullable Matcher<Boolean> not(final boolean b) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Boolean(b)));
  }
  public static @Nullable Matcher<Byte> not(final byte b) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Byte(b)));
  }
  public static @Nullable Matcher<Character> not(final char i) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Character(i)));
  }
  public static @Nullable Matcher<Double> not(final double d) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Double(d)));
  }
  public static @Nullable Matcher<Float> not(final float f) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Float(f)));
  }
  public static @Nullable Matcher<Integer> not(final int i) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Integer(i)));
  }
  public static @Nullable Matcher<Long> not(final long i) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Long(i)));
  }
  /**
   * Creates a matcher that wraps an existing matcher, but inverts the logic by
   * which it will match.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(not(equalTo(smelly))))
   * </pre>
   *
   * @param matcher the matcher whose sense should be inverted
   */
  public static <T> Matcher<T> not(final Matcher<T> matcher) {
    return org.hamcrest.core.IsNot.<T> not(matcher);
  }
  public static @Nullable Matcher<Short> not(final short s) {
    return cantBeNull(org.hamcrest.core.IsNot.not(new Short(s)));
  }
  /**
   * A shortcut to the frequently used <code>not(equalTo(x))</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(not(smelly)))
   * </pre>
   *
   * instead of:
   *
   * <pre>
   * assertThat(cheese, is(not(equalTo(smelly))))
   * </pre>
   *
   * @param value the value that any examined object should <b>not</b> equal
   */
  public static <T> Matcher<T> not(final T value) {
    return org.hamcrest.core.IsNot.<T> not(value);
  }
  /**
   * A shortcut to the frequently used <code>not(nullValue())</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(notNullValue()))
   * </pre>
   *
   * instead of:
   *
   * <pre>
   * assertThat(cheese, is(not(nullValue())))
   * </pre>
   */
  public static @Nullable Matcher<java.lang.Object> notNullValue() {
    return org.hamcrest.core.IsNull.notNullValue();
  }
  /**
   * A shortcut to the frequently used <code>not(nullValue(X.class)). Accepts a
   * single dummy argument to facilitate type inference.</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(notNullValue(X.class)))
   * </pre>
   *
   * instead of:
   *
   * <pre>
   * assertThat(cheese, is(not(nullValue(X.class))))
   * </pre>
   *
   * @param type dummy parameter used to infer the generic type of the returned
   *          matcher
   */
  public static <T> Matcher<T> notNullValue(final java.lang.Class<T> type) {
    return org.hamcrest.core.IsNull.<T> notNullValue(type);
  }
  /**
   * Creates a matcher that matches if examined object is <code>null</code>.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(nullValue())
   * </pre>
   */
  public static @Nullable Matcher<@Nullable Object> nullValue() {
    return org.hamcrest.core.IsNull.nullValue();
  }
  /**
   * Creates a matcher that matches if examined object is <code>null</code>.
   * Accepts a single dummy argument to facilitate type inference.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(cheese, is(nullValue(Cheese.class))
   * </pre>
   *
   * @param type dummy parameter used to infer the generic type of the returned
   *          matcher
   */
  public static <T> Matcher<T> nullValue(final java.lang.Class<T> type) {
    return org.hamcrest.core.IsNull.<T> nullValue(type);
  }
  /**
   * Creates a matcher that matches only when the examined object is the same
   * instance as the specified target object.
   *
   * @param target the target instance against which others should be assessed
   */
  public static <T> Matcher<T> sameInstance(final T target) {
    return org.hamcrest.core.IsSame.<T> sameInstance(target);
  }
  /**
   * Creates a matcher that matches if the examined {@link String} starts with
   * the specified {@link String}.
   * <p/>
   * For example:
   *
   * <pre>
   * assertThat(&quot;myStringOfNote&quot;, startsWith(&quot;my&quot;))
   * </pre>
   *
   * @param prefix the substring that the returned matcher will expect at the
   *          start of any examined string
   */
  public static @Nullable Matcher<java.lang.String> startsWith(final java.lang.String prefix) {
    return org.hamcrest.core.StringStartsWith.startsWith(prefix);
  }
  /**
   * Creates a matcher that matches only when the examined object is the same
   * instance as the specified target object.
   *
   * @param target the target instance against which others should be assessed
   */
  public static <T> Matcher<T> theInstance(final T target) {
    return org.hamcrest.core.IsSame.<T> theInstance(target);
  }
}

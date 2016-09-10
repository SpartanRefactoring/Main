package il.org.spartan;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.annotation.*;
import org.hamcrest.*;
import org.hamcrest.core.*;
import org.hamcrest.number.*;
import org.junit.*;

/** @author Yossi Gil
 * @since 2015-07-18 */
@SuppressWarnings({ "javadoc", "null" }) //
public class azzert extends org.junit.Assert {
  public static <T> Matcher<T> allOf(final java.lang.Iterable<Matcher<? super T>> matchers) {
    return AllOf.<T> allOf(matchers);
  }

  @SafeVarargs public static <T> Matcher<T> allOf(final Matcher<? super T>... matchers) {
    return AllOf.<T> allOf(matchers);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second) {
    return AllOf.<T> allOf(first, second);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
    return AllOf.<T> allOf(first, second, third);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third,
      final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return AllOf.<T> allOf(first, second, third, fourth, fifth);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third,
      final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
    return AllOf.<T> allOf(first, second, third, fourth, fifth, sixth);
  }

  public static <T> Matcher<T> any(final java.lang.Class<T> type) {
    return IsInstanceOf.<T> any(type);
  }

  public static <T> @Nullable AnyOf<T> anyOf(final java.lang.Iterable<Matcher<? super T>> matchers) {
    return AnyOf.<T> anyOf(matchers);
  }

  @SafeVarargs public static <T> @Nullable AnyOf<T> anyOf(final Matcher<? super T>... matchers) {
    return AnyOf.<T> anyOf(matchers);
  }

  public static <T> @Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second) {
    return AnyOf.<T> anyOf(first, second);
  }

  public static <T> @Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
    return AnyOf.<T> anyOf(first, second, third);
  }

  public static <T> @Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third,
      final Matcher<? super T> fourth) {
    return AnyOf.<T> anyOf(first, second, third, fourth);
  }

  public static <T> @Nullable AnyOf<@Nullable T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third,
      final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return AnyOf.<T> anyOf(first, second, third, fourth, fifth);
  }

  public static <T> @Nullable AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third,
      final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
    return AnyOf.<T> anyOf(first, second, third, fourth, fifth, sixth);
  }

  public static Matcher<@Nullable Object> anything() {
    return IsAnything.anything();
  }

  public static Matcher<@Nullable Object> anything(final @Nullable String description) {
    return IsAnything.anything(description);
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

  public static void assertFalse(final boolean b) {
    that("", Boolean.valueOf(b), is(Boolean.FALSE));
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

  public static void assertTrue(final boolean b) {
    that("", Boolean.valueOf(b), is(Boolean.TRUE));
  }

  public static void assertTrue(final String s, final boolean b) {
    that(s, Boolean.valueOf(b), is(Boolean.TRUE));
  }

  public static Asserter aye(final boolean claim) {
    return aye("", claim);
  }

  public static Asserter aye(final String reason, final boolean claim) {
    return new Asserter().andAye(reason, claim);
  }

  public static <LHS> CombinableMatcher.@Nullable CombinableBothMatcher<LHS> both(final Matcher<? super LHS> matcher) {
    return CombinableMatcher.<LHS> both(matcher);
  }

  @Factory public static Matcher<@Nullable Boolean> comparesEqualTo(final boolean b) {
    return OrderingComparison.comparesEqualTo(Boolean.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Byte> comparesEqualTo(final byte b) {
    return OrderingComparison.comparesEqualTo(Byte.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Character> comparesEqualTo(final char c) {
    return OrderingComparison.comparesEqualTo(Character.valueOf(c));
  }

  @Factory public static Matcher<@Nullable Double> comparesEqualTo(final double d) {
    return OrderingComparison.comparesEqualTo(Double.valueOf(d));
  }

  @Factory public static Matcher<@Nullable Float> comparesEqualTo(final float f) {
    return OrderingComparison.comparesEqualTo(Float.valueOf(f));
  }

  @Factory public static Matcher<@Nullable Integer> comparesEqualTo(final int i) {
    return OrderingComparison.comparesEqualTo(Integer.valueOf(i));
  }

  @Factory public static Matcher<@Nullable Long> comparesEqualTo(final long l) {
    return OrderingComparison.comparesEqualTo(Long.valueOf(l));
  }

  @Factory public static Matcher<@Nullable Short> comparesEqualTo(final short s) {
    return OrderingComparison.comparesEqualTo(Short.valueOf(s));
  }

  public static Matcher<@Nullable String> containsString(final String substring) {
    return StringContains.containsString(substring);
  }

  public static <T> Matcher<T> describedAs(final String description, final Matcher<T> matcher, final Object... values) {
    return DescribedAs.<T> describedAs(description, matcher, values);
  }

  public static <LHS> CombinableMatcher.@Nullable CombinableEitherMatcher<LHS> either(final Matcher<? super LHS> matcher) {
    return CombinableMatcher.<LHS> either(matcher);
  }

  public static Matcher<String> endsWith(final String suffix) {
    return StringEndsWith.endsWith(suffix);
  }

  public static <T> Matcher<T> equalTo(final T operand) {
    return IsEqual.<T> equalTo(operand);
  }

  public static Matcher<String> equalToIgnoringCase(final String expectedString) {
    return org.hamcrest.Matchers.equalToIgnoringCase(expectedString);
  }

  public static Matcher<String> equalToIgnoringWhiteSpace(final String expectedString) {
    return org.hamcrest.Matchers.equalToIgnoringWhiteSpace(expectedString);
  }

  public static <U> Matcher<java.lang.Iterable<U>> everyItem(final Matcher<U> itemMatcher) {
    return Every.<U> everyItem(itemMatcher);
  }

  public static void fail() {
    Assert.fail();
  }

  public static void fail(final String s) {
    Assert.fail(s);
  }

  public static void falze(final boolean b) {
    assertFalse("", b);
  }

  @Factory public static Matcher<@Nullable Boolean> greaterThan(final boolean b) {
    return OrderingComparison.greaterThan(Boolean.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Byte> greaterThan(final byte b) {
    return OrderingComparison.greaterThan(Byte.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Character> greaterThan(final char c) {
    return OrderingComparison.greaterThan(Character.valueOf(c));
  }

  @Factory public static Matcher<@Nullable Double> greaterThan(final double d) {
    return OrderingComparison.greaterThan(Double.valueOf(d));
  }

  @Factory public static Matcher<@Nullable Float> greaterThan(final float f) {
    return OrderingComparison.greaterThan(Float.valueOf(f));
  }

  @Factory public static Matcher<@Nullable Integer> greaterThan(final int i) {
    return OrderingComparison.greaterThan(Integer.valueOf(i));
  }

  @Factory public static Matcher<@Nullable Long> greaterThan(final long l) {
    return OrderingComparison.greaterThan(Long.valueOf(l));
  }

  @Factory public static Matcher<@Nullable Short> greaterThan(final short s) {
    return OrderingComparison.greaterThan(Short.valueOf(s));
  }

  @Factory public static Matcher<@Nullable Boolean> greaterThanOrEqualTo(final boolean b) {
    return OrderingComparison.greaterThanOrEqualTo(Boolean.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Byte> greaterThanOrEqualTo(final byte b) {
    return OrderingComparison.greaterThanOrEqualTo(Byte.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Character> greaterThanOrEqualTo(final char c) {
    return OrderingComparison.greaterThanOrEqualTo(Character.valueOf(c));
  }

  @Factory public static Matcher<@Nullable Double> greaterThanOrEqualTo(final double d) {
    return OrderingComparison.greaterThanOrEqualTo(Double.valueOf(d));
  }

  @Factory public static Matcher<@Nullable Float> greaterThanOrEqualTo(final float f) {
    return OrderingComparison.greaterThanOrEqualTo(Float.valueOf(f));
  }

  @Factory public static Matcher<@Nullable Integer> greaterThanOrEqualTo(final int i) {
    return OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(i));
  }

  @Factory public static Matcher<@Nullable Long> greaterThanOrEqualTo(final long l) {
    return OrderingComparison.greaterThanOrEqualTo(Long.valueOf(l));
  }

  @Factory public static Matcher<@Nullable Short> greaterThanOrEqualTo(final short s) {
    return OrderingComparison.greaterThanOrEqualTo(Short.valueOf(s));
  }

  public static <T> Matcher<java.lang.Iterable<? super @Nullable T>> hasItem(final Matcher<? super @Nullable T> itemMatcher) {
    return IsCollectionContaining.<T> hasItem(itemMatcher);
  }

  public static <T> Matcher<java.lang.Iterable<? super @Nullable T>> hasItem(final T item) {
    return IsCollectionContaining.<T> hasItem(item);
  }

  @SafeVarargs public static <T> Matcher<java.lang.Iterable<T>> hasItems(final Matcher<? super T>... itemMatchers) {
    return IsCollectionContaining.<T> hasItems(itemMatchers);
  }

  @SafeVarargs public static <T> Matcher<java.lang.Iterable<T>> hasItems(final T... items) {
    return IsCollectionContaining.<T> hasItems(items);
  }

  public static <T> Matcher<T> instanceOf(final java.lang.Class<?> type) {
    return IsInstanceOf.<T> instanceOf(type);
  }

  public static Matcher<@Nullable Boolean> is(final boolean b) {
    return is(Boolean.valueOf(b));
  }

  public static Matcher<@Nullable Byte> is(final byte b) {
    return is(Byte.valueOf(b));
  }

  public static Matcher<@Nullable Character> is(final char c) {
    return is(Character.valueOf(c));
  }

  public static Matcher<@Nullable Double> is(final double d) {
    return is(Double.valueOf(d));
  }

  public static Matcher<@Nullable Float> is(final float f) {
    return is(Float.valueOf(f));
  }

  public static Matcher<@Nullable Integer> is(final int i) {
    return is(Integer.valueOf(i));
  }

  public static Matcher<@Nullable Long> is(final long l) {
    return is(Long.valueOf(l));
  }

  public static <T> Matcher<T> is(final Matcher<T> matcher) {
    return Is.<T> is(matcher);
  }

  public static Matcher<@Nullable Short> is(final short s) {
    return is(Short.valueOf(s));
  }

  public static <@Nullable T> Matcher<T> is(final @Nullable T value) {
    return Is.<T> is(value);
  }

  public static <T> Matcher<T> isA(final java.lang.Class<T> type) {
    return Is.<T> isA(type);
  }

  public static void isNull(final @Nullable Object o) {
    that(o, nullValue());
  }

  /** @param message what to print
   * @param o what to examine */
  public static void isNull(final String message, @Nullable final Object o) {
    assertNull(message, o);
  }

  public static Wrapper<String> iz(final String s) {
    return new Wrapper<>(s);
  }

  @Factory public static Matcher<@Nullable Boolean> lessThan(final boolean b) {
    return OrderingComparison.lessThan(Boolean.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Byte> lessThan(final byte b) {
    return OrderingComparison.lessThan(Byte.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Character> lessThan(final char c) {
    return OrderingComparison.lessThan(Character.valueOf(c));
  }

  @Factory public static Matcher<@Nullable Double> lessThan(final double d) {
    return OrderingComparison.lessThan(Double.valueOf(d));
  }

  @Factory public static Matcher<@Nullable Float> lessThan(final float f) {
    return OrderingComparison.lessThan(Float.valueOf(f));
  }

  @Factory public static Matcher<@Nullable Integer> lessThan(final int i) {
    return OrderingComparison.lessThan(Integer.valueOf(i));
  }

  @Factory public static Matcher<@Nullable Long> lessThan(final long l) {
    return OrderingComparison.lessThan(Long.valueOf(l));
  }

  @Factory public static Matcher<@Nullable Short> lessThan(final short s) {
    return OrderingComparison.lessThan(Short.valueOf(s));
  }

  @Factory public static Matcher<@Nullable Boolean> lessThanOrEqualTo(final boolean b) {
    return OrderingComparison.lessThanOrEqualTo(Boolean.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Byte> lessThanOrEqualTo(final byte b) {
    return OrderingComparison.lessThanOrEqualTo(Byte.valueOf(b));
  }

  @Factory public static Matcher<@Nullable Character> lessThanOrEqualTo(final char c) {
    return OrderingComparison.lessThanOrEqualTo(Character.valueOf(c));
  }

  @Factory public static Matcher<@Nullable Double> lessThanOrEqualTo(final double d) {
    return OrderingComparison.lessThanOrEqualTo(Double.valueOf(d));
  }

  @Factory public static Matcher<@Nullable Float> lessThanOrEqualTo(final float f) {
    return OrderingComparison.lessThanOrEqualTo(Float.valueOf(f));
  }

  @Factory public static Matcher<@Nullable Integer> lessThanOrEqualTo(final int i) {
    return OrderingComparison.lessThanOrEqualTo(Integer.valueOf(i));
  }

  @Factory public static Matcher<@Nullable Long> lessThanOrEqualTo(final long l) {
    return OrderingComparison.lessThanOrEqualTo(Long.valueOf(l));
  }

  @Factory public static Matcher<@Nullable Short> lessThanOrEqualTo(final short s) {
    return OrderingComparison.lessThanOrEqualTo(Short.valueOf(s));
  }

  public static Asserter nay(final boolean claim) {
    return nay("", claim);
  }

  @NonNull public static Asserter nay(final String reason, final boolean claim) {
    return new Asserter().andNay(reason, claim);
  }

  public static void nonNulls(final @Nullable Iterable<@Nullable Object> os) {
    azzert.notNull(os);
    assert os != null;
    for (final @Nullable Object o : os)
      azzert.notNull(os + "", o);
  }

  public static Matcher<Boolean> not(final boolean b) {
    return cantBeNull(IsNot.not(new Boolean(b)));
  }

  public static Matcher<Byte> not(final byte b) {
    return cantBeNull(IsNot.not(new Byte(b)));
  }

  public static Matcher<Character> not(final char i) {
    return cantBeNull(IsNot.not(new Character(i)));
  }

  public static Matcher<Double> not(final double d) {
    return cantBeNull(IsNot.not(new Double(d)));
  }

  public static Matcher<Float> not(final float f) {
    return cantBeNull(IsNot.not(new Float(f)));
  }

  public static Matcher<Integer> not(final int i) {
    return cantBeNull(IsNot.not(new Integer(i)));
  }

  public static Matcher<Long> not(final long i) {
    return cantBeNull(IsNot.not(new Long(i)));
  }

  public static <T> Matcher<T> not(final Matcher<T> matcher) {
    return IsNot.<T> not(matcher);
  }

  public static Matcher<Short> not(final short s) {
    return cantBeNull(IsNot.not(new Short(s)));
  }

  public static <T> Matcher<T> not(final T value) {
    return IsNot.<T> not(value);
  }

  public static void notNull(final @Nullable Object o) {
    that(o, notNullValue());
  }

  public static void notNull(final String s, final @Nullable Object o) {
    assertThat(s, o, notNullValue());
  }

  public static Matcher<@Nullable Object> notNullValue() {
    return IsNull.notNullValue();
  }

  public static <T> Matcher<T> notNullValue(final java.lang.Class<T> type) {
    return IsNull.<T> notNullValue(type);
  }

  public static void notNullz(final @Nullable Object @Nullable... os) {
    assert os != null;
    notNull(os);
    for (final @Nullable Object o : os)
      notNull(os + "", o);
  }

  public static Matcher<@Nullable Object> nullValue() {
    return IsNull.nullValue();
  }

  public static <T> Matcher<T> nullValue(final java.lang.Class<T> type) {
    return IsNull.<T> nullValue(type);
  }

  public static void positive(final int i) {
    azzert.that(i, greaterThan(0));
  }

  public static <T> Matcher<T> sameInstance(final T target) {
    return IsSame.<T> sameInstance(target);
  }

  public static Matcher<String> startsWith(final String prefix) {
    return StringStartsWith.startsWith(prefix);
  }

  public static void that(final boolean b, final Matcher<? super @Nullable Boolean> m) {
    that(Boolean.valueOf(b), m);
  }

  public static void that(final byte b, final Matcher<? super @Nullable Byte> m) {
    that(Byte.valueOf(b), m);
  }

  public static void that(final char c, final Matcher<? super @Nullable Character> m) {
    that(Character.valueOf(c), m);
  }

  public static void that(final double d, final Matcher<? super @Nullable Double> m) {
    that(Double.valueOf(d), m);
  }

  public static void that(final float f, final Matcher<? super @Nullable Float> m) {
    that(Float.valueOf(f), m);
  }

  public static void that(final int i, final Matcher<? super @Nullable Integer> m) {
    that(Integer.valueOf(i), m);
  }

  public static void that(final long l, final Matcher<? super @Nullable Long> m) {
    that(Long.valueOf(l), m);
  }

  public static void that(final Object actual, final Wrapper<@Nullable String> expected) {
    that(compressSpaces(actual + ""), is(compressSpaces(expected.get())));
  }

  public static void that(final short s, final Matcher<? super @Nullable Short> m) {
    that(Short.valueOf(s), m);
  }

  public static void that(final String reason, final boolean b, final Matcher<? super Boolean> m) {
    assertThat(reason, Boolean.valueOf(b), m);
  }

  public static void that(final String reason, final byte b, final Matcher<? super @Nullable Byte> m) {
    assertThat(reason, Byte.valueOf(b), m);
  }

  public static void that(final String reason, final char c, final Matcher<? super Character> m) {
    assertThat(reason, Character.valueOf(c), m);
  }

  public static void that(final String reason, final double d, final Matcher<? super @Nullable Double> m) {
    assertThat(reason, Double.valueOf(d), m);
  }

  public static void that(final String reason, final float f, final Matcher<? super Float> m) {
    assertThat(reason, Float.valueOf(f), m);
  }

  public static void that(final String reason, final int i, final Matcher<? super Integer> m) {
    assertThat(reason, Integer.valueOf(i), m);
  }

  public static void that(final String reason, final long l, final Matcher<? super @Nullable Long> m) {
    assertThat(reason, Long.valueOf(l), m);
  }

  public static void that(final String reason, final short s, final Matcher<? super Short> m) {
    assertThat(reason, Short.valueOf(s), m);
  }

  public static <@Nullable T> void that(final String reason, final @Nullable T actual, final Matcher<? super @Nullable T> matcher) {
    assertThat(reason, actual, matcher);
  }

  public static <@Nullable T> void that(final @Nullable T actual, final Matcher<? super @Nullable T> matcher) {
    assertThat("", actual, matcher);
  }

  public static <T> Matcher<T> theInstance(final T target) {
    return IsSame.<T> theInstance(target);
  }

  /** Assert that an integer is zero
   * @param i JD */
  public static void zero(final int i) {
    assertEquals(0, i);
  }

  /** Assert that long is zero
   * @param l JD */
  public static void zero(final long l) {
    assertEquals(0L, l);
  }

  public static class ____META {
    static class Inline {
      static <T> void assertEquals(final String reason, final T t1, final T t2) {
        azzert.that(reason, t2, is(t1));
      }

      static <T> void assertEquals(final T t1, final T t2) {
        azzert.that(t2, is(t1));
      }

      static void assertFalse(final boolean b) {
        azzert.nay(b);
      }

      static void assertFalse(final String reason, final boolean b) {
        azzert.nay(reason, b);
      }

      static <T> void assertNotEquals(final T t1, final T t2) {
        azzert.that(t2, is(t1));
      }

      static <T> void assertNotNull(final T t) {
        azzert.notNull(t);
      }

      static <T> void assertNull(final T t) {
        azzert.isNull(t);
      }

      static void assertTrue(final boolean b) {
        azzert.aye(b);
      }

      static void assertTrue(final String reason, final boolean b) {
        azzert.aye(reason, b);
      }

      static void assertZero(final int i) {
        azzert.zero(i);
      }
    }
  }

  public static class Asserter {
    public @NonNull Asserter andAye(final boolean claim) {
      return andAye("", claim);
    }

    public Asserter andAye(final String reason, final boolean claim) {
      azzert.that(reason, claim, is(true));
      return this;
    }

    public Asserter andNay(final boolean claim) {
      return andNay("", claim);
    }

    public Asserter andNay(final String reason, final boolean claim) {
      azzert.that(reason, claim, is(false));
      return this;
    }
  }
}

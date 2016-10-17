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
  public static <T> Matcher<T> allOf(final java.lang.Iterable<Matcher<? super T>> ¢) {
    return AllOf.<T> allOf(¢);
  }

  @SafeVarargs public static <T> Matcher<T> allOf(final Matcher<? super T>... ¢) {
    return AllOf.<T> allOf(¢);
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

  public static <T> @Nullable AnyOf<T> anyOf(final java.lang.Iterable<Matcher<? super T>> ¢) {
    return AnyOf.<T> anyOf(¢);
  }

  @SafeVarargs public static <T> @Nullable AnyOf<T> anyOf(final Matcher<? super T>... ¢) {
    return AnyOf.<T> anyOf(¢);
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

  public static void assertFalse(final boolean ¢) {
    that("", Boolean.valueOf(¢), is(Boolean.FALSE));
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

  public static void assertTrue(final boolean ¢) {
    that("", Boolean.valueOf(¢), is(Boolean.TRUE));
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

  public static <LHS> CombinableMatcher.@Nullable CombinableBothMatcher<LHS> both(final Matcher<? super LHS> ¢) {
    return CombinableMatcher.<LHS> both(¢);
  }

  @Factory public static Matcher<@Nullable Boolean> comparesEqualTo(final boolean ¢) {
    return OrderingComparison.comparesEqualTo(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Byte> comparesEqualTo(final byte ¢) {
    return OrderingComparison.comparesEqualTo(Byte.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Character> comparesEqualTo(final char ¢) {
    return OrderingComparison.comparesEqualTo(Character.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Double> comparesEqualTo(final double ¢) {
    return OrderingComparison.comparesEqualTo(Double.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Float> comparesEqualTo(final float ¢) {
    return OrderingComparison.comparesEqualTo(Float.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Integer> comparesEqualTo(final int ¢) {
    return OrderingComparison.comparesEqualTo(Integer.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Long> comparesEqualTo(final long ¢) {
    return OrderingComparison.comparesEqualTo(Long.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Short> comparesEqualTo(final short ¢) {
    return OrderingComparison.comparesEqualTo(Short.valueOf(¢));
  }

  public static Matcher<@Nullable String> containsString(final String substring) {
    return StringContains.containsString(substring);
  }

  public static <T> Matcher<T> describedAs(final String description, final Matcher<T> t, final Object... values) {
    return DescribedAs.<T> describedAs(description, t, values);
  }

  public static <LHS> CombinableMatcher.@Nullable CombinableEitherMatcher<LHS> either(final Matcher<? super LHS> ¢) {
    return CombinableMatcher.<LHS> either(¢);
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

  public static void fail(final String ¢) {
    Assert.fail(¢);
  }

  public static void falze(final boolean ¢) {
    assertFalse("", ¢);
  }

  @Factory public static Matcher<@Nullable Boolean> greaterThan(final boolean ¢) {
    return OrderingComparison.greaterThan(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Byte> greaterThan(final byte ¢) {
    return OrderingComparison.greaterThan(Byte.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Character> greaterThan(final char ¢) {
    return OrderingComparison.greaterThan(Character.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Double> greaterThan(final double ¢) {
    return OrderingComparison.greaterThan(Double.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Float> greaterThan(final float ¢) {
    return OrderingComparison.greaterThan(Float.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Integer> greaterThan(final int ¢) {
    return OrderingComparison.greaterThan(Integer.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Long> greaterThan(final long ¢) {
    return OrderingComparison.greaterThan(Long.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Short> greaterThan(final short ¢) {
    return OrderingComparison.greaterThan(Short.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Boolean> greaterThanOrEqualTo(final boolean ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Byte> greaterThanOrEqualTo(final byte ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Byte.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Character> greaterThanOrEqualTo(final char ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Character.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Double> greaterThanOrEqualTo(final double ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Double.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Float> greaterThanOrEqualTo(final float ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Float.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Integer> greaterThanOrEqualTo(final int ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Long> greaterThanOrEqualTo(final long ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Long.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Short> greaterThanOrEqualTo(final short ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Short.valueOf(¢));
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

  public static Matcher<@Nullable Boolean> is(final boolean ¢) {
    return is(Boolean.valueOf(¢));
  }

  public static Matcher<@Nullable Byte> is(final byte ¢) {
    return is(Byte.valueOf(¢));
  }

  public static Matcher<@Nullable Character> is(final char ¢) {
    return is(Character.valueOf(¢));
  }

  public static Matcher<@Nullable Double> is(final double ¢) {
    return is(Double.valueOf(¢));
  }

  public static Matcher<@Nullable Float> is(final float ¢) {
    return is(Float.valueOf(¢));
  }

  public static Matcher<@Nullable Integer> is(final int ¢) {
    return is(Integer.valueOf(¢));
  }

  public static Matcher<@Nullable Long> is(final long ¢) {
    return is(Long.valueOf(¢));
  }

  public static <T> Matcher<T> is(final Matcher<T> ¢) {
    return Is.<T> is(¢);
  }

  public static Matcher<@Nullable Short> is(final short ¢) {
    return is(Short.valueOf(¢));
  }

  public static <@Nullable T> Matcher<T> is(final @Nullable T value) {
    return Is.<T> is(value);
  }

  public static <T> Matcher<T> isA(final java.lang.Class<T> type) {
    return Is.<T> isA(type);
  }

  public static void isNull(final @Nullable Object ¢) {
    that(¢, nullValue());
  }

  /** @param message what to print
   * @param o what to examine */
  public static void isNull(final String message, @Nullable final Object o) {
    assertNull(message, o);
  }

  public static Wrapper<String> iz(final String ¢) {
    return new Wrapper<>(¢);
  }

  @Factory public static Matcher<@Nullable Boolean> lessThan(final boolean ¢) {
    return OrderingComparison.lessThan(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Byte> lessThan(final byte ¢) {
    return OrderingComparison.lessThan(Byte.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Character> lessThan(final char ¢) {
    return OrderingComparison.lessThan(Character.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Double> lessThan(final double ¢) {
    return OrderingComparison.lessThan(Double.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Float> lessThan(final float ¢) {
    return OrderingComparison.lessThan(Float.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Integer> lessThan(final int ¢) {
    return OrderingComparison.lessThan(Integer.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Long> lessThan(final long ¢) {
    return OrderingComparison.lessThan(Long.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Short> lessThan(final short ¢) {
    return OrderingComparison.lessThan(Short.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Boolean> lessThanOrEqualTo(final boolean ¢) {
    return OrderingComparison.lessThanOrEqualTo(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Byte> lessThanOrEqualTo(final byte ¢) {
    return OrderingComparison.lessThanOrEqualTo(Byte.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Character> lessThanOrEqualTo(final char ¢) {
    return OrderingComparison.lessThanOrEqualTo(Character.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Double> lessThanOrEqualTo(final double ¢) {
    return OrderingComparison.lessThanOrEqualTo(Double.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Float> lessThanOrEqualTo(final float ¢) {
    return OrderingComparison.lessThanOrEqualTo(Float.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Integer> lessThanOrEqualTo(final int ¢) {
    return OrderingComparison.lessThanOrEqualTo(Integer.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Long> lessThanOrEqualTo(final long ¢) {
    return OrderingComparison.lessThanOrEqualTo(Long.valueOf(¢));
  }

  @Factory public static Matcher<@Nullable Short> lessThanOrEqualTo(final short ¢) {
    return OrderingComparison.lessThanOrEqualTo(Short.valueOf(¢));
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
    for (final @Nullable Object ¢ : os)
      azzert.notNull(os + "", ¢);
  }

  public static Matcher<Boolean> not(final boolean ¢) {
    return cantBeNull(IsNot.not(new Boolean(¢)));
  }

  public static Matcher<Byte> not(final byte ¢) {
    return cantBeNull(IsNot.not(new Byte(¢)));
  }

  public static Matcher<Character> not(final char i) {
    return cantBeNull(IsNot.not(new Character(i)));
  }

  public static Matcher<Double> not(final double ¢) {
    return cantBeNull(IsNot.not(new Double(¢)));
  }

  public static Matcher<Float> not(final float ¢) {
    return cantBeNull(IsNot.not(new Float(¢)));
  }

  public static Matcher<Integer> not(final int ¢) {
    return cantBeNull(IsNot.not(new Integer(¢)));
  }

  public static Matcher<Long> not(final long i) {
    return cantBeNull(IsNot.not(new Long(i)));
  }

  public static <T> Matcher<T> not(final Matcher<T> ¢) {
    return IsNot.<T> not(¢);
  }

  public static Matcher<Short> not(final short ¢) {
    return cantBeNull(IsNot.not(new Short(¢)));
  }

  public static <T> Matcher<T> not(final T value) {
    return IsNot.<T> not(value);
  }

  public static void notNull(final @Nullable Object ¢) {
    that(¢, notNullValue());
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
    for (final @Nullable Object ¢ : os)
      notNull(os + "", ¢);
  }

  public static Matcher<@Nullable Object> nullValue() {
    return IsNull.nullValue();
  }

  public static <T> Matcher<T> nullValue(final java.lang.Class<T> type) {
    return IsNull.<T> nullValue(type);
  }

  public static void positive(final int ¢) {
    azzert.that(¢, greaterThan(0));
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

  public static <@Nullable T> void that(final String reason, final @Nullable T actual, final Matcher<? super @Nullable T> t) {
    assertThat(reason, actual, t);
  }

  public static <@Nullable T> void that(final @Nullable T actual, final Matcher<? super @Nullable T> t) {
    assertThat("", actual, t);
  }

  public static <T> Matcher<T> theInstance(final T target) {
    return IsSame.<T> theInstance(target);
  }

  /** Assert that an integer is zero
   * @param ¢ JD */
  public static void zero(final int ¢) {
    assertEquals(0, ¢);
  }

  /** Assert that long is zero
   * @param ¢ JD */
  public static void zero(final long ¢) {
    assertEquals(0L, ¢);
  }

  public static class ____META {
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

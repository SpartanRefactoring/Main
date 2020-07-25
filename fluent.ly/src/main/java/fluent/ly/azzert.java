package fluent.ly;

import static il.org.spartan.Utils.cantBeNull;
import static il.org.spartan.Utils.compressSpaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.DescribedAs;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsSame;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
import org.hamcrest.number.OrderingComparison;
import org.jetbrains.annotations.Contract;
import org.junit.Assert;

import il.org.spartan.Wrapper;
import let.it;

/**
 * Extends {@link org.junit.Assert} with more assertion for equality
 * comparisons. If the comparison yields a "not-equal" result, a JUnit assertion
 * failure is issued.
 *
 * @author Itay Maman Jul 9, 2007
 * @author Yossi Gil revised 2015-07-18
 */
public class azzert extends org.junit.Assert {
  public static <T> Matcher<T> allOf(final java.lang.Iterable<Matcher<? super T>> ¢) {
    return AllOf.allOf(¢);
  }

  @SafeVarargs public static <T> Matcher<T> allOf(final Matcher<? super T>... ¢) {
    return AllOf.allOf(¢);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second) {
    return AllOf.allOf(first, second);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second,
      final Matcher<? super T> third) {
    return AllOf.allOf(first, second, third);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return AllOf.allOf(first, second, third, fourth, fifth);
  }

  public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth,
      final Matcher<? super T> sixth) {
    return AllOf.allOf(first, second, third, fourth, fifth, sixth);
  }

  public static <T> Matcher<T> any(final java.lang.Class<T> type) {
    return IsInstanceOf.any(type);
  }

  public static <T> AnyOf<T> anyOf(final java.lang.Iterable<Matcher<? super T>> ¢) {
    return AnyOf.anyOf(¢);
  }

  @SafeVarargs public static <T> AnyOf<T> anyOf(final Matcher<? super T>... ¢) {
    return AnyOf.anyOf(¢);
  }

  public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second) {
    return AnyOf.anyOf(first, second);
  }

  public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third) {
    return AnyOf.anyOf(first, second, third);
  }

  public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth) {
    return AnyOf.anyOf(first, second, third, fourth);
  }

  public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return AnyOf.anyOf(first, second, third, fourth, fifth);
  }

  public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second,
      final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth,
      final Matcher<? super T> sixth) {
    return AnyOf.anyOf(first, second, third, fourth, fifth, sixth);
  }

  public static Matcher<Object> anything() {
    return IsAnything.anything();
  }

  public static Matcher<Object> anything(final String description) {
    return IsAnything.anything(description);
  }

  public static <T> void assertCollectionsEqual(final Collection<T> c1, final Collection<T> c2) {
    assertCollectionsEqual("", c1, c2);
  }

  public static <T> void collectionsEqual(final Collection<T> ts1, final T[] ts2) {
    assertCollectionsEqual("", ts1, Arrays.asList(ts2));
  }

  public static <T> void assertCollectionsEqual(final String s, final Collection<T> c1, final Collection<T> c2) {
    assertContained(s, c1, c2);
    assertContained(s, c2, c1);
  }

  public static <T> void assertCollectionsEqual(final String s, final Collection<T> ts1, final T[] ts2) {
    assertCollectionsEqual(s, ts1, Arrays.asList(ts2));
  }

  public static <T> void assertCollectionsEqual(final String s, final T[] ts1, final Collection<T> ts2) {
    assertCollectionsEqual(s, ts2, ts1);
  }

  public static <T> void assertContained(final String s, final Collection<T> c1, final Collection<T> c2) {
    // assertLE(s, c1.size(), c2.size());
    final ArrayList<T> missing = new ArrayList<>();
    for (final T ¢ : c1)
      if (!c2.contains(¢))
        missing.add(¢);
    switch (missing.size()) {
      case 0:
        return;
      case 1:
        fail(s + "Element '" + missing.get(0) + "' not found in " + c2.size() + " sized-\n collection " + c2);
        break;
      default:
        fail(s + "Element '" + missing.get(0) + "' and '" + missing.get(1) + "'  as well as " + (missing.size() - 2)
            + " other \n elements were not found in " + c2.size() + " sized-\n collection " + c2);
        break;
    }
  }

  public static <T> void assertContains(final Collection<T> ts, final T t) {
    assertContains("", ts, t);
  }

  public static <T> void assertContains(final String s, final Collection<T> ts, final T t) {
    assert ts.contains(t) : s + " t = " + t;
  }

  public static void assertEquals(final boolean a, final boolean b) {
    assertEquals(Boolean.valueOf(a), Boolean.valueOf(b));
  }

  public static void assertEquals(final boolean b1, final Boolean b2) {
    assertEquals(Boolean.valueOf(b1), b2);
  }

  public static void assertEquals(final Boolean b1, final boolean b2) {
    assertEquals(b1, Boolean.valueOf(b2));
  }

  public static void assertEquals(final int expected, final int actual) {
    assertEquals(fluent.ly.box.it(expected), fluent.ly.box.it(actual));
  }

  public static void assertEquals(final int a, final Integer b) {
    assertEquals(Integer.valueOf(a), b);
  }

  public static void assertEquals(final Integer a, final int b) {
    assertEquals(a, Integer.valueOf(b));
  }

  public static void assertEquals(final String message, final boolean b1, final boolean b2) {
    assertEquals(message, Boolean.valueOf(b1), Boolean.valueOf(b2));
  }

  public static void assertEquals(final String message, final boolean b1, final Boolean b2) {
    assertEquals(message, Boolean.valueOf(b1), b2);
  }

  public static void assertEquals(final String message, final Boolean b1, final boolean b2) {
    assertEquals(message, b1, Boolean.valueOf(b2));
  }

  public static void assertEquals(final String reason, final int i1, final int i2) {
    assertThat(reason, fluent.ly.box.it(i1), CoreMatchers.equalTo(fluent.ly.box.it(i2)));
  }

  public static void assertEquals(final String message, final int a, final Integer b) {
    assertEquals(message, Integer.valueOf(a), b);
  }

  public static void assertEquals(final String message, final Integer a, final int b) {
    assertEquals(message, a, Integer.valueOf(b));
  }

  public static void assertFalse(final boolean ¢) {
    that("", Boolean.valueOf(¢), is(Boolean.FALSE));
  }

  public static void assertFalse(final String s, final boolean b) {
    that(s, b, is(Boolean.FALSE));
  }

  public static void assertLE(final String s, final int i, final int m) {
    assert i <= m : s + " n=" + i + " m=" + m;
  }

  public static <T> void assertNotContains(final Collection<T> ts, final T t) {
    assertNotContains("", ts, t);
  }

  public static <T> void assertNotContains(final String s, final Collection<T> ts, final T t) {
    assert !ts.contains(t) : s + " t = " + t;
  }

  public static void assertNotEquals(final Object o1, final Object o2) {
    assertThat("", o1, CoreMatchers.not(o2));
  }

  public static void assertNotEquals(final String message, final Object o1, final Object o2) {
    assertThat(message, o1, CoreMatchers.not(o2));
  }

  public static void assertNotEquals(final String s1, final String s2) {
    assertNotEquals(null, s1, s2);
  }

  public static void assertNotEquals(final String message, final String s1, final String s2) {
    assert !s1.equals(s2) : message;
  }

  public static void assertNull(final Object ¢) {
    assert ¢ == null;
  }

  public static void assertNull(final String message, final Object o) {
    assertEquals(message, null, o);
  }

  public static void assertPositive(final int ¢) {
    assert ¢ > 0 : "Expecting a positive value, but got " + ¢;
  }

  public static <T> void assertSubset(final Collection<T> c1, final Collection<T> c2) {
    assertContained("", c1, c2);
  }

  public static void assertTrue(final boolean ¢) {
    that("", Boolean.valueOf(¢), is(Boolean.TRUE));
  }

  public static void assertTrue(final String s, final boolean b) {
    that(s, Boolean.valueOf(b), is(Boolean.TRUE));
  }

  public static void assertZero(final int ¢) {
    assertEquals("Expecting a zero", ¢, 0);
  }

  public static Asserter aye(final boolean claim) {
    return aye("", claim);
  }

  public static Asserter aye(final String reason, final boolean claim) {
    return new Asserter().andAye(reason, claim);
  }

  public static <LHS> CombinableMatcher.CombinableBothMatcher<LHS> both(final Matcher<? super LHS> ¢) {
    return CombinableMatcher.both(¢);
  }

  @Factory public static Matcher<Boolean> comparesEqualTo(final boolean ¢) {
    return OrderingComparison.comparesEqualTo(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<Byte> comparesEqualTo(final byte ¢) {
    return OrderingComparison.comparesEqualTo(Byte.valueOf(¢));
  }

  @Factory public static Matcher<Character> comparesEqualTo(final char ¢) {
    return OrderingComparison.comparesEqualTo(Character.valueOf(¢));
  }

  @Factory public static Matcher<Double> comparesEqualTo(final double ¢) {
    return OrderingComparison.comparesEqualTo(Double.valueOf(¢));
  }

  @Factory public static Matcher<Float> comparesEqualTo(final float ¢) {
    return OrderingComparison.comparesEqualTo(Float.valueOf(¢));
  }

  @Factory public static Matcher<Integer> comparesEqualTo(final int ¢) {
    return OrderingComparison.comparesEqualTo(Integer.valueOf(¢));
  }

  @Factory public static Matcher<Long> comparesEqualTo(final long ¢) {
    return OrderingComparison.comparesEqualTo(Long.valueOf(¢));
  }

  @Factory public static Matcher<Short> comparesEqualTo(final short ¢) {
    return OrderingComparison.comparesEqualTo(Short.valueOf(¢));
  }

  public static Matcher<String> containsString(final String substring) {
    return StringContains.containsString(substring);
  }

  public static <T> Matcher<T> describedAs(final String description, final Matcher<T> m, final Object... values) {
    return DescribedAs.describedAs(description, m, values);
  }

  public static <LHS> CombinableMatcher.CombinableEitherMatcher<LHS> either(final Matcher<? super LHS> ¢) {
    return CombinableMatcher.either(¢);
  }

  public static Matcher<String> endsWith(final String suffix) {
    return StringEndsWith.endsWith(suffix);
  }

  public static <T> void equals(final String prefix, final Collection<T> set, final Collection<T> ts) {
    Set<T> temp = new HashSet<>(set);
    temp.removeAll(ts);
    assert temp.isEmpty() : temp;
    temp = new HashSet<>();
    temp.addAll(ts);
    temp.removeAll(set);
    assert temp.isEmpty() : prefix + " - " + temp;
  }

  public static <T> Matcher<T> equalTo(final T operand) {
    return IsEqual.equalTo(operand);
  }

  public static Matcher<String> equalToIgnoringCase(final String expectedString) {
    return org.hamcrest.Matchers.equalToIgnoringCase(expectedString);
  }

  public static Matcher<String> equalToIgnoringWhiteSpace(final String expectedString) {
    return org.hamcrest.Matchers.equalToIgnoringWhiteSpace(expectedString);
  }

  public static <U> Matcher<java.lang.Iterable<U>> everyItem(final Matcher<U> itemMatcher) {
    return Every.everyItem(itemMatcher);
  }

  public static void fail() {
    Assert.fail();
  }

  public static void fail(final String ¢) {
    Assert.fail(¢);
  }

  public static void falze(final boolean ¢) {
    assert !¢;
  }

  @Factory public static Matcher<Boolean> greaterThan(final boolean ¢) {
    return OrderingComparison.greaterThan(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<Byte> greaterThan(final byte ¢) {
    return OrderingComparison.greaterThan(Byte.valueOf(¢));
  }

  @Factory public static Matcher<Character> greaterThan(final char ¢) {
    return OrderingComparison.greaterThan(Character.valueOf(¢));
  }

  @Factory public static Matcher<Double> greaterThan(final double ¢) {
    return OrderingComparison.greaterThan(Double.valueOf(¢));
  }

  @Factory public static Matcher<Float> greaterThan(final float ¢) {
    return OrderingComparison.greaterThan(Float.valueOf(¢));
  }

  @Factory public static Matcher<Integer> greaterThan(final int ¢) {
    return OrderingComparison.greaterThan(Integer.valueOf(¢));
  }

  @Factory public static Matcher<Long> greaterThan(final long ¢) {
    return OrderingComparison.greaterThan(Long.valueOf(¢));
  }

  @Factory public static Matcher<Short> greaterThan(final short ¢) {
    return OrderingComparison.greaterThan(Short.valueOf(¢));
  }

  @Factory public static Matcher<Boolean> greaterThanOrEqualTo(final boolean ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<Byte> greaterThanOrEqualTo(final byte ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Byte.valueOf(¢));
  }

  @Factory public static Matcher<Character> greaterThanOrEqualTo(final char ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Character.valueOf(¢));
  }

  @Factory public static Matcher<Double> greaterThanOrEqualTo(final double ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Double.valueOf(¢));
  }

  @Factory public static Matcher<Float> greaterThanOrEqualTo(final float ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Float.valueOf(¢));
  }

  @Factory public static Matcher<Integer> greaterThanOrEqualTo(final int ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(¢));
  }

  @Factory public static Matcher<Long> greaterThanOrEqualTo(final long ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Long.valueOf(¢));
  }

  @Factory public static Matcher<Short> greaterThanOrEqualTo(final short ¢) {
    return OrderingComparison.greaterThanOrEqualTo(Short.valueOf(¢));
  }

  public static <T> Matcher<java.lang.Iterable<? super T>> hasItem(final Matcher<? super T> itemMatcher) {
    return IsCollectionContaining.hasItem(itemMatcher);
  }

  public static <T> Matcher<java.lang.Iterable<? super T>> hasItem(final T item) {
    return IsCollectionContaining.hasItem(item);
  }

  @SafeVarargs public static <T> Matcher<java.lang.Iterable<T>> hasItems(final Matcher<? super T>... itemMatchers) {
    return IsCollectionContaining.hasItems(itemMatchers);
  }

  @SafeVarargs public static <T> Matcher<java.lang.Iterable<T>> hasItems(final T... items) {
    return IsCollectionContaining.hasItems(items);
  }

  public static <T> Matcher<T> instanceOf(final java.lang.Class<?> type) {
    return IsInstanceOf.instanceOf(type);
  }

  public static Matcher<Boolean> is(final boolean ¢) {
    return is(Boolean.valueOf(¢));
  }

  public static Matcher<Byte> is(final byte ¢) {
    return is(Byte.valueOf(¢));
  }

  public static Matcher<Character> is(final char ¢) {
    return is(Character.valueOf(¢));
  }

  public static Matcher<Double> is(final double ¢) {
    return is(Double.valueOf(¢));
  }

  public static Matcher<Float> is(final float ¢) {
    return is(Float.valueOf(¢));
  }

  public static Matcher<Integer> is(final int ¢) {
    return is(Integer.valueOf(¢));
  }

  public static Matcher<Long> is(final long ¢) {
    return is(Long.valueOf(¢));
  }

  @Contract(pure = true) public static <T> Matcher<T> is(final Matcher<T> ¢) {
    return Is.is(¢);
  }

  public static Matcher<Short> is(final short ¢) {
    return is(Short.valueOf(¢));
  }

  @Contract(pure = true) public static <T> Matcher<T> is(final T value) {
    return Is.is(value);
  }

  public static <T> Matcher<T> isA(final java.lang.Class<T> type) {
    return Is.isA(type);
  }

  public static void isNull(final Object ¢) {
    assertThat("", ¢, nullValue());
  }

  /**
   * @param message what to print
   * @param o       what to examine
   */
  @Contract("_, !null -> fail") public static void isNull(final String message, final Object o) {
    azzert.assertNull(message, o);
  }

  public static Wrapper<String> iz(final String ¢) {
    return new Wrapper<>(¢);
  }

  @Factory public static Matcher<Boolean> lessThan(final boolean ¢) {
    return OrderingComparison.lessThan(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<Byte> lessThan(final byte ¢) {
    return OrderingComparison.lessThan(Byte.valueOf(¢));
  }

  @Factory public static Matcher<Character> lessThan(final char ¢) {
    return OrderingComparison.lessThan(Character.valueOf(¢));
  }

  @Factory public static Matcher<Double> lessThan(final double ¢) {
    return OrderingComparison.lessThan(Double.valueOf(¢));
  }

  @Factory public static Matcher<Float> lessThan(final float ¢) {
    return OrderingComparison.lessThan(Float.valueOf(¢));
  }

  @Factory public static Matcher<Integer> lessThan(final int ¢) {
    return OrderingComparison.lessThan(Integer.valueOf(¢));
  }

  @Factory public static Matcher<Long> lessThan(final long ¢) {
    return OrderingComparison.lessThan(Long.valueOf(¢));
  }

  @Factory public static Matcher<Short> lessThan(final short ¢) {
    return OrderingComparison.lessThan(Short.valueOf(¢));
  }

  @Factory public static Matcher<Boolean> lessThanOrEqualTo(final boolean ¢) {
    return OrderingComparison.lessThanOrEqualTo(Boolean.valueOf(¢));
  }

  @Factory public static Matcher<Byte> lessThanOrEqualTo(final byte ¢) {
    return OrderingComparison.lessThanOrEqualTo(Byte.valueOf(¢));
  }

  @Factory public static Matcher<Character> lessThanOrEqualTo(final char ¢) {
    return OrderingComparison.lessThanOrEqualTo(Character.valueOf(¢));
  }

  @Factory public static Matcher<Double> lessThanOrEqualTo(final double ¢) {
    return OrderingComparison.lessThanOrEqualTo(Double.valueOf(¢));
  }

  @Factory public static Matcher<Float> lessThanOrEqualTo(final float ¢) {
    return OrderingComparison.lessThanOrEqualTo(Float.valueOf(¢));
  }

  @Factory public static Matcher<Integer> lessThanOrEqualTo(final int ¢) {
    return OrderingComparison.lessThanOrEqualTo(Integer.valueOf(¢));
  }

  @Factory public static Matcher<Long> lessThanOrEqualTo(final long ¢) {
    return OrderingComparison.lessThanOrEqualTo(Long.valueOf(¢));
  }

  @Factory public static Matcher<Short> lessThanOrEqualTo(final short ¢) {
    return OrderingComparison.lessThanOrEqualTo(Short.valueOf(¢));
  }

  public static Asserter nay(final boolean claim) {
    return nay("", claim);
  }

  public static Asserter nay(final String reason, final boolean claim) {
    return new Asserter().andNay(reason, claim);
  }

  public static void NonNulls(final Iterable<Object> os) {
    assert os != null;
    assert os != null;
    for (final Object ¢ : os)
      assert ¢ != null : os + "";
  }

  public static Matcher<Boolean> not(final boolean ¢) {
    return cantBeNull(IsNot.not(Boolean.valueOf(¢)));
  }

  public static Matcher<Byte> not(final byte ¢) {
    return cantBeNull(IsNot.not(Byte.valueOf(¢)));
  }

  public static Matcher<Character> not(final char i) {
    return cantBeNull(IsNot.not(Character.valueOf(i)));
  }

  public static Matcher<Double> not(final double ¢) {
    return cantBeNull(IsNot.not(Double.valueOf(¢)));
  }

  public static Matcher<Float> not(final float ¢) {
    return cantBeNull(IsNot.not(Float.valueOf(¢)));
  }

  public static Matcher<Integer> not(final int ¢) {
    return cantBeNull(IsNot.not(Integer.valueOf(¢)));
  }

  public static Matcher<Long> not(final long i) {
    return cantBeNull(IsNot.not(Long.valueOf(i)));
  }

  @Contract(pure = true) public static <T> Matcher<T> not(final Matcher<T> ¢) {
    return IsNot.not(¢);
  }

  public static Matcher<Short> not(final short ¢) {
    return cantBeNull(IsNot.not(Short.valueOf(¢)));
  }

  @Contract(pure = true) public static <T> Matcher<T> not(final T value) {
    return IsNot.not(value);
  }

  public static void NonNull(final Object ¢) {
    assertThat("", ¢, NonNullValue());
  }

  public static void NonNull(final String s, final Object o) {
    assertThat(s, o, NonNullValue());
  }

  @Contract(pure = true) public static Matcher<Object> NonNullValue() {
    return IsNull.notNullValue();
  }

  @Contract(pure = true) public static <T> Matcher<T> NonNullValue(final java.lang.Class<T> type) {
    return IsNull.notNullValue(type);
  }

  public static void NonNullz(final Object... os) {
    assert os != null;
    assert os != null;
    for (final Object ¢ : os)
      assert ¢ != null : os + "";
  }

  @Contract(pure = true) public static Matcher<Object> nullValue() {
    return IsNull.nullValue();
  }

  @Contract(pure = true) public static <T> Matcher<T> nullValue(final java.lang.Class<T> type) {
    return IsNull.nullValue(type);
  }

  public static void positive(final int ¢) {
    azzert.that(¢, greaterThan(0));
  }

  @Contract(pure = true) public static <T> Matcher<T> sameInstance(final T target) {
    return IsSame.sameInstance(target);
  }

  public static Matcher<String> startsWith(final String prefix) {
    return StringStartsWith.startsWith(prefix);
  }

  public static void that(final boolean b, final Matcher<? super Boolean> m) {
    assertThat("", Boolean.valueOf(b), m);
  }

  public static void that(final byte b, final Matcher<? super Byte> m) {
    assertThat("", Byte.valueOf(b), m);
  }

  public static void that(final char c, final Matcher<? super Character> m) {
    assertThat("", Character.valueOf(c), m);
  }

  public static void that(final double d, final Matcher<? super Double> m) {
    assertThat("", Double.valueOf(d), m);
  }

  public static void that(final float f, final Matcher<? super Float> m) {
    assertThat("", Float.valueOf(f), m);
  }

  public static void that(final int i, final Matcher<? super Integer> m) {
    assertThat("", Integer.valueOf(i), m);
  }

  public static void that(final long l, final Matcher<? super Long> m) {
    assertThat("", Long.valueOf(l), m);
  }

  public static void that(final Object actual, final Wrapper<String> expected) {
    assertThat("", compressSpaces(actual + ""), is(compressSpaces(expected.get())));
  }

  public static void that(final short s, final Matcher<? super Short> m) {
    assertThat("", Short.valueOf(s), m);
  }

  public static void that(final String reason, final boolean b, final Matcher<? super Boolean> m) {
    assertThat(reason, Boolean.valueOf(b), m);
  }

  public static void that(final String reason, final byte b, final Matcher<? super Byte> m) {
    assertThat(reason, Byte.valueOf(b), m);
  }

  public static void that(final String reason, final char c, final Matcher<? super Character> m) {
    assertThat(reason, Character.valueOf(c), m);
  }

  public static void that(final String reason, final double d, final Matcher<? super Double> m) {
    assertThat(reason, Double.valueOf(d), m);
  }

  public static void that(final String reason, final float f, final Matcher<? super Float> m) {
    assertThat(reason, Float.valueOf(f), m);
  }

  public static void that(final String reason, final int i, final Matcher<? super Integer> m) {
    assertThat(reason, Integer.valueOf(i), m);
  }

  public static void that(final String reason, final long l, final Matcher<? super Long> m) {
    assertThat(reason, Long.valueOf(l), m);
  }

  public static void that(final String reason, final short s, final Matcher<? super Short> m) {
    assertThat(reason, Short.valueOf(s), m);
  }

  public static <T> void that(final String reason, final T actual, final Matcher<? super T> t) {
    assertThat(reason, actual, t);
  }

  public static <T> void that(final T actual, final Matcher<? super T> t) {
    assertThat("", actual, t);
  }

  @Contract(pure = true) public static <T> Matcher<T> theInstance(final T target) {
    return IsSame.theInstance(target);
  }

  public static void xassertEquals(final int a, final int b) {
    assertEquals("", a, b);
  }

  public static void xassertEquals(final String s, final int a, final int b) {
    assertEquals(s, Integer.valueOf(a), Integer.valueOf(b));
  }

  /**
   * Assert that an integer is zero
   *
   * @param ¢ JD
   */
  public static void zero(final int ¢) {
    assertEquals(0, ¢);
  }

  /**
   * Assert that long is zero
   *
   * @param ¢ JD
   */
  public static void zero(final long ¢) {
    assertEquals(0L, ¢);
  }

  public static class ____META {
    //
  }

  public static class Asserter {
    public Asserter andAye(final boolean claim) {
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

  public interface Subject<T> {
    default Subject<T> is(final T object) {
      assert it().equals(object);
      return this;
    }

    T it();
  }

  public static <T> Subject<T> that(final T subject) {
    return () -> subject;
  }

  public static <T> Subject<T> that(final it<T> subject) {
    return () -> subject.it;
  }
}

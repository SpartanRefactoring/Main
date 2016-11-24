package il.org.spartanl.testing;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spatan.iteration.*;

/** Extends {@link org.junit.Assert} with more assertion for equality
 * comparisons. If the comparison yields a "not-equal" result, a JUnit assertion
 * failure is issued.
 * @author Itay Maman Jul 9, 2007 */
public class Assert extends org.junit.Assert {
  public static <T> void assertCollectionsEqual(@NotNull final Collection<T> c1, @NotNull final Collection<T> c2) {
    assertCollectionsEqual("", c1, c2);
  }

  public static <T> void assertCollectionsEqual(@NotNull final Collection<T> ts1, final T[] ts2) {
    assertCollectionsEqual("", ts1, Arrays.asList(ts2));
  }

  public static <T> void assertCollectionsEqual(final String s, @NotNull final Collection<T> c1, @NotNull final Collection<T> c2) {
    assertContained(s, c1, c2);
    assertContained(s, c2, c1);
  }

  public static <T> void assertCollectionsEqual(final String s, @NotNull final Collection<T> ts1, final T[] ts2) {
    assertCollectionsEqual(s, ts1, Arrays.asList(ts2));
  }

  public static <T> void assertCollectionsEqual(final String s, final T[] ts1, @NotNull final Collection<T> ts2) {
    assertCollectionsEqual(s, ts2, ts1);
  }

  public static <T> void assertContained(final String s, @NotNull final Collection<T> c1, @NotNull final Collection<T> c2) {
    // assertLE(s, c1.size(), c2.size());
    @NotNull final ArrayList<T> missing = new ArrayList<>();
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
            + " other \n elements were not found in " + c2.size() + " sized-\n" + " collection " + c2);
        break;
    }
  }

  public static <T> void assertContains(@NotNull final Collection<T> ts, final T t) {
    assertContains("", ts, t);
  }

  public static <T> void assertContains(final String s, @NotNull final Collection<T> ts, final T t) {
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

  public static void assertEquals(final String message, final int a, final Integer b) {
    assertEquals(message, Integer.valueOf(a), b);
  }

  public static void assertEquals(final String message, final Integer a, final int b) {
    assertEquals(message, a, Integer.valueOf(b));
  }

  public static void assertLE(final String s, final int i, final int m) {
    assert i <= m : s + " n=" + i + " m=" + m;
  }

  public static <T> void assertNotContains(@NotNull final Collection<T> ts, final T t) {
    assertNotContains("", ts, t);
  }

  public static <T> void assertNotContains(final String s, @NotNull final Collection<T> ts, final T t) {
    assert !ts.contains(t) : s + " t = " + t;
  }

  public static void assertNotEquals(@NotNull final Object o1, final Object o2) {
    assertNotEquals(null, o1, o2);
  }

  public static void assertNotEquals(final String message, @NotNull final Object o1, final Object o2) {
    assert !o1.equals(o2);
  }

  public static void assertNotEquals(@NotNull final String s1, final String s2) {
    assertNotEquals(null, s1, s2);
  }

  public static void assertNotEquals(final String message, @NotNull final String s1, final String s2) {
    assert !s1.equals(s2) : message;
  }

  public static void assertNull(@Nullable final Object ¢) {
    assert ¢ == null;
  }

  public static void assertNull(final String message, final Object o) {
    assertEquals(message, null, o);
  }

  public static void assertPositive(final int ¢) {
    assert ¢ > 0 : "Expecting a positive value, but got " + ¢;
  }

  public static <T> void assertSubset(@NotNull final Collection<T> c1, @NotNull final Collection<T> c2) {
    assertContained("", c1, c2);
  }

  public static void assertZero(final int ¢) {
    assertEquals("Expecting a zero", ¢, 0);
  }

  public static <T> void equals(final String prefix, @NotNull final Set<T> set, @NotNull final Iterable<T> ts) {
    @NotNull final List<T> list = Iterables.toList(ts);
    @NotNull Set<T> temp = new HashSet<>();
    temp.addAll(set);
    temp.removeAll(list);
    assert temp.isEmpty() : temp;
    temp = new HashSet<>();
    temp.addAll(list);
    temp.removeAll(set);
    assert temp.isEmpty() : prefix + " - " + temp;
  }

  public static void xassertEquals(final int a, final int b) {
    assertEquals("", a, b);
  }

  public static void xassertEquals(final String s, final int a, final int b) {
    assertEquals(s, Integer.valueOf(a), Integer.valueOf(b));
  }
}

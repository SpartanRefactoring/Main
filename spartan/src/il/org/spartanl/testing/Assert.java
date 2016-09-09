package il.org.spartanl.testing;

import java.util.*;

import il.org.spatan.iteration.*;

/** Extends {@link org.junit.Assert} with more assertion for equality
 * comparisons. If the comparison yields a "not-equal" result, a JUnit assertion
 * failure is issued.
 * @author Itay Maman Jul 9, 2007 */
public class Assert extends org.junit.Assert {
  public static <T> void assertCollectionsEqual(final Collection<T> c1, final Collection<T> c2) {
    assertCollectionsEqual("", c1, c2);
  }

  public static <T> void assertCollectionsEqual(final Collection<T> ts1, final T[] ts2) {
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
    for (final T t : c1)
      if (!c2.contains(t))
        missing.add(t);
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

  public static <T> void assertContains(final Collection<T> c, final T t) {
    assertContains("", c, t);
  }

  public static <T> void assertContains(final String s, final Collection<T> ts, final T t) {
    assertTrue(s + " t = " + t, ts.contains(t));
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

  public static void assertLE(final String s, final int n, final int m) {
    assertTrue(s + " n=" + n + " m=" + m, n <= m);
  }

  public static <T> void assertNotContains(final Collection<T> ts, final T t) {
    assertNotContains("", ts, t);
  }

  public static <T> void assertNotContains(final String s, final Collection<T> c, final T t) {
    assertFalse(s + " t = " + t, c.contains(t));
  }

  public static void assertNotEquals(final Object o1, final Object o2) {
    assertNotEquals(null, o1, o2);
  }

  public static void assertNotEquals(final String message, final Object o1, final Object o2) {
    assertFalse(message, o1.equals(o2));
  }

  public static void assertNotEquals(final String s1, final String s2) {
    assertNotEquals(null, s1, s2);
  }

  public static void assertNotEquals(final String message, final String s1, final String s2) {
    assertFalse(message, s1.equals(s2));
  }

  public static void assertNull(final Object o) {
    assertEquals(null, o);
  }

  public static void assertNull(final String message, final Object o) {
    assertEquals(message, null, o);
  }

  public static void assertPositive(final int n) {
    assertTrue("Expecting a positive value, but got " + n, n > 0);
  }

  public static <T> void assertSubset(final Collection<T> c1, final Collection<T> c2) {
    assertContained("", c1, c2);
  }

  public static void assertZero(final int n) {
    assertEquals("Expecting a zero", n, 0);
  }

  public static <T> void equals(final String prefix, final Set<T> set, final Iterable<T> ts) {
    final List<T> list = Iterables.toList(ts);
    Set<T> temp = new HashSet<>();
    temp.addAll(set);
    temp.removeAll(list);
    assertTrue(temp.toString(), temp.isEmpty());
    temp = new HashSet<>();
    temp.addAll(list);
    temp.removeAll(set);
    assertTrue(prefix + " - " + temp.toString(), temp.isEmpty());
  }

  public static void xassertEquals(final int a, final int b) {
    assertEquals("", a, b);
  }

  public static void xassertEquals(final String s, final int a, final int b) {
    assertEquals(s, Integer.valueOf(a), Integer.valueOf(b));
  }
}

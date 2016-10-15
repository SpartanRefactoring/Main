/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan;

import static il.org.spartan.azzert.*;
/**
 * Fluent API 
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-10-15
 */
public interface AssertToAzzert {
  static <T> void assertEquals(final String reason, final T t1, final T t2) {
    azzert.that(reason, t2, is(t1));
  }

  static <T> void assertEquals(final T t1, final T t2) {
    azzert.that(t2, is(t1));
  }

  static void assertFalse(final boolean b) {
    assert !(b);
  }

  static void assertFalse(final Object reason, final boolean b) {
    assert !(b) : reason;
  }

  static <T> void assertNotEquals(final T t1, final T t2) {
    azzert.that(t2, is(not(t1)));
  }

  static <T> void assertNotNull(final T t) {
    assert t != null;
  }

  static <T> void assertNotNull(Object reason, final T t) {
    assert t != null : reason;
  }
  static <T> void assertNull(final T t) {
    azzert.isNull(t);
  }

  static void assertTrue(final boolean b) {
    assert b;
  }

  static void assertTrue(final Object reason, final boolean b) {
    assert b : reason;
  }

  static void assertZero(final int i) {
    azzert.zero(i);
  }
}
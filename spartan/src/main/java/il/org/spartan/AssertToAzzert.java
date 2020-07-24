/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan;

import static fluent.ly.azzert.is;
import static fluent.ly.azzert.not;

import fluent.ly.azzert;

/** Fluent API
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-10-15 */
//
public interface AssertToAzzert {
  static <T> void assertEquals(final String reason, final T t1, final T t2) {
    azzert.that(reason, t2, is(t1));
  }
  // public static void assertEquals(final Object exp, final
  // Object val) {
  // azzert.that(val, is(exp));
  // }
  // static <T> void assertEquals(final T t1, final T t2) {
  // azzert.that(t2, is(t1));
  // }
  static void assertFalse(final boolean ¢) {
    assert !¢;
  }
  static void assertFalse(final Object reason, final boolean b) {
    assert !b : reason;
  }
  static <T> void assertNotEquals(final T t1, final T t2) {
    azzert.that(t2, is(not(t1)));
  }
  static <T> void assertNonNull(final Object reason, final T t) {
    assert t != null : reason;
  }
  static <T> void assertNonNull(final T ¢) {
    assert ¢ != null;
  }
  static <T> void assertNull(final T ¢) {
    azzert.isNull(¢);
  }
  static void assertTrue(final boolean ¢) {
    assert ¢;
  }
  static void assertTrue(final Object reason, final boolean b) {
    assert b : reason;
  }
  static void assertZero(final int ¢) {
    azzert.zero(¢);
  }
}
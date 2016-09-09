/**
 *
 */
package il.org.spartan.utils;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Yossi Gil
 * @since Mar 1, 2012 */
public class Primes {
  private static boolean isPrime(final int c) {
    for (int d = 2; d * d <= c; d++)
      if (c % d == 0)
        return false;
    return true;
  }

  private int current = 1;

  public int next() {
    for (;;)
      if (isPrime(++current))
        return current;
  }

  @SuppressWarnings("static-method") //
  public static class TEST {
    @Test public void firstIsTwo() {
      assertEquals(2, new Primes().next());
    }

    @Test public void secondIsThree() {
      final Primes p = new Primes();
      p.next();
      assertEquals(3, p.next());
    }

    @Test public void thirdIsFive() {
      final Primes p = new Primes();
      p.next();
      p.next();
      assertEquals(5, p.next());
    }
  }
}

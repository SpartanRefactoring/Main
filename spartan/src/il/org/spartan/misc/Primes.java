/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.misc;

import static org.junit.Assert.*;
import il.org.spartan.*;

import org.junit.*;

/**
 * Primality testing and generation of primes.
 *
 * @author Yossi Gil
 * @since 2012-05-01
 */
public class Primes {
  /**
   * Tests for primality.
   *
   * @param c candidate to experience primality testing
   * @return <code><b>true</b></code> <i>iff</i> the parameter is prime.
   */
  public static boolean isPrime(final int c) {
    return //
        c < 0 ? isPrime(-c) // deal with negative values
            : c > 1 && isPrimeCore(c); // any integer >- 2
  }
  private static boolean isPrimeCore(final int c) {
    for (int d = 2; d * d <= c; ++d)
      if (c % d == 0)
        return false;
    return true;
  }
  /**
   * A generator for the sequence of primes: 2, 3, 5, 7, 11, 13, 17, 19, 23,...
   *
   * @return the next value in the sequence of primes; the first value returned
   *         is 2.
   */
  public int next() {
    for (;;)
      if (isPrimeCore(++current))
        return current;
  }

  private int current = 1;

  @SuppressWarnings({ "static-method", "javadoc" })//
  public static class TEST {
    @Test public void firstIsTwo() {
      assertEquals(2, new Primes().next());
    }
    @Test public void isPrimeOf_1() {
      assertFalse(isPrime(-1));
    }
    @Test public void isPrimeOf_2() {
      azzert.aye(isPrime(-2));
    }
    @Test public void isPrimeOf_3() {
      azzert.aye(isPrime(-3));
    }
    @Test public void isPrimeOf_4() {
      assertFalse(isPrime(-4));
    }
    @Test public void isPrimeOf0() {
      assertFalse(isPrime(0));
    }
    @Test public void isPrimeOf1() {
      assertFalse(isPrime(1));
    }
    @Test public void secondIsThree() {
      final Primes p = new Primes();
      p.next();
      assertEquals(3, p.next());
    }
    @Test public void selfConsistentUntil1000() {
      final Primes ps = new Primes();
      for (int c = 0; c < 1000; ++c)
        if (isPrime(c))
          assertEquals(c, ps.next());
    }
    @Test public void thirdIsFive() {
      final Primes p = new Primes();
      p.next();
      p.next();
      assertEquals(5, p.next());
    }
  }
}

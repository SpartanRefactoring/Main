/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.misc;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

/** Primality testing and generation of primes.
 * @author Yossi Gil
 * @since 2012-05-01 */
public class Primes {
  /** Tests for primality.
   * @param ¢ candidate to be tested
   * @return <code><b>true</b></code> <i>iff</i> the parameter is prime. */
  public static boolean isPrime(final int ¢) {
    return ¢ < -1 && isPrime(-¢) // deal with negative values
        || ¢ > 1 && isPrime¢(¢); // any integer >- 2
  }

  private static boolean isPrime¢(final int ¢) {
    for (int d = 2; d * d <= ¢; ++d)
      if (¢ % d == 0)
        return false;
    return true;
  }

  private int current = 1;

  /** A generator for the sequence of primes: 2, 3, 5, 7, 11, 13, 17, 19, 23,...
   * @return next value in the sequence of primes; the first value returned is
   *         2. */
  public int next() {
    for (;;)
      if (isPrime¢(++current))
        return current;
  }

  @SuppressWarnings({ "static-method", "javadoc" }) //
  public static class TEST {
    @Test public void firstIsTwo() {
      azzert.that(new Primes().next(), is(2));
    }

    @Test public void isPrimeOf__1() {
      azzert.nay(isPrime(-1));
    }

    @Test public void isPrimeOf__2() {
      azzert.aye(isPrime(-2));
    }

    @Test public void isPrimeOf__3() {
      assert isPrime(-3);
    }

    @Test public void isPrimeOf__4() {
      assert !isPrime(-4);
    }

    @Test public void isPrimeOf0() {
      assert !isPrime(0);
    }

    @Test public void isPrimeOf1() {
      assert !isPrime(1);
    }

    @Test public void secondIsThree() {
      final Primes p = new Primes();
      p.next();
      azzert.that(p.next(), is(3));
    }

    @Test public void selfConsistentUntil1000() {
      final Primes ps = new Primes();
      for (int c = 0; c < 1000; ++c)
        if (isPrime(c))
          azzert.that(ps.next(), is(c));
    }

    @Test public void thirdIsFive() {
      final Primes p = new Primes();
      p.next();
      p.next();
      azzert.that(p.next(), is(5));
    }
  }
}

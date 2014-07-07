/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan.misc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Primality testing and generation of primes.
 * 
 * @author Yossi Gil
 * @since Mar 1, 2012
 */
public class Primes {
	private int current = 1;
	/**
	 * @returns the next value in the sequence of primes:
	 *          2,3,5,7,11,13,17,19,23,...
	 */
	public int next() {
		for (;;)
			if (isPrime(++current))
				return current;
	}
	/**
	 * Tests for primality.
	 * 
	 * @param c
	 *          candidate to experience primality testing
	 * @return <code><b>true</b></code> <i>iff</i> the parameter is prime.
	 */
	public static boolean isPrime(final int c) {
		for (int d = 2; d * d <= c; d++)
			if (c % d == 0)
				return false;
		return true;
	}
	@SuppressWarnings("static-method")//
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

package il.org.spartan.sequence;

import static fluent.ly.azzert.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class FibonacciTest {
  @Test public void testCreate() {
    assert new Fibonacci().more();
  }

  @Test public void testCreateThreshold() {
    assert new Fibonacci(1000).more();
  }

  @Test public void testFifth() {
    azzert.that(new Fibonacci(1000).advance().advance().advance().advance().current(), is(8));
  }

  @Test public void testFirst() {
    azzert.that(new Fibonacci(1000).current(), is(1));
  }

  @Test public void testFourth() {
    azzert.that(new Fibonacci(1000).advance().advance().advance().current(), is(5));
  }

  @Test public void testSecond() {
    azzert.that(new Fibonacci(1000).advance().current(), is(2));
  }

  @Test public void testThird() {
    azzert.that(new Fibonacci(1000).advance().advance().current(), is(3));
  }
}

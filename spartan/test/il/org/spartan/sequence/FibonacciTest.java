package il.org.spartan.sequence;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method") public class FibonacciTest {
  @Test public void testCreate() {
    assert new Fibonacci().more();
  }

  @Test public void testCreateThreshold() {
    assert new Fibonacci(1000).more();
  }

  @Test public void testFifth() {
    assertEquals(8, new Fibonacci(1000).advance().advance().advance().advance().current());
  }

  @Test public void testFirst() {
    assertEquals(1, new Fibonacci(1000).current());
  }

  @Test public void testFourth() {
    assertEquals(5, new Fibonacci(1000).advance().advance().advance().current());
  }

  @Test public void testSecond() {
    assertEquals(2, new Fibonacci(1000).advance().current());
  }

  @Test public void testThird() {
    assertEquals(3, new Fibonacci(1000).advance().advance().current());
  }
}

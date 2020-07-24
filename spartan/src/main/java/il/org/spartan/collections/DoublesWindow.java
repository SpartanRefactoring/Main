package il.org.spartan.collections;

import static fluent.ly.azzert.is;
import static org.junit.Assert.assertEquals;

import java.util.EmptyStackException;

import org.junit.Test;

import fluent.ly.azzert;

/** Manage a window of doubles of a certain size, supporting
 * {@link #add(double)} operations in such a fashion that the oldest value
 * exceeding capacity is discarded
 * @author Yossi Gil
 * @since Apr 27, 2012 */
public class DoublesWindow {
  private final double[] window;
  private int newest;
  private int size;

  /** Instantiate {@link DoublesWindow}, creating a window of a given capacity.
   * @param capacity a non-negative integer
   * @throws IllegalArgumentException in case the argument was non-positive */
  public DoublesWindow(final int capacity) {
    if (capacity < 1)
      throw new IllegalArgumentException(capacity + "");
    window = new double[capacity];
  }
  /** add a value into the window, removing the oldest one if necessary
   * @param ¢ an arbitrary value
   * @return <code><strong>this</strong></code> */
  public DoublesWindow add(final double ¢) {
    if (size < window.length)
      ++size;
    window[newest = (newest + 1) % window.length] = ¢;
    return this;
  }
  /** @return the capacity of queue, */
  public int capacity() {
    return window.length;
  }
  public boolean full() {
    return size() == capacity();
  }
  /** Retrieve the newest value stored in this instance
   * @return the last value added into this instance
   * @throws EmptyStackException in case no values were previously added */
  public double newest() {
    if (size == 0)
      throw new EmptyStackException();
    return window[newest];
  }
  /** Retrieve the oldest value stored in this instance
   * @return the oldest value in this instance
   * @throws EmptyStackException in case no values were previously added */
  public double oldest() {
    if (size == 0)
      throw new EmptyStackException();
    return window[(newest + window.length - size + 1) % window.length];
  }
  /** Determines many values are stored in this instance.
   * @return a non-negative integer, which is the count of values stored in this
   *         instance */
  public int size() {
    return size;
  }

  @SuppressWarnings("static-method") //
  public static class TEST {
    @Test public void addCapacityDoesNotAbort() {
      new DoublesWindow(3).add(13.2).add(14.2).add(-1).add(-4);
    }
    @Test public void correctCapacity() {
      azzert.that(new DoublesWindow(10).capacity(), is(10));
    }
    @Test public void correctSizeAfterOverflow() {
      azzert.that(new DoublesWindow(3).add(1).add(3).add(4).add(5).size(), is(3));
    }
    @Test public void correctSizeBeforeOverflow() {
      azzert.that(new DoublesWindow(3).add(1).add(3).add(4).size(), is(3));
    }
    @Test public void correctSizeOfEmpty() {
      azzert.that(new DoublesWindow(10).size(), is(0));
    }
    @Test public void correctSizeOfOne() {
      azzert.that(new DoublesWindow(10).add(1).size(), is(1));
    }
    @Test public void correctSizeOfTwo() {
      azzert.that(new DoublesWindow(10).add(1).add(3).size(), is(2));
    }
    @Test public void create() {
      assert new DoublesWindow(10) != null;
    }
    @Test(expected = IllegalArgumentException.class) public void createMinusOneSize() {
      assert new DoublesWindow(-1) != null;
    }
    @Test(expected = IllegalArgumentException.class) public void createNegativeSize() {
      assert new DoublesWindow(-10) != null;
    }
    @Test(expected = IllegalArgumentException.class) public void createZeroSize() {
      assert new DoublesWindow(0) != null;
    }
    @Test public void hasAdd() {
      new DoublesWindow(10).add(13.2);
    }
    @Test public void hasNewest() {
      assertEquals(13.2, new DoublesWindow(10).add(13.2).newest(), 1E-5);
    }
    @Test(expected = EmptyStackException.class) public void newestOfEmpty() {
      assertEquals(13.2, new DoublesWindow(10).newest(), 1E-5);
    }
    @Test public void newestOfSequence() {
      assertEquals(14.2, new DoublesWindow(10).add(0).add(1).add(14.2).newest(), 1E-5);
    }
    @Test(expected = EmptyStackException.class) public void oldestOfEmpty() {
      assertEquals(13.2, new DoublesWindow(10).oldest(), 1E-5);
    }
    @Test public void oldestOfOverflowingSequence() {
      assertEquals(12.7, new DoublesWindow(2).add(-3.0).add(12.7).add(14.2).oldest(), 1E-5);
    }
    @Test public void oldestOfSequence() {
      assertEquals(-3.0, new DoublesWindow(10).add(-3.0).add(1).add(14.2).oldest(), 1E-5);
    }
    @Test public void simpleOldest() {
      assertEquals(13.2, new DoublesWindow(10).add(13.2).oldest(), 1E-5);
    }
    @Test public void veryLongWindow() {
      final DoublesWindow w = new DoublesWindow(5);
      for (int ¢ = 0; ¢ < 5; ++¢) {
        azzert.that(w.capacity(), is(5));
        azzert.that(w.size(), is(¢));
        assert !w.full();
        w.add(¢);
        azzert.that(w.size(), is(¢ + 1));
        assertEquals(¢, w.newest(), 1E-5);
        assertEquals(0, w.oldest(), 1E-5);
      }
      for (int ¢ = 5; ¢ < 100; ++¢) {
        assert w.full();
        azzert.that(w.capacity(), is(5));
        azzert.that(w.size(), is(5));
        w.add(¢);
        assertEquals(¢, w.newest(), 1E-10);
        assertEquals(¢ - 5 + 1, w.oldest(), 1E-10);
      }
    }
  }
}

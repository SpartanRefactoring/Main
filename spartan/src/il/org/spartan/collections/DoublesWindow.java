/**
 *
 */
package il.org.spartan.collections;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

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
      throw new IllegalArgumentException("" + capacity);
    window = new double[capacity];
  }

  /** add a value into the window, removing the oldest one if necessary
   * @param d an arbitrary value
   * @return <code><strong>this</strong></code> */
  public DoublesWindow add(final double d) {
    if (size < window.length)
      ++size;
    window[newest = (newest + 1) % window.length] = d;
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
      assertEquals(10, new DoublesWindow(10).capacity());
    }

    @Test public void correctSizeAfterOverflow() {
      assertEquals(3, new DoublesWindow(3).add(1).add(3).add(4).add(5).size());
    }

    @Test public void correctSizeBeforeOverflow() {
      assertEquals(3, new DoublesWindow(3).add(1).add(3).add(4).size());
    }

    @Test public void correctSizeOfEmpty() {
      assertEquals(0, new DoublesWindow(10).size());
    }

    @Test public void correctSizeOfOne() {
      assertEquals(1, new DoublesWindow(10).add(1).size());
    }

    @Test public void correctSizeOfTwo() {
      assertEquals(2, new DoublesWindow(10).add(1).add(3).size());
    }

    @Test public void create() {
      assert null != new DoublesWindow(10);
    }

    @Test(expected = IllegalArgumentException.class) public void createMinusOneSize() {
      assert null != new DoublesWindow(-1);
    }

    @Test(expected = IllegalArgumentException.class) public void createNegativeSize() {
      assert null != new DoublesWindow(-10);
    }

    @Test(expected = IllegalArgumentException.class) public void createZeroSize() {
      assert null != new DoublesWindow(0);
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
      for (int i = 0; i < 5; i++) {
        assertEquals(5, w.capacity());
        assertEquals(i, w.size());
        assert !w.full();
        w.add(i);
        assertEquals(i + 1, w.size());
        assertEquals(i, w.newest(), 1E-5);
        assertEquals(0, w.oldest(), 1E-5);
      }
      for (int i = 5; i < 100; i++) {
        assert w.full();
        assertEquals(5, w.capacity());
        assertEquals(5, w.size());
        w.add(i);
        assertEquals(i, w.newest(), 1E-10);
        assertEquals(i - 5 + 1, w.oldest(), 1E-10);
      }
    }
  }
}

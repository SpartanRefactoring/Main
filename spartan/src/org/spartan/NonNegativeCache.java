package org.spartan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import static org.spartan._.*;

/**
 * A class for lazy, memoizing evaluation of integers.
 * 
 * @author Yossi Gil
 * @since 2014-06-20
 */
public abstract class NonNegativeCache {
  /**
   * The cached value, negative when the cache was not populated
   */
  private int value = -1;

  /**
   * This function is to be implemented by clients, giving a method for
   * computing the cached value. It is guaranteed that this function will only
   * be called once.
   * 
   * @return the value to be cached
   */
  protected abstract int _();

  /**
   * Compute the cached value, either by looking up memoization, or by actual
   * computation
   * 
   * @return the cached value
   */
  public int value() {
    return value >= 0 ? value : (value = _());
  }

  public static class TEST extends NonNegativeCache {
    private static final int SOME_OFFSET = 17;
    private int evaluations = 0;

    @Override protected int _() {
      return SOME_OFFSET + sqr(evaluations++);
    }

    @Test public void firstReturnsFirstOffset() {
      assertEquals(SOME_OFFSET, value());
    }

    @Test public void restReturnsFirstOffset() {
      value();
      assertEquals(SOME_OFFSET, value());
      for (int i = 0; i < 10; i++)
        assertEquals(SOME_OFFSET, value());
    }
  }
}

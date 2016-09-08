/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;

import org.junit.*;

/** A class for lazy, memoizing evaluation of integers.
 * @author Yossi Gil
 * @since 2014-06-20 */
public abstract class NonNegativeCache {
  @SuppressWarnings({ "javadoc" }) //
  public static class TEST extends NonNegativeCache {
    private static final int SOME_OFFSET = 17;
    private int evaluations = 0;

    @Override protected int __() {
      return SOME_OFFSET + sqr(evaluations++);
    }

    @Test public void firstReturnsFirstOffset() {
      assertEquals(SOME_OFFSET, value());
    }

    @Test public void restReturnsFirstOffset() {
      value();
      assertEquals(SOME_OFFSET, value());
      for (int i = 0; i < 10; ++i)
        assertEquals(SOME_OFFSET, value());
    }
  }

  /** The cached value, negative when the cache was not populated */
  private int value = -1;

  /** This function is to be implemented by clients, giving a method for
   * computing the cached value. It is guaranteed that this function will only
   * be called once.
   * @return value to be cached */
  protected abstract int __();

  /** Compute the cached value, either by looking up memoization, or by actual
   * computation
   * @return cached value */
  public int value() {
    return value >= 0 ? value : (value = __());
  }
}

/**
 *
 */
package il.org.spartan.sequence;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
@SuppressWarnings("static-method") public class BooleanHistoryTest {
  @Test public void addSize() {
    final BooleanHistory h = new BooleanHistory(10);
    h.add(true);
    assertEquals(1, h.size());
  }

  @Test public void countEmpty() {
    final BooleanHistory h = new BooleanHistory(10);
    assertEquals(0, h.count(true));
    assertEquals(0, h.count(false));
  }

  @Test public void countFalseLoop() {
    final BooleanHistory h = new BooleanHistory(10);
    for (int i = 0; i < 1000; i++) {
      h.add(i % 3 == 1);
      int m = 0;
      for (int j = i; j >= 0 && j > i - 10; j--)
        m += As.binary(j % 3 != 1);
      assertEquals(m, h.count(false));
    }
  }

  @Test public void countFalseTrivial() {
    final BooleanHistory h = new BooleanHistory(10);
    h.add(false);
    h.add(false);
    assertEquals(2, h.count(false));
    assertEquals(0, h.count(true));
  }

  @Test public void countTrueLoop() {
    final BooleanHistory h = new BooleanHistory(10);
    for (int i = 0; i < 1000; i++) {
      h.add(i % 3 == 1);
      int m = 0;
      for (int j = i; j >= 0 && j > i - 10; j--)
        m += As.binary(j % 3 == 1);
      assertEquals(m, h.count(true));
    }
  }

  @Test public void countTrueTrivial() {
    final BooleanHistory h = new BooleanHistory(10);
    h.add(true);
    h.add(true);
    assertEquals(2, h.count(true));
    assertEquals(0, h.count(false));
  }

  @Test public void createSize() {
    final BooleanHistory h = new BooleanHistory(10);
    assertEquals(0, h.size());
  }

  @Test public void maxSize() {
    final BooleanHistory h = new BooleanHistory(10);
    for (int i = 0; i < 1000; i++) {
      h.add(i % 3 == 1);
      assertEquals(Math.min(i + 1, 10), h.size());
    }
  }
}

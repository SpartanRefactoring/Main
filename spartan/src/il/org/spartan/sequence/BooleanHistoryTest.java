package il.org.spartan.sequence;

import static il.org.spartan.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
@SuppressWarnings("static-method") public class BooleanHistoryTest {
  @Test public void addSize() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    h.add(true);
    azzert.that(h.size(), is(1));
  }

  @Test public void countEmpty() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    azzert.that(h.count(true), is(0));
    azzert.that(h.count(false), is(0));
  }

  @Test public void countFalseLoop() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    for (int i = 0; i < 1000; ++i) {
      h.add(i % 3 == 1);
      int m = 0;
      for (int j = i; j >= 0 && j > i - 10; --j)
        m += as.bit(j % 3 != 1);
      azzert.that(h.count(false), is(m));
    }
  }

  @Test public void countFalseTrivial() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    h.add(false);
    h.add(false);
    azzert.that(h.count(false), is(2));
    azzert.that(h.count(true), is(0));
  }

  @Test public void countTrueLoop() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    for (int i = 0; i < 1000; ++i) {
      h.add(i % 3 == 1);
      int m = 0;
      for (int j = i; j >= 0 && j > i - 10; --j)
        m += as.bit(j % 3 == 1);
      azzert.that(h.count(true), is(m));
    }
  }

  @Test public void countTrueTrivial() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    h.add(true);
    h.add(true);
    azzert.that(h.count(true), is(2));
    azzert.that(h.count(false), is(0));
  }

  @Test public void createSize() {
    azzert.that(new BooleanHistory(10).size(), is(0));
  }

  @Test public void maxSize() {
    @NotNull final BooleanHistory h = new BooleanHistory(10);
    for (int ¢ = 0; ¢ < 1000; ++¢) {
      h.add(¢ % 3 == 1);
      azzert.that(h.size(), is(Math.min(¢ + 1, 10)));
    }
  }
}

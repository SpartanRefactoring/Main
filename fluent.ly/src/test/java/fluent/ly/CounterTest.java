package fluent.ly;

import static fluent.ly.azzert.is;

import org.junit.Test;

import il.org.spartan.utils.Accumulator;
import il.org.spartan.utils.Accumulator.Counter;

@SuppressWarnings("static-method") public class CounterTest {
  @Test public void booleanAdds() {
    final Accumulator c = new Counter();
    azzert.that(c.value(), is(0));
    c.add(true);
    azzert.that(c.value(), is(1));
    c.add(false);
    azzert.that(c.value(), is(1));
    c.add(false);
    azzert.that(c.value(), is(1));
    c.add(true);
    azzert.that(c.value(), is(2));
    c.add(true);
    azzert.that(c.value(), is(3));
  }

  @Test public void emptyAdds() {
    final Accumulator.Counter c = new Counter();
    for (var ¢ = 0; ¢ < 19; ++¢)
      c.add();
    azzert.that(c.value(), is(19));
  }
}
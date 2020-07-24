package fluent.ly;

import static fluent.ly.azzert.is;

import org.junit.Test;

import il.org.spartan.utils.Accumulator;
import il.org.spartan.utils.Accumulator.Last;

@SuppressWarnings("static-method") public class AccumulatorLastTest {
  @Test public void booleanAdds() {
    final Accumulator.Last c = new Last();
    azzert.that(as.bit(false), is(0));
    azzert.that(c.value(), is(0));
    c.add(true);
    azzert.that(c.value(), is(1));
    azzert.that(as.bit(false), is(0));
    c.add(false);
    azzert.that(c.value(), is(0));
    c.add(false);
    azzert.that(c.value(), is(0));
    c.add(true);
    azzert.that(c.value(), is(1));
    c.add(true);
    azzert.that(c.value(), is(1));
  }

  @Test public void emptyAdds() {
    final Accumulator.Last c = new Last();
    for (int ¢ = 0; ¢ < 19; ++¢)
      c.add(¢);
    c.add(11);
    azzert.that(c.value(), is(11));
  }
}
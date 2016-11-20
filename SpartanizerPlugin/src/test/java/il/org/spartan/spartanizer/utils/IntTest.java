package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method")
public class IntTest {
  @Test public void testValueOf() {
    azzert.that(0, is(Int.valueOf(0).inner));
  }

  @Test public void testInner() {
    final Int $ = new Int();
    $.inner = 4;
    assert $.inner() == Integer.valueOf(4);
    $.inner += 3;
    assert $.inner() == Integer.valueOf(7);
  }
}

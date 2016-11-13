package il.org.spartan.spartanizer.utils;

import org.junit.*;

@SuppressWarnings("static-method") public class IntTest {
  @Test public void testInner() {
    Int $ = new Int();
    $.inner = 4;
    assert $.inner() == Integer.valueOf(4);
    $.inner += 3;
    assert $.inner() == Integer.valueOf(7);
  }
}

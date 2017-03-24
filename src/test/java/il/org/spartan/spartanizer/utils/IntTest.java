package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
/** Test case for class {@link Int}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since Nov 13, 2016
 */
@SuppressWarnings("static-method")
public class IntTest {
  @Test public void testInner() {
    @NotNull final Int $ = new Int();
    $.inner = 4;
    assert $.inner() == Integer.valueOf(4);
    $.inner += 3;
    assert $.inner() == Integer.valueOf(7);
  }

  @Test public void testValueOf() {
    azzert.that(0, is(Int.valueOf(0).inner));
  }
}

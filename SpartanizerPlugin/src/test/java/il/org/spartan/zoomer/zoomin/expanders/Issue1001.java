package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link AssignmentOperatorExpansion}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 */
@Ignore // TODO: Ori Roth
@SuppressWarnings("static-method")
public class Issue1001 {
  @Test public void basic() {
    bloatingOf("a += 1")//
        .gives("a = a + 1");
  }

  @Test public void inclusion() {
    bloatingOf("a += b += 1")//
        .gives("a = a + (b += 1)")//
        .gives("a = a + (b = b + 1)")//
        .stays();
  }

  @Test public void inclusion2() {
    bloatingOf("a += b = 1")//
        .gives("a = a + (b = 1)")//
        .stays();
  }

  @Test public void inclusion3() {
    bloatingOf("a = b += 1")//
        .gives("a = b = b + 1")//
        .stays();
  }

  @Test public void operators() {
    bloatingOf("a %= b |= 1")//
        .gives("a = a % (b |= 1)")//
        .gives("a = a % (b = b | 1)")//
        .stays();
  }
}

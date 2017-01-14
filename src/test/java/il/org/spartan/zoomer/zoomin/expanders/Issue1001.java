package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit test for {@link AssignmentOperatorExpansion}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 */
@Ignore // TODO: Ori Roth
@SuppressWarnings("static-method")
public class Issue1001 {
  @Test public void basic() {
    zoomingInto("a += 1").gives("a = a + 1");
  }

  @Test public void inclusion() {
    zoomingInto("a += b += 1").gives("a = a + (b += 1)").gives("a = a + (b = b + 1)").stays();
  }

  @Test public void inclusion2() {
    zoomingInto("a += b = 1").gives("a = a + (b = 1)").stays();
  }

  @Test public void inclusion3() {
    zoomingInto("a = b += 1").gives("a = b = b + 1").stays();
  }

  @Test public void operators() {
    zoomingInto("a %= b |= 1").gives("a = a % (b |= 1)").gives("a = a % (b = b | 1)").stays();
  }
}

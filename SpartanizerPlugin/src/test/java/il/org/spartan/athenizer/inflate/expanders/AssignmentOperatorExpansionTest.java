package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.zoomer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.expanders.*;

/** nit test for {@link AssignmentOperatorExpansion}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 [[SuppressWarningsSpartan]] */
@SuppressWarnings("static-method")
@Ignore
public class AssignmentOperatorExpansionTest {
  @Test public void basic() {
    expansionOf("a += 1").gives("a = a + 1");
  }

  @Test public void inclusion() {
    expansionOf("a += b += 1").gives("a = a + (b += 1)").gives("a = a + (b = b + 1)").stays();
  }

  @Test public void inclusion2() {
    expansionOf("a += b = 1").gives("a = a + (b = 1)").stays();
  }

  @Test public void inclusion3() {
    expansionOf("a = b += 1").gives("a = b = b + 1").stays();
  }

  @Test public void operators() {
    expansionOf("a %= b |= 1").gives("a = a % (b |= 1)").gives("a = a % (b = b | 1)").stays();
  }
}

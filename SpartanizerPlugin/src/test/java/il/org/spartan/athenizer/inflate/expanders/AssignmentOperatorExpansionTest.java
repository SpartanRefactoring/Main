package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** nit test for {@link AssignmentOperatorExpansion}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 [[SuppressWarningsSpartan]] */
@SuppressWarnings("static-method")
public class AssignmentOperatorExpansionTest {
  @Test public void basic() {
    expandingOf("a += 1").gives("a = a + 1");
  }

  @Test public void inclusion() {
    expandingOf("a += b += 1").gives("a = a + (b += 1)").gives("a = a + (b = b + 1)").stays();
  }

  @Test public void inclusion2() {
    expandingOf("a += b = 1").gives("a = a + (b = 1)").stays();
  }

  @Test public void inclusion3() {
    expandingOf("a = b += 1").gives("a = b = b + 1").stays();
  }
  
  @Test public void operators() {
    expandingOf("a %= b |= 1").gives("a = a % (b |= 1)").gives("a = a % (b = b | 1)").stays();
  }
}

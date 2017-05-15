package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test class for issue 115 - XOR parsing bug with trimmingOf testing utility.
 * @since 2016 */
@Ignore("Yossi - parsing with spaces, does not parse without. Space from '=' does not seem to be required")
@SuppressWarnings("static-method")
public class Issue0115 {
  @Test public void trimmerBugXOR_Notparsing01() {
    trimmingOf("j=j^k")//
        .gives("j^=k");
  }
  @Test public void trimmerBugXOR_Notparsing02() {
    trimmingOf("j = j ^ k")//
        .gives("j^=k");
  }
  @Ignore @Test public void xor() {
    trimmingOf("a ^= 2; a ^= 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a ^= 2 ^ 3;")//
    ;
  }
  @Test public void trimmerBugXOR_Notparsing03() {
    trimmingOf("j = j^ k")//
        .gives("j ^= k");
  }
  @Test public void trimmerBugXOR_Notparsing04() {
    trimmingOf("j = j ^k")//
        .gives("j ^= k");
  }
  @Test public void trimmerBugXOR_Parsing01() {
    trimmingOf("j = j ^ k")//
        .gives("j ^= k");
  }
  @Test public void trimmerBugXOR_Parsing02() {
    trimmingOf("j = j ^ k")//
        .gives("j ^=k");
  }
}

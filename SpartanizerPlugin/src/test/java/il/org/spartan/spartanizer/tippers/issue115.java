package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Test class for issue 115 - XOR parsing bug with trimmingOf testing utility.
 * @since 2016 */
@Ignore("Yossi - parsing with spaces, does not parse without. " + "Space from '=' does not seem to be required")
@SuppressWarnings("static-method")
public class issue115 {
  @Test public void trimmerBugXOR_Notparsing01() {
    trimmingOf("j=j^k").gives("j^=k");
  }

  @Test public void trimmerBugXOR_Notparsing02() {
    trimmingOf("j = j ^ k").gives("j^=k");
  }

  @Test public void trimmerBugXOR_Notparsing03() {
    trimmingOf("j = j^ k").gives("j ^= k");
  }

  @Test public void trimmerBugXOR_Notparsing04() {
    trimmingOf("j = j ^k").gives("j ^= k");
  }

  @Test public void trimmerBugXOR_Parsing01() {
    trimmingOf("j = j ^ k").gives("j ^= k");
  }

  @Test public void trimmerBugXOR_Parsing02() {
    trimmingOf("j = j ^ k").gives("j ^=k");
  }
}

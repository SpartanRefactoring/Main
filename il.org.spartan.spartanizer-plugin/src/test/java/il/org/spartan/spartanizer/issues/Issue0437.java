package il.org.spartan.spartanizer.issues;

import org.junit.*;

/** Failing tests from {@link InfixIndexOfToStringContainsTest} The reason these
 * tests fail is because {@link __.isString()} cannot infer types of variables
 * as strings unless they are string literals...
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0437 {
  /** Nothing to test as this issue is defunct: InfixIndexOfToStringContains */
  @Test public void test() {
    assert true;
  }
}

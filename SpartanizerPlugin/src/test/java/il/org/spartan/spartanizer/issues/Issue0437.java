package il.org.spartan.spartanizer.issues;

import org.junit.*;
import org.junit.runners.*;

/** Failing tests from {@link InfixIndexOfToStringContainsTest} The reason these
 * tests fail is because {@link type.isString()} cannot infer types of variables
 * as strings unless they are string literals...
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class Issue0437 {
  /** Nothing to test as this issue is defunct: InfixIndexOfToStringContains */
  @Test public void test() {
    assert true;
  }
}

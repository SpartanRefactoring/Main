package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.testing.*;

/** Failing tests from {@link InfixIndexOfToStringContainsTest} The reason these
 * tests fail is because {@link type.isString()} cannot infer types of variables
 * as strings unless they are string literals...
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings("static-method")
public class Issue0437 {
  @Test public void testMutation0() {
    trimmingOf("String str; String stringy; return str.indexOf(stringy)>= 0;")//
        .gives("String str; String stringy; return str.contains(stringy);")//
        .stays();
  }

  @Test public void testMutation1() {
    trimmingOf("String str; if(str.indexOf(\"stringy\")>= 0) return true;")//
        .gives("String str; if(str.contains(\"stringy\")) return true;")//
        .stays();
  }

  @Test public void testMutation2() {
    trimmingOf("String str; if(str.indexOf(stringy)>= 0) return true;")//
        .gives("String str; if(str.contains(stringy)) return true;")//
        .stays();
  }
}

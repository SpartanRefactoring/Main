package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0163 {
  @Test public void issue163_01() {
    trimmingOf("return \"remove the block: \" + n + \"\";")//
        .gives("return \"remove the block: \" + n;")//
        .stays();
  }

  @Test public void issue163_02() {
    trimmingOf("x + \"\" + f() + \"\" + g() + \"abc\"")//
        .gives("x + \"\" + f() + g() + \"abc\"")//
        .stays();
  }

  @Test public void issue163_03() {
    trimmingOf("x + \"\" + \"\"")//
        .gives("x+\"\"")//
        .stays();
  }

  @Test public void issue163_04() {
    trimmingOf("\"\"+\"\"+x +\"\"")//
        .gives("\"\"+\"\"+x")//
        .gives("\"\"+x")//
        .gives("x+\"\"")//
        .stays();
  }
}

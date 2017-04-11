package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0163 {
  @Test public void issue163_01() {
    topDownTrimming("return \"remove the block: \" + n + \"\";")//
        .gives("return \"remove the block: \" + n;")//
        .stays();
  }

  @Test public void issue163_02() {
    topDownTrimming("x + \"\" + f() + \"\" + g() + \"abc\"")//
        .gives("x + \"\" + f() + g() + \"abc\"")//
        .stays();
  }

  @Test public void issue163_03() {
    topDownTrimming("x + \"\" + \"\"")//
        .gives("x+\"\"")//
        .stays();
  }

  @Test public void issue163_04() {
    topDownTrimming("\"\"+\"\"+x +\"\"")//
        .gives("\"\"+\"\"+x")//
        .gives("\"\"+x")//
        .gives("x+\"\"")//
        .stays();
  }
}

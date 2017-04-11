package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0116 {
  @Test public void issue116_01() {
    topDownTrimming("\"\" + x")//
        .gives("x + \"\"")//
        .stays();
  }

  @Test public void issue116_02() {
    topDownTrimming("\"\" + x.foo()")//
        .gives("x.foo() + \"\"")//
        .stays();
  }

  @Test public void issue116_03() {
    topDownTrimming("\"\" + (Integer)(\"\" + x).length()")//
        .gives("(Integer)(\"\" + x).length() + \"\"")//
        .gives("(Integer)(x +\"\").length() + \"\"")//
        .stays();
  }

  @Test public void issue116_04() {
    topDownTrimming("String s = \"\" + x.foo();")//
        .gives("x.foo();")//
        .stays();
  }

  @Test public void issue116_07() {
    topDownTrimming("\"\" + 0 + (x - 7)")//
        .gives("0 + \"\" + (x - 7)")//
        .stays();
  }

  @Test public void issue116_08() {
    topDownTrimming("return x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"isEmpty()\" : \"\" + x + \".isEmpty()\");")
        .gives("return \"Use \" + ((x == null ? \"\" : \"\" + x + \".\")+\"isEmpty()\");")
        .gives("return \"Use \" + (x == null ? \"\" : \"\" + x  + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x +\"\" + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x + \".\")+\"isEmpty()\";")//
        .stays();
  }
}

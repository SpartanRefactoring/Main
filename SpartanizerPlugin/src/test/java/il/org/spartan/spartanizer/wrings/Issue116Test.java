package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue116Test {
  @Test public void issue116_01() {
    trimmingOf("\"\" + x").gives("x + \"\"").stays();
  }

  @Test public void issue116_02() {
    trimmingOf("\"\" + x.foo()").gives("x.foo() + \"\"").stays();
  }

  @Test public void issue116_03() {
    trimmingOf("\"\" + (Integer)(\"\" + x).length()").gives("(Integer)(\"\" + x).length() + \"\"").gives("(Integer)(x +\"\").length() + \"\"")
        .stays();
  }

  @Test public void issue116_04() {
    trimmingOf("String s = \"\" + x.foo();").gives("String s = x.foo() + \"\";").stays();
  }

  @Test public void issue116_07() {
    trimmingOf("\"\" + 0 + (x - 7)").gives("0 + \"\" + (x - 7)").stays();
  }

  @Test public void issue116_08() {
    trimmingOf("return x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"isEmpty()\" : \"\" + x + \".isEmpty()\");")
        .gives("return \"Use \" + ((x == null ? \"\" : \"\" + x + \".\")+\"isEmpty()\");")
        .gives("return \"Use \" + (x == null ? \"\" : \"\" + x  + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x +\"\" + \".\")+\"isEmpty()\";")
        .gives("return \"Use \" + (x == null ? \"\" : x + \".\")+\"isEmpty()\";").stays();
  }
}

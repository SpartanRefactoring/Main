package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue116Test {
  @Ignore @Test public void issue116_01() {
    trimming("\"\" + 0 + (x - 7)").to("0 + \"\" + (x - 7)").stays();
  }

  @Ignore @Test public void issue116_02() {
    trimming("return x == null ? \"Use isEmpty()\" : \"Use \" + x + \".isEmpty()\";")
        .to("return \"Use \" + (x == null ? \"isEmpty()\" : \"\" + x + \".isEmpty()\");")
        .to("return \"Use \" + ((x == null ? \"\" : \"\" + x + \".\")+\"isEmpty()\");")
        .to("return \"Use \" + ((x == null ? \"\" : x + \"\" + \".\")+\"isEmpty()\");")
        .to("return \"Use \" + ((x == null ? \"\" : x + \".\")+\"isEmpty()\");");
  }
}

package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue163Test {
  @Test public void issue163_01() {
    trimming("return \"remove the block: \" + n + \"\";").to("return \"remove the block: \" + n;").stays();
  }

  @Test public void issue163_02() {
    trimming("x + \"\" + f() + \"\" + g() + \"abc\"").to("x + \"\" + f() + g() + \"abc\"").stays();
  }

  @Test public void issue163_03() {
    trimming("x + \"\" + \"\"").to("x+\"\"").stays();
  }

  @Test public void issue163_04() {
    trimming("\"\"+\"\"+x +\"\"").to("\"\"+\"\"+x").to("\"\"+x").to("x+\"\"").stays();
  }
}

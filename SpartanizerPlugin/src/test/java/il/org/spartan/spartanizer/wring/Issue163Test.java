package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue163Test {
  @Test public void issue163_01() {
    trimming("return \"remove the block: \" + n + \"\";").to("return \"remove the block: \" + n;").stays();
  }
}

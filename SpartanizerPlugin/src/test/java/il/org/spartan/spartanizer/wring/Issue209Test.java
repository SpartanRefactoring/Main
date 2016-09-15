package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue209Test {
  @Test public void issue209_01() {
    trimming("new Integer(3).toString()").to("new Integer(3) + \"\"").stays();
  }
  
  @Test public void issue209_02() {
    trimming("new Integer(3).toString();").stays();
  }
}

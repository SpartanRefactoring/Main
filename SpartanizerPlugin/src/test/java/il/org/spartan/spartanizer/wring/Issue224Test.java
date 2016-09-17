package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016 
 * @see Issue209Test*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue224Test {
  @Test public void issue224_01() {
    trimming("a+b.toString()").to("a+\"\"+b").stays();
  }

  @Test public void issue224_02() {
    trimming("b.toString()").to("\"\"+b").to("b+\"\"").stays();
  }
  
  @Test public void issue224_03() {
    trimming("\"5\"+b.toString()").to("\"5\"+\"\"+b").to("\"5\"+b").stays();
  }
}

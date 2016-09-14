package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixTermsZero}
 * @author TODO Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue172Test {
  @Test public void issue172_01() {
    trimming("1+3*x+0").to("1+3*x");
  }

  @Test public void issue172_02() {
    trimming("1+3*x+0+\"\"").to("1+3*x+\"\"");
  }

  @Test public void issue172_03() {
    trimming("0+x+\"\"").stays();
  }

  @Test public void issue172_04() {
    trimming("2+1*x+0+\"abc\"+\"\"").to("2+1*x+\"abc\"").to("1*x+2+\"abc\"").to("x+2+\"abc\"").stays();
  }

  @Test public void issue172_05() {
    trimming("x+\"\"+\"abc\"+0").to("x+\"abc\"+0").stays();
  }
  
  @Test public void issue172_06() {
    trimming("0 + \"\"").stays();
  }

  @Test public void issue172_07() {
    trimming("\"\" + 0").to("0+\"\"").stays();
  }
  
  @Test public void issue172_08() {
    trimming("\"\" + 0 + 1").to("0+ \"\" + 1").stays();
  }
  
  @Test public void issue172_09() {
    trimming("x+1+0").stays();
  }
  
  @Test public void issue172_10(){
      trimming("0+x+1").stays();
  }
}

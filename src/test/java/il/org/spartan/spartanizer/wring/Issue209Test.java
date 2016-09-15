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
    trimming("new Integer(3).toString()").stays();
  }
  @Test public void issue54_01() {
    trimming("(x.toString())").to("x + \"\"");
  }

  @Test public void issue54_02() {
    trimming("if(x.toString() == \"abc\") return a;").to("if(x + \"\" == \"abc\") return a;");
  }

  @Test public void issue54_03() {
    trimming("((Integer)6).toString()").to("(Integer)6 + \"\"");
  }

  @Test public void issue54_04() {
    trimming("switch(x.toString()){ case \"1\": return; case \"2\": return; default: return; }")
        .to("switch(x + \"\"){ case \"1\": return; case \"2\": return; default: return; }");
  }

  @Test public void issue54_05() {
    trimming("x.toString(5)").stays();
  }

  @Test public void issue54_06() {
    trimming("a.toString().length()").to("(a + \"\").length()");
  }
  @Test public void issue209_02() {
    trimming("new Integer(3).toString();").stays();
  }
}

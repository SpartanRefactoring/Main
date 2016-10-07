package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixPlusRemoveParenthesis}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public final class Issue162 {
  // TODO: Mateu InfixAdditionZero fail.
  @Ignore @Test public void issue162_01() {
    trimmingOf("0+(0+x+y+(4))").gives("x+y+4").stays();
  }
  
  @Ignore("addition zero") 
  @Test public void issue162_01c() {
    trimmingOf("0+x+y+4").gives("x+y+4").stays();
  }
  
  @Ignore("addition zero") 
  @Test public void issue162_01d() {
    trimmingOf("0+x").gives("x").stays();
  }
  
  @Ignore("addition zero") 
  @Test public void issue162_01e() {
    trimmingOf("x+0").gives("x").stays();
  }

  @Ignore("addition zero") 
  @Test public void issue162_01d5() {
    trimmingOf("0+x+3").gives("x+3").stays();
  }
  
  @Ignore("addition zero") 
  @Test public void issue162_01e2() {
    trimmingOf("x+0+y").gives("x+y").stays();
  }


  @Test public void issue162_02() {
    trimmingOf("\"I ate\"+(\"an\"+\" ice cream sandwich\")").gives("\"I ate\"+\"an\"+\" ice cream sandwich\"").stays();
  }

  @Test public void issue162_03() {
    trimmingOf("(2*3)+\"\"").gives("2*3+\"\"").gives("6+\"\"").stays();
  }

  @Test public void issue162_04() {
    trimmingOf("\"a\"+(x-2)").stays();
  }

  @Test public void issue162_05() {
    trimmingOf("\"a\"+((x-2))").gives("\"a\"+(x-2)").stays();
  }

  @Test public void issue162_06() {
    trimmingOf("(\"a\")+(x-2)").gives("\"a\"+(x-2)").stays();
  }

  @Test public void issue162_07() {
    trimmingOf("(x-2)+\"abc\"").gives("x-2+\"abc\"");
  }

  @Test public void issue162_08() {
    trimmingOf("(f() ? x : y) + \".toString\"").stays();
  }

  @Test public void issue162_09() {
    trimmingOf("\"I \" + \"ate\"+(\"an\"+\" ice cream sandwich\")").gives("\"I \" + \"ate\"+\"an\"+\" ice cream sandwich\"").stays();
  }
}

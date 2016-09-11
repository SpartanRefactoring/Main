package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue162Test {
  @Test public void issue162_01() {
    trimming("0+(0+x+y+(4))").to("0+(0+x+y+4)").stays();
  }

  @Test public void issue162_02() {
    trimming("\"I ate\"+(\"an\"+\" ice cream sandwich\")").to("\"I ate\"+\"an\"+\" ice cream sandwich\"").stays();
  }

  @Test public void issue162_03() {
    trimming("(2*3)+\"\"").to("2*3+\"\"").to("6+\"\"").stays();
  }

  @Test public void issue162_04() {
    trimming("\"a\"+(x-2)").stays();
  }

  @Ignore("under construction") @Test public void issue162_05() {
    trimming("\"a\"+((x-2))").to("\"a\"+(x-2)").stays();
  }

  @Test public void issue162_06() {
    trimming("(\"a\")+(x-2)").to("\"a\"+(x-2)").stays();
  }
}

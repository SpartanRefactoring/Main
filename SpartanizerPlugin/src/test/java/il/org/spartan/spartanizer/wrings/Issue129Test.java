package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue129Test {
  @Test public void issue129_01() {
    trimming("$ += s + (new Integer(i) + \"\")").to("$ += s + (Integer.valueOf(i) + \"\")").stays();
  }

  @Test public void issue129_02() {
    trimming("1 + 2 - (x+1)").to("1+2-x-1").to("3-x-1").stays();
  }

  @Test public void issue129_03() {
    trimming("1 + 2 + (x+1)").to("1 + 2 + x + 1").stays();
  }

  @Test public void issue129_04() {
    trimming("\"\" + 0 + (x - 7)").to("0 + \"\" + (x - 7)").stays();
  }

  @Test public void issue129_05() {
    trimming("x + 5 + y + 7.0 +1.*f(3)").stays();
  }
}

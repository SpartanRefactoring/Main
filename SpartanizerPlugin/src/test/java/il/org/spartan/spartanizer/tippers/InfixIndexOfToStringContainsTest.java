package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class InfixIndexOfToStringContainsTest {
  @Ignore @SuppressWarnings("static-method") @Test public void testMutation1() {
    trimmingOf("if(str.indexOf(\"stringy\") >= 0) return true;").gives("if(str.contains(\"stringy\")) return true;").stays();
  }

  @Ignore @SuppressWarnings("static-method") @Test public void testMutation2() {
    trimmingOf("if(str.indexOf(stringy) >= 0) return true;").gives("if(str.contains(stringy)) return true;").stays();
  }

  @Ignore @SuppressWarnings("static-method") @Test public void testMutation0() {
    trimmingOf("String str; String stringy; return str.indexOf(stringy) >= 0;").gives("String str; String stringy; return str.contains(stringy);")
        .stays();
  }

  @SuppressWarnings("static-method") @Test public void testMutation3() {
    trimmingOf("\"str\".indexOf(\"stringy\") >= 0").gives("\"str\".contains(\"stringy\")").stays();
  }
}

package il.org.spartan.spartanizer.tippers;

import org.junit.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class InfixIndexOfToStringContainsTest {
  @SuppressWarnings("static-method") @Test public void testMutation1() {
    trimmingOf("if(str.indexOf(\"stringy\") >= 0) return true;").gives("if(str.contains(\"stringy\")) return true;").stays();
  }
  
  @SuppressWarnings("static-method") @Test public void testMutation2() {
    trimmingOf("if(str.indexOf(stringy) >= 0) return true;").gives("if(str.contains(stringy)) return true;").stays();
  }
  
  @SuppressWarnings("static-method") @Test public void testMutation0() {
    trimmingOf("str.indexOf(stringy) >= 0").gives("str.contains(stringy)").stays();
  }
}

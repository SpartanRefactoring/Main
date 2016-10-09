package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/**
 * Failing tests from {@link InfixIndexOfToStringContainsTest}
 * @since 2016 */
@SuppressWarnings("static-method") @FixMethodOrder(MethodSorters.NAME_ASCENDING) @Ignore public class Issue437 {
  @Test public void testMutation1() {
    trimmingOf("if(str.indexOf(\"stringy\") >= 0) return true;").gives("if(str.contains(\"stringy\")) return true;").stays();
  }

  @Test public void testMutation2() {
    trimmingOf("if(str.indexOf(stringy) >= 0) return true;").gives("if(str.contains(stringy)) return true;").stays();
  }

  @Test public void testMutation0() {
    trimmingOf("String str; String stringy; return str.indexOf(stringy) >= 0;").gives("String str; String stringy; return str.contains(stringy);")
        .stays();
  }

}

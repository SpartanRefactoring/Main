package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since Dec 3, 2016 */
@SuppressWarnings("static-method")
public class MatcherTest {
  @Test public void a() {
    assertTrue(new Matcher("$X", "").matches((wizard.ast("s"))));
  }

  @Test public void b() {
    assertTrue(new Matcher("for($N1 $N2 : $X1) $B", "").matches(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();"))));
  }

  @Test public void c() {
    assertEquals("print();\n",
        (new Matcher("for($N1 $N2 : $X1) $B", "")).getMatching(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();")), "$B") + "");
  }
}

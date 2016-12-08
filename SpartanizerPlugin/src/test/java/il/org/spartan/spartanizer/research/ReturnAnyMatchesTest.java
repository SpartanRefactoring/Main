package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ReturnAnyMatchesTest {
  @Ignore @Test public void a() {
    trimmingOf(" for (final UserDefinedTipper<Statement> ¢ : tippers) if (¢.canTip(s)) return true; return false;")
        .withTipper(Block.class, new ReturnAnyMatches()).gives("return tippers.stream().anyMatch(¢ -> ¢.canTip(s));");
  }
}

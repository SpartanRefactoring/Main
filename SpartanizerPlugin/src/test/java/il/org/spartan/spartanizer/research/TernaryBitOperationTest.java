package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class TernaryBitOperationTest {
  @Test public void basic() {
    trimmingOf("(k == 0) ? 1 : 0").withTipper(ConditionalExpression.class, new TernaryBitOperation()).gives("Bit.not(k)");
  }
}

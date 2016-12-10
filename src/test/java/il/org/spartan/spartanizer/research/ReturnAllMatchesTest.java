package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ReturnAllMatchesTest {
  @Test public void a() {
    trimmingOf("for (  Entry<?> ¢ : that.entrySet())   if (m.count(¢.getElement()) != ¢.getCount())   return false;  return true;")
        .withTipper(Block.class, new ReturnAllMatches()).gives("return that.entrySet().stream().allMatch(¢ -> !(m.count(¢.getElement()) != ¢.getCount()));");
  }
}

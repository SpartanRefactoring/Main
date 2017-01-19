package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class HoldsForAllTest {
  @Test public void a() {
    trimmingOf("for (  Entry<?> ¢ : that.entrySet())   if (m.count(¢.getElement()) != ¢.getCount())   return false;  return true;")
        .using(Block.class, new ReturnHoldsForAll())
        .gives("return that.entrySet().stream().allMatch(¢ -> !(m.count(¢.getElement()) != ¢.getCount()));");
  }
}
